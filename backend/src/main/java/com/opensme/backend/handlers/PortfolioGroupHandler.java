package com.opensme.backend.handlers;

import com.opensme.backend.DatabaseHelper;
import com.opensme.backend.models.PortfolioGroup;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.SQLException;

/**
 * Handles all portfolio_group.* WebSocket actions.
 */
public class PortfolioGroupHandler implements HandlerInterface {
    
    /**
     * Default constructor.
     */
    public PortfolioGroupHandler() {}

    @Override
    public Object handle(String action, JSONObject data) {
        switch (action) {
            case "portfolio_group.list":
                return listPortfolioGroups();
            default:
                throw new IllegalArgumentException("Unknown portfolio_group action: " + action);
        }
    }

    /**
     * Fetches all portfolio groups from the PORTFOLIO_GROUPS table.
     *
     * @return JSONArray of portfolio group records
     */
    private JSONArray listPortfolioGroups() {
        String sql = "SELECT ID, NAME, POS FROM PORTFOLIO_GROUPS ORDER BY POS";
        try {
            return DatabaseHelper.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }
}