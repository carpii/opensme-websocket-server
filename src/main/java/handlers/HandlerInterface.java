package handlers;

import org.json.JSONObject;

/**
 * Interface for handling WebSocket actions within a specific namespace.
 */
public interface HandlerInterface {

	/**
	 * Handles a specific action and returns a JSON string result.
	 *
	 * @param action the action to handle (e.g., "portfolio.get")
	 * @param data   the optional data payload
	 * @return a JSON-formatted string result
	 */
	String handle(String action, JSONObject data);
}
