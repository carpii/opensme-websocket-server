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


@ServerEndpoint(value = "/ws")
public class WebSocketServer {

	private static final int PORT = 8025;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Client connected: " + session.getId());
	}

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
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Disconnected: " + session.getId() + " (" + reason + ")");
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("Error from " + session.getId() + ": " + throwable.getMessage());
	}

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
