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

    @Autowired
    private WebSocketChannelFactory channelFactory;
    
    @OnWebSocketEvent
    public void recieveMessage(WebSocketTextObjectMessageEvent event) {
        WebSocketChannel socket = event.getSource();
        Message message = event.getMessageAsObject(Message.class);
        message.setBody(event.getClientId() + " says: " + message.getBody());
        socket.broadcastObject(message, event.getProtocolHandler());
    }
    
    @OnWebSocketEvent
    public void onClientConnect(WebSocketClientConnectionEvent event) throws NoSuchWebSocketClient, IOException {
        WebSocketChannel socket = event.getSource();
        Message message = new Message();
        message.setBody("Welcome " + event.getUsername());
        socket.sendMessageAsXml(message, event.getClientId(), RecipientType.CLIENT_ID);
        message.setBody(event.getClientId() + " joined the conversation.");
        socket.broadcastXmlMessage(event.getClientId() + " joined the conversation.");
    }
    
    @OnWebSocketEvent
    public void onClientDisconnect(WebSocketClientDisconnectedEvent event) {
        WebSocketChannel socket = event.getSource();
        Message message = new Message();
        message.setBody(event.getUsername() + " left the conversation.");
       socket.broadcastXmlMessage(message);
    }
    
   
    
    
}