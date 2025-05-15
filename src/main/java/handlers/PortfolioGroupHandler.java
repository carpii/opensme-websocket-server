package handlers;

import org.json.JSONArray;
import util.DatabaseHelper;

/**
 * Handles all portfolioGroup.* WebSocket actions.
 */
public class PortfolioGroupHandler implements HandlerInterface {
    
    /**
     * Default constructor.
     */
    public PortfolioGroupHandler() {}

    @Override
    public Object handle(String action, org.json.JSONObject data) {
        switch (action) {
            case "portfolioGroup.list":
                return listPortfolioGroups(); // returns JSONArray directly
            default:
                throw new IllegalArgumentException("Unknown portfolioGroup action: " + action);
        }
    }

    /**
     * Fetches all portfolio groups from the PORTFOLIO_GROUPS table.
     *
     * @return JSONArray of portfolio group records
     */
    private JSONArray listPortfolioGroups() {
        try {
            return DatabaseHelper.fetchAllRows("PORTFOLIO_GROUPS");
        } catch (Exception e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
    }
}