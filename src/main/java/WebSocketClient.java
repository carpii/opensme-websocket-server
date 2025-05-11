import jakarta.websocket.*;
import org.json.JSONObject;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class WebSocketClient {
	private static final String SERVER_URI = "ws://localhost:8025/websockets/ws";
	private static final CountDownLatch latch = new CountDownLatch(1);

	@OnOpen
	public void onOpen(Session session) {
		try {
			JSONObject msg = new JSONObject();
			msg.put("requestID", UUID.randomUUID().toString());
			msg.put("action", "table.get");
			session.getAsyncRemote().sendText(msg.toString());
		} catch (Exception e) {
			JSONObject error = new JSONObject();
			error.put("error", "Failed to send message");
			error.put("detail", e.getMessage());
			System.err.println(error.toString());
			latch.countDown();
		}
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
		// Do nothing
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
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(WebSocketClient.class, URI.create(SERVER_URI));
		latch.await();
	}
}
