package vn.giabaoblog.giabaoblogserver.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> queryParams = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }

        return queryParams;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();

        Map<String, String> queryParams = parseQueryString(query);
        String userIdStr = queryParams.get("userId");

        if (userIdStr != null) {
            Long userId = Long.valueOf(userIdStr);
            userSessions.put(userId, session);
            session.getAttributes().put("userId", userId);
        } else {
            session.close();
            System.out.println("userId not found in query string, closing session.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = getUserIdFromSession(session);
        userSessions.remove(userId);
    }

    public void sendNotification(Long userId, String message) throws IOException {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        return (Long) session.getAttributes().get("userId");
    }
}
