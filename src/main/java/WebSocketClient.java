import jakarta.websocket.*;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class WebSocketClient {
	private static final String SERVER_URI = "ws://localhost:8025/websockets/ws";
	private static final CountDownLatch latch = new CountDownLatch(1);

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connection established");
		try {
			session.getBasicRemote().sendText("SHOW TABLES");
		} catch (Exception e) {
			System.err.println("Failed to send message: " + e.getMessage());
			latch.countDown();
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Server response:\n" + message);
		latch.countDown();
		try {
			session.close();
		} catch (Exception ignored) {}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Connection closed: " + reason);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("Error: " + throwable.getMessage());
		latch.countDown();
	}

	public static void main(String[] args) throws Exception {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(WebSocketClient.class, URI.create(SERVER_URI));
		latch.await();
	}
}
