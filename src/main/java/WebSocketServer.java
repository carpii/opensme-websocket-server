import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

@ServerEndpoint(value = "/ws")
public class WebSocketServer {

	private static final int PORT = 8025;
	private static final String DB_PATH = Paths.get("db", "sme").toString();

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Client connected: " + session.getId());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		if ("SHOW TABLES".equalsIgnoreCase(message.trim())) {
			session.getBasicRemote().sendText(getTableList());
		} else {
			session.getBasicRemote().sendText("ACK: " + message);
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

	private String getTableList() {
		StringBuilder result = new StringBuilder();
		try (Connection conn = DriverManager.getConnection("jdbc:h2:" + DB_PATH);
			ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
			while (rs.next()) {
				result.append(rs.getString("TABLE_NAME")).append("\n");
			}
		} catch (SQLException e) {
			return "DB error: " + e.getMessage();
		}
		return result.length() > 0 ? result.toString().trim() : "(no tables)";
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
