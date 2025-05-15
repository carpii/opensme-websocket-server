package backend.handlers;

import backend.handlers.PortfolioHandler;
import backend.handlers.PortfolioGroupHandler;
import backend.models.Portfolio;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Delegates WebSocket messages to the appropriate namespace handler.
 */
public class Router {

    /**
     * Registry mapping namespace strings to their handlers
     */
    private static final Map<String, HandlerInterface> registry = new HashMap<>();

    static {
        registry.put("portfolio", new PortfolioHandler());
        registry.put("portfolioGroup", new PortfolioGroupHandler());
        registry.put("table", new TableHandler()); // Register TableHandler
    }

    /**
     * Default constructor.
     */
    public Router() {}

    /**
     * Handles a WebSocket request by routing it to the appropriate handler.
     *
     * @param requestId unique identifier for the request
     * @param action the action to handle (e.g., "portfolio.get")
     * @param data JSON object containing action parameters
     * @return JSON string containing the response
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

            Object result = handler.handle(action, data);
            response.put("result", result);
        } catch (Exception e) {
            response.put("error", "Handler error: " + e.getMessage());
        }

        return response.toString();
    }
}
