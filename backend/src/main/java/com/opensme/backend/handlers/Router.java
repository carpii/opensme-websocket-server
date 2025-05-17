package com.opensme.backend.handlers;

import com.opensme.backend.models.Portfolio;
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
        registry.put("portfolio_group", new PortfolioGroupHandler());
        registry.put("table", new TableHandler());
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
        try {
            String[] parts = action.split("\\.");
            String handlerName = parts[0];
            HandlerInterface handler = registry.get(handlerName);
            
            if (handler == null) {
                throw new RuntimeException("No handler found for: " + handlerName);
            }

            JSONObject response = new JSONObject();
            response.put("requestID", requestId);
            response.put("data", handler.handle(action, data));
            return response.toString();

        } catch (Exception e) {
            return createErrorResponse(requestId, "Handler error: " + e.getMessage());
        }
    }

    /**
     * Creates an error response JSON string.
     *
     * @param requestId unique identifier for the request
     * @param errorMessage the error message to include in the response
     * @return JSON string containing the error response
     */
    private static String createErrorResponse(String requestId, String errorMessage) {
        JSONObject response = new JSONObject();
        response.put("requestID", requestId);
        response.put("error", errorMessage);
        return response.toString();
    }
}
