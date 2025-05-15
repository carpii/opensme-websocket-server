package handlers;

import org.json.JSONObject;

/**
 * Interface for handling WebSocket actions within a specific namespace.
 */
public interface HandlerInterface {
	Object handle(String action, JSONObject data);
}
