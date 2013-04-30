package org.kasource.web.websocket.spring.example;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.annotations.OnWebSocketEvent;
import org.kasource.web.websocket.annotations.OnWebSocketMessage;
import org.kasource.web.websocket.annotations.WebSocketListener;
import org.kasource.web.websocket.annotations.extensions.OnBase64Message;
import org.kasource.web.websocket.annotations.extensions.OnJsonMessage;
import org.kasource.web.websocket.annotations.extensions.OnXmlMessage;

import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketClientEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.WebSocketTextObjectMessageEvent;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.XmlProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@WebSocketListener("/chat/*")
@Controller
public class ChatController {

   
    
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
