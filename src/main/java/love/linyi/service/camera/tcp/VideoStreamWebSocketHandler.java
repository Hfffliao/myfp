package love.linyi.service.camera.tcp;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
@Component
public class VideoStreamWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("WebSocket客户端连接: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("WebSocket客户端断开: " + session.getId());
    }

    public void broadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    System.err.println("发送消息到WebSocket客户端失败: " + e.getMessage());
                }
            }
        }
    }
    // 新增：支持发送字节数组的广播方法
    public void broadcastBinaryMessage(byte[] data) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    // 使用BinaryMessage发送原始字节数组
                    session.sendMessage(new BinaryMessage(data));
                } catch (IOException e) {
                    System.err.println("发送二进制消息到WebSocket客户端失败: " + e.getMessage());
                }
            }
        }
    }
    public int getConnectedClientsCount() {
        return sessions.size();
    }
}
