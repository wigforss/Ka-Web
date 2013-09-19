package org.kasource.web.websocket.spring.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.annotations.OnWebSocketEvent;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.register.WebSocketListenerRegister;

@WebSocketListener("/chat/*")
public class ChatServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
      WebSocketConfig config = (WebSocketConfig) getServletContext().getAttribute(WebSocketConfig.class.getName());
      WebSocketListenerRegister register = config.getListenerRegister();
      register.registerListener(this);
    }
    
    @OnWebSocketEvent
    public void recieveMessage(WebSocketTextMessageEvent event) {
        WebSocketChannel socket = event.getSource();
      
        socket.broadcast(event.getUsername()+ " says: " + event.getMessage());
    }
    
    @OnWebSocketEvent
    public void onClientConnect(WebSocketClientConnectionEvent event) throws NoSuchWebSocketClient, IOException {
        WebSocketChannel socket = event.getSource();
        socket.sendMessage("Welcome " + event.getUsername(), event.getClientId(), RecipientType.CLIENT_ID);
        
        socket.broadcast(event.getUsername() + " joined the conversation.");
    }
    
    @OnWebSocketEvent
    public void onClientDisconnect(WebSocketClientDisconnectedEvent event) {
        WebSocketChannel socket = event.getSource();
       socket.broadcast(event.getUsername() + " left the conversation.");
    }
    
}
