package org.kasource.web.websocket.spring.example;

import java.io.IOException;

import javax.annotation.Resource;

import org.kasource.web.websocket.NoSuchWebSocketClient;
import org.kasource.web.websocket.WebSocket;
import org.kasource.web.websocket.annotations.WebSocketEventListener;
import org.kasource.web.websocket.annotations.WebSocketTextListener;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientEvent;
import org.kasource.web.websocket.event.WebSocketEventType;
import org.springframework.stereotype.Controller;



@Controller
public class ChatController {

    @Resource(name = "chatWebSocket")
    private WebSocket webSocket;
    
    @WebSocketTextListener("chat")
    public void recieveMessage(String message, String username) {
        webSocket.broadcast(username + " says: "+message);
    }
    
    @WebSocketEventListener(value = "chat", filter = WebSocketEventType.CLIENT_CONNECTED)
    public void onClientConnect(WebSocketClientConnectionEvent event) throws NoSuchWebSocketClient, IOException {
        webSocket.sendMessage(event.getClientId(), "Welcome " + event.getClientId());
        webSocket.broadcast(event.getClientId() + " joined the conversation.");
    }
    
    @WebSocketEventListener(value = "chat", filter = WebSocketEventType.CLIENT_DISCONNECTED)
    public void onClientDisconnect(WebSocketClientEvent event) {
        webSocket.broadcast(event.getClientId() + " left the conversation.");
    }
    
    
    
}
