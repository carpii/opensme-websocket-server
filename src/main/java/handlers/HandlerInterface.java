package handlers;

import org.json.JSONObject;

/**
 * Interface for handling WebSocket actions within a specific namespace.
 */
public interface HandlerInterface {
    /**
     * Handles a WebSocket action with optional data.
     *
     * @param action the action to handle (e.g., "portfolio.get")
     * @param data JSON object containing action parameters
     * @return Object containing the action result
     */
    Object handle(String action, JSONObject data);
}
