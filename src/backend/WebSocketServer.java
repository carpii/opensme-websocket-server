package backend;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;
import backend.handlers.Router;

/**
 * WebSocket server implementation that handles client connections and routes requests.
 */
@ServerEndpoint(value = "/ws")
public class WebSocketServer {

    /**
     * Creates a new WebSocket server instance.
     */
    public WebSocketServer() {
        // Default constructor
    }

    /** Port number the server listens on */
    private static final int PORT = 8025;

    /**
     * Handles new WebSocket connections.
     * 
     * @param session The WebSocket session
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected: " + session.getId());
    }

    /**
     * Handles incoming WebSocket messages.
     * 
     * @param message The received message
     * @param session The WebSocket session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received: " + message); // log incoming message
        try {
            JSONObject json = new JSONObject(message);
            String requestId = json.optString("requestID", UUID.randomUUID().toString());
            String action = json.optString("action", "");
            JSONObject data = json.optJSONObject("data");
            String response = Router.handle(requestId, action, data);
            session.getAsyncRemote().sendText(response);
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("requestID", UUID.randomUUID().toString());
            error.put("error", "Invalid message format: " + e.getMessage());
            session.getAsyncRemote().sendText(error.toString());
        }
    }

    /**
     * Handles WebSocket connection closures.
     * 
     * @param session The WebSocket session
     * @param reason The reason for closure
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Disconnected: " + session.getId() + " (" + reason + ")");
    }

    /**
     * Handles WebSocket errors.
     * 
     * @param session The WebSocket session
     * @param throwable The error that occurred
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error from " + session.getId() + ": " + throwable.getMessage());
    }

    /**
     * Main entry point for the WebSocket server.
     * 
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 driver not found: " + e.getMessage());
            System.exit(1);
        }

        Server server = new Server("localhost", PORT, "/websockets", null, WebSocketServer.class);
        Thread serverThread = new Thread(() -> {
            try {
                server.start();
                System.out.println("WebSocket server started on ws://localhost:" + PORT + "/websockets/ws");
            } catch (DeploymentException e) {
                System.err.println("Failed to start server: " + e.getMessage());
                System.exit(1);
            }
        });
        serverThread.start();

        System.out.println("App logic continues. Type 'quit' to shut down...");
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if ("quit".equalsIgnoreCase(scanner.nextLine())) {
                    server.stop();
                    break;
                }
            }
        }
    }
}
