import jakarta.websocket.*;
import org.json.JSONObject;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * WebSocket client that sends a single action and prints the JSON response.
 * Usage: java WebSocketClient <action> [data]
 * Example: java WebSocketClient portfolio.list '{"id":"12345"}'
 */
@ClientEndpoint
public class WebSocketClient {
	private static final String SERVER_URI = "ws://localhost:8025/websockets/ws";
	private static final CountDownLatch latch = new CountDownLatch(1);
	private final JSONObject outgoingMessage;

	public WebSocketClient(JSONObject message) {
		this.outgoingMessage = message;
	}

	@OnOpen
	public void onOpen(Session session) {
		session.getAsyncRemote().sendText(outgoingMessage.toString());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println(message);
		latch.countDown();
		try {
			session.close();
		} catch (Exception ignored) {}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// no-op
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		JSONObject error = new JSONObject();
		error.put("error", "WebSocket error");
		error.put("detail", throwable.getMessage());
		System.err.println(error.toString());
		latch.countDown();
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: WebSocketClient <action> [data]");
			System.exit(1);
		}

		String action = args[0];
		String dataPayload = args.length > 1 ? args[1] : ""; // Optional second argument

		JSONObject msg = new JSONObject();
		msg.put("requestID", UUID.randomUUID().toString());
		msg.put("action", action);

		// Include data only if it's provided
		if (!dataPayload.isEmpty()) {
			JSONObject data = new JSONObject(dataPayload);
			msg.put("data", data);
		} else {
			msg.put("data", new JSONObject()); // Default to an empty JSON object
		}

		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(new WebSocketClient(msg), URI.create(SERVER_URI));
		latch.await();
	}
}
