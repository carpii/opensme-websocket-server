package com.opensme.backend;

import jakarta.websocket.*;
import org.json.JSONObject;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * WebSocket client implementation for testing the server.
 * Command line usage: WebSocketClient action [data]
 * 
 * Example:
 * java WebSocketClient portfolio.list
 * java WebSocketClient portfolio.get {"id":"1"}
 */
@ClientEndpoint
public class WebSocketClient {
    /** URI of the WebSocket server */
    private static final String SERVER_URI = "ws://localhost:8025/websockets/ws";
    
    /** Latch for synchronizing client shutdown */
    private static final CountDownLatch latch = new CountDownLatch(1);
    
    /** Message to be sent to the server */
    private final JSONObject outgoingMessage;

    /**
     * Creates a new WebSocket client with a message to send.
     * 
     * @param message The JSON message to send to the server
     */
    public WebSocketClient(JSONObject message) {
        this.outgoingMessage = message;
    }

    /**
     * Handles WebSocket connection open event.
     * 
     * @param session The WebSocket session
     */
    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText(outgoingMessage.toString());
    }

    /**
     * Handles incoming WebSocket messages.
     * 
     * @param message The received message
     * @param session The WebSocket session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
        latch.countDown();
        try {
            session.close();
        } catch (Exception ignored) {}
    }

    /**
     * Handles WebSocket connection close event.
     * 
     * @param session The WebSocket session
     * @param reason The reason for closure
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        // no-op
    }

    /**
     * Handles WebSocket errors.
     * 
     * @param session The WebSocket session
     * @param throwable The error that occurred
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        JSONObject error = new JSONObject();
        error.put("error", "WebSocket error");
        error.put("detail", throwable.getMessage());
        System.err.println(error.toString());
        latch.countDown();
    }

    /**
     * Main entry point for the WebSocket client.
     * 
     * @param args Command line arguments [action, data]
     * @throws Exception If connection or communication fails
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: WebSocketClient <action> [data]");
            System.exit(1);
        }

        String action = args[0];
        String dataPayload = args.length > 1 ? args[1] : ""; 

        JSONObject msg = new JSONObject();
        msg.put("requestID", UUID.randomUUID().toString());
        msg.put("action", action);

        if (!dataPayload.isEmpty()) {
            JSONObject data = new JSONObject(dataPayload);
            msg.put("data", data);
        } else {
            msg.put("data", new JSONObject());
        }

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(new WebSocketClient(msg), URI.create(SERVER_URI));
        latch.await();
    }
}
