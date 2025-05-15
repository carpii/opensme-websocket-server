package handlers;

import handlers.HandlerInterface;
import models.Portfolio; // Import the Portfolio class
import org.json.JSONArray;
import org.json.JSONObject;
import util.DatabaseHelper;

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

    /**
     * Default constructor.
     */
    public PortfolioHandler() {}

    @Override
    public Object handle(String action, JSONObject data) {
        switch (action) {
            case "portfolio.get":
                return getPortfolio(data); // returns JSONObject
            case "portfolio.list":
                return listPortfolios();   // returns JSONArray
            default:
                throw new IllegalArgumentException("Unknown portfolio action: " + action);
        }
    }

    /**
     * Fetches a specific portfolio by ID.
     *
     * @param data JSON object containing the portfolio ID
     * @return JSON object with portfolio details
     */
    private JSONObject getPortfolio(JSONObject data) {
        if (data == null || !data.has("id")) {
            throw new IllegalArgumentException("Missing 'id'");
        }

        String portfolioId = data.getString("id");
        System.out.println("DEBUG: Received portfolio ID: " + portfolioId); // Debug log

        JSONObject portfolio = new JSONObject();

        String query = "SELECT * FROM PORTFOLIOS WHERE ID = ?";
        try (
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, portfolioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    portfolio.put("id", rs.getInt("ID"));
                    portfolio.put("name", rs.getString("NAME"));
                    portfolio.put("description", rs.getString("DESCRIPTION"));
                    portfolio.put("currency", rs.getString("CURRENCY"));
                    portfolio.put("dateAdded", rs.getTimestamp("DATE_ADDED"));
                    portfolio.put("syncUpdatedAt", rs.getTimestamp("SYNC_UPDATED_AT"));
                    portfolio.put("syncDeletedAt", rs.getTimestamp("SYNC_DELETED_AT"));
                    portfolio.put("syncIgnore", rs.getBoolean("SYNC_IGNORE"));
                    portfolio.put("pos", rs.getInt("POS"));
                } else {
                    throw new IllegalArgumentException("Portfolio not found for ID: " + portfolioId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }

        return portfolio;
    }

    /**
     * Fetches all portfolios from the PORTFOLIOS table as a JSON array.
     *
     * @return JSONArray of portfolio records
     */
    private JSONArray listPortfolios() {
        try {
            return DatabaseHelper.fetchAllRows("PORTFOLIOS");
        } catch (Exception e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches all portfolios from the PORTFOLIOS table as a list of Portfolio objects.
     *
     * @return List of Portfolio objects
     */
    private List<Portfolio> getAllPortfolios() {
        List<Portfolio> portfolios = new ArrayList<>();

        String query = "SELECT * FROM PORTFOLIOS";
        try (
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Portfolio p = new Portfolio(
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
                portfolios.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }

        return portfolios;
    }
}
