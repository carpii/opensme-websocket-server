package com.opensme.backend.handlers;

import com.opensme.backend.DatabaseHelper;
import com.opensme.backend.models.Portfolio;
import com.opensme.backend.models.PortfolioItem;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all portfolio.* WebSocket actions.
 */
public class PortfolioHandler implements HandlerInterface {

    private static final String PORTFOLIO_FIELDS = 
        "ID, POS, NAME, DATE_ADDED, DESCRIPTION, CASH, CURRENCY, " +
        "DEFAULT_COSTS, OVERRIDES, UUID, SYNC_UPDATED_AT, SYNC_DELETED_AT, " +
        "SYNC_POSITION_UPDATED_AT, SYNC_IGNORE";

    private static final String PORTFOLIO_ITEM_FIELDS =
        "ID, PORTFOLIO_ID, POS, SYMBOL, NAME, POSITION_TYPE, " +
        "DATE_ADDED, CURRENCY, BUY_PRICE, SHARES, ACB_PER_SHARE, " +
        "CACHED_LAST, TARGET_PRICE, PROVIDER, EXCHANGE, COMMENT, " +
        "OVERRIDES, TRANSACTION_UUID, UUID, SYNC_UPDATED_AT, " +
        "SYNC_DELETED_AT, SYNC_POSITION_UPDATED_AT, SYNC_IGNORE, " +
        "SYNC_CACHEDPRICE_UPDATED_AT, LAST_UPDATED_AT";

    /**
     * Default constructor.
     */
    public PortfolioHandler() {}

    @Override
    public Object handle(String action, JSONObject data) {
        switch (action) {
            case "portfolio.list":
                return listPortfolios();
            case "portfolio.get":
                return getPortfolio(data);
            case "portfolio.get_items":    // Changed from portfolio.getitems
                return getPortfolioItems(data);
            default:
                throw new IllegalArgumentException("Unknown portfolio action: " + action);
        }
    }

    /**
     * Creates a Portfolio object from a ResultSet row.
     */
    private Portfolio createPortfolioFromResultSet(ResultSet rs) throws SQLException {
        return new Portfolio(
            rs.getInt("ID"),
            rs.getInt("POS"),
            rs.getString("NAME"),
            rs.getTimestamp("DATE_ADDED"),
            rs.getString("DESCRIPTION"),
            rs.getBigDecimal("CASH"),
            rs.getString("CURRENCY"),
            rs.getBigDecimal("DEFAULT_COSTS"),
            rs.getString("OVERRIDES"),
            rs.getString("UUID"),
            rs.getTimestamp("SYNC_UPDATED_AT"),
            rs.getTimestamp("SYNC_DELETED_AT"),
            rs.getTimestamp("SYNC_POSITION_UPDATED_AT"),
            rs.getBoolean("SYNC_IGNORE")
        );
    }

    /**
     * Fetches one or all portfolios from the PORTFOLIOS table.
     *
     * @param portfolioId Optional ID to fetch specific portfolio, null for all
     * @return List of Portfolio objects
     */
    private List<Portfolio> getPortfolios(Integer portfolioId) {
        List<Portfolio> portfolios = new ArrayList<>();
        
        String query = "SELECT " + PORTFOLIO_FIELDS + " FROM PORTFOLIOS";
        if (portfolioId != null) {
            query += " WHERE ID = ?";
        }

        try (
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            if (portfolioId != null) {
                stmt.setInt(1, portfolioId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    portfolios.add(createPortfolioFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }

        if (portfolioId != null && portfolios.isEmpty()) {
            throw new IllegalArgumentException("Portfolio not found for ID: " + portfolioId);
        }

        return portfolios;
    }

    private JSONObject getPortfolio(JSONObject data) {
        if (data == null || !data.has("id")) {
            throw new IllegalArgumentException("Missing 'id'");
        }
        return getPortfolios(data.getInt("id"))
            .get(0)
            .toJSON();
    }

    public List<Portfolio> getPortfolios() {
        return getPortfolios(null);
    }

    /**
     * Fetches all portfolios from the PORTFOLIOS table as a JSON array.
     *
     * @return JSONArray of portfolio records
     */
    public JSONArray listPortfolios() {
        String sql = "SELECT " + PORTFOLIO_FIELDS + " FROM PORTFOLIOS";
        try {
            return DatabaseHelper.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches portfolio items for a specific portfolio ID.
     *
     * @param data JSON object containing the portfolio ID
     * @return JSONArray of portfolio items
     */
    public JSONArray getPortfolioItems(JSONObject data) {
        if (data == null || !data.has("id")) {
            throw new IllegalArgumentException("Missing portfolio id");
        }

        int portfolioId = data.getInt("id");
        String sql = "SELECT " + PORTFOLIO_ITEM_FIELDS + 
                    " FROM PORTFOLIO_ITEMS WHERE PORTFOLIO_ID = ?";
        try {
            return DatabaseHelper.executeQuery(sql, String.valueOf(portfolioId));
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches portfolio items for a specific portfolio ID as a list of PortfolioItem objects using JSON conversion.
     *
     * @param portfolioId ID of the portfolio
     * @return List of PortfolioItem objects
     */
    public List<PortfolioItem> getItems(int portfolioId) {
        JSONObject request = new JSONObject();
        request.put("id", portfolioId);
        JSONArray jsonArray = getPortfolioItems(request);
        
        List<PortfolioItem> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(PortfolioItem.fromJSON(jsonArray.getJSONObject(i)));
        }
        return items;
    }
}
