package handlers;

import handlers.portfolio.PortfolioHandler;
import handlers.table.TableHandler;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Delegates WebSocket messages to the appropriate namespace handler.
 */
public class Router {

	private static final Map<String, HandlerInterface> registry = new HashMap<>();

	static {
		registry.put("portfolio", new PortfolioHandler());
		registry.put("table", new TableHandler());
	}

	/**
	 * Default constructor.
	 */
	public Router() {}

	/**
	 * Routes an incoming action to the correct handler based on its namespace.
	 *
	 * @param requestId the ID of the client request
	 * @param action    the full action string (e.g., "portfolio.get")
	 * @param data      the optional data payload
	 * @return a JSON-formatted response string
	 */
	public static String handle(String requestId, String action, JSONObject data) {
		JSONObject response = new JSONObject();
		response.put("requestID", requestId);

		try {
			String[] parts = action.split("\\.", 2);
			if (parts.length != 2) {
				throw new IllegalArgumentException("Invalid action format");
			}
			String namespace = parts[0];
			HandlerInterface handler = registry.get(namespace);
			if (handler == null) {
				throw new IllegalArgumentException("Unknown namespace: " + namespace);
			}
			response.put("result", new JSONObject(handler.handle(action, data)));
		} catch (Exception e) {
			response.put("error", "Handler error: " + e.getMessage());
		}

		return response.toString();
	}
}
