package handlers.portfolio;

import handlers.HandlerInterface;
import org.json.JSONObject;
import org.json.JSONArray;
import util.DatabaseHelper;

/**
 * Handles all portfolio.* WebSocket actions.
 */
public class PortfolioHandler implements HandlerInterface {

	@Override
	public String handle(String action, JSONObject data) {
		switch (action) {
			case "portfolio.get":
				return getPortfolio(data);
			case "portfolio.list":
				return listPortfolios();
			default:
				throw new IllegalArgumentException("Unknown portfolio action: " + action);
		}
	}

	private String getPortfolio(JSONObject data) {
		if (data == null || !data.has("id")) {
			throw new IllegalArgumentException("Missing 'id'");
		}
		JSONObject result = new JSONObject();
		result.put("id", data.getString("id"));
		result.put("details", "Portfolio data stub");
		return result.toString();
	}

	/**
	 * Fetches all portfolios from the PORTFOLIOS table.
	 *
	 * @return JSON string with an array under key "portfolios"
	 */
	private String listPortfolios() {
		JSONObject result = new JSONObject();
		try {
			JSONArray portfolios = DatabaseHelper.fetchAllRows("PORTFOLIOS");
			result.put("portfolios", portfolios);
		} catch (Exception e) {
			result.put("error", "DB error: " + e.getMessage());
		}
		return result.toString();
	}
}
