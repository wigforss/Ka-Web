package org.kasource.web.websocket.guice.example.web;



import java.io.IOException;

import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.annotations.OnWebSocketEvent;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;

import com.google.inject.Singleton;

@WebSocketListener("/chat")
@Singleton
public class ChatServer {

    @OnWebSocketEvent
    public void onMessage(WebSocketTextMessageEvent event) {
        event.getSource().broadcast(event.getClientId() + " says: " + event.getMessage());
    }
    
    @OnWebSocketEvent
    public void onClientConnect(WebSocketClientConnectionEvent event) throws NoSuchWebSocketClient, IOException {
        WebSocketChannel channel = event.getSource();
        channel.sendMessage("Welcome " + event.getUsername(), event.getClientId(), RecipientType.CLIENT_ID);
        channel.broadcast(event.getClientId() + " joined the conversation.");
    }
    
    @OnWebSocketEvent
    public void onClientDisconnect(WebSocketClientDisconnectedEvent event) {
        event.getSource().broadcast(event.getClientId() + " left the conversation.");
    }
    
}
