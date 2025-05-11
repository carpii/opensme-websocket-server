package handlers.portfolio;

import handlers.HandlerInterface;
import org.json.JSONObject;


/**
 * Handles all portfolio.* WebSocket actions.
 */
public class PortfolioHandler implements HandlerInterface {

	/**
	 * Default constructor.
	 */
	public PortfolioHandler() {}


	@Override
	public String handle(String action, JSONObject data) {
		switch (action) {
			case "portfolio.get":
				return getPortfolio(data);
			case "portfolio.list":
				return listPortfolios(data);
			default:
				throw new IllegalArgumentException("Unknown portfolio action: " + action);
		}
	}

	/**
	 * Handles portfolio.get action.
	 *
	 * @param data JSON object with required "id" field
	 * @return JSON string with portfolio data (stub)
	 */
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
	 * Handles portfolio.list action.
	 *
	 * @param data optional data (unused)
	 * @return JSON string with portfolio list stub
	 */
	private String listPortfolios(JSONObject data) {
		JSONObject result = new JSONObject();
		result.put("portfolios", "Portfolio list stub");
		return result.toString();
	}
}
