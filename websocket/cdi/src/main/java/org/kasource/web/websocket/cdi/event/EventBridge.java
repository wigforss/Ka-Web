package org.kasource.web.websocket.cdi.event;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.extension.BroadcastObjectAsWebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.extension.BroadcastWebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.extension.BroadcastObjectAsWebSocketTextMessageEvent;
import org.kasource.web.websocket.event.extension.BroadcastWebSocketTextMessageEvent;
import org.kasource.web.websocket.event.extension.SendWebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.extension.SendObjectAsWebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.extension.SendWebSocketTextMessageEvent;
import org.kasource.web.websocket.event.extension.SendObjectAsWebSocketTextMessageEvent;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event bridge, that bridges web socket events to and from the CDI Event System.
 * 
 * This class listens to a ServletContext to be published as an event. The application bootstrap code
 * needs to publish the ServletContext as an event for this class to be properly initialized.
 * 
 * @author rikardwi
 **/
@Startup
@ApplicationScoped
public class EventBridge implements WebSocketEventListener, WebSocketListenerRegistration {
    private static final Logger LOG = LoggerFactory.getLogger(EventBridge.class);
    
    @Inject @Any 
    private Event<WebSocketEvent> socketEvent;
    
  
    
    @Inject @Configured
    private AnnotationWebsocketBinding mapping;
    
    
    /**
     * Invoked when a initialized ServletContext has been published.
     * 
     * @param servletContext ServletContext to use.
     **/
    public void onServletContextInitialized(@Observes ServletContext servletContext) {
        WebSocketListenerRegister listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        listenerRegister.registerListener(this);
    }
    
    /**
     * Invoked when a SendWebSocketTextMessageEvent is published. The message from 
     * the event will be sent to the correct websocket client.
     *  
     * @param event Event to send to websocket
     **/
    public void onSendTextMessage(@Observes SendWebSocketTextMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        try {
            webSocket.sendMessage(event.getMessage(), event.getRecipient(), event.getRecipientType());
        } catch (Exception e) {
           LOG.warn("Could not send message: " +  event.getMessage() + " to " +event.getRecipientType() + ": " + event.getRecipient());
        } 
    }
    
    /**
     * Invoked when a SendWebSocketTextMessageEvent is published. The message from 
     * the event will be sent to the correct websocket client.
     *  
     * @param event Event to send to websocket
     **/
    public void onSendTextMessage(@Observes SendObjectAsWebSocketTextMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        try {
            webSocket.sendMessage(event.getMessage(), event.getProtocolHandler(), event.getRecipient(), event.getRecipientType());
        } catch (Exception e) {
           LOG.warn("Could not send message: " +  event.getMessage() + " to " +event.getRecipientType() + ": " + event.getRecipient());
        } 
    }
    
    /**
     * Invoked when a SendWebSocketBinaryMessageEvent is published. The message from 
     * the event will be sent to the correct websocket client.
     *  
     * @param event Event to send to websocket
     **/
    public void onSendBinaryMessage(@Observes SendWebSocketBinaryMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        try {
            webSocket.sendBinaryMessage(event.getMessage(), event.getRecipient(), event.getRecipientType());
        } catch (Exception e) {
           LOG.warn("Could not send message: " +  event.getMessage() + " to " +event.getRecipientType() + ": " + event.getRecipient());
        } 
    }
    
    /**
     * Invoked when a SendWebSocketBinaryMessageEvent is published. The message from 
     * the event will be sent to the correct websocket client.
     *  
     * @param event Event to send to websocket
     **/
    public void onSendBinaryProtocolMessage(@Observes SendObjectAsWebSocketBinaryMessageEvent event) throws IOException {
        WebSocketChannel webSocket = event.getSource();
       try {
            webSocket.sendBinaryMessage(event.getMessage(), event.getProtocolHandler(), event.getRecipient(), event.getRecipientType());
        } catch (Exception e) {
           LOG.warn("Could not send message: " +  event.getMessage() + " to " +event.getRecipientType() + ": " + event.getRecipient());
        } 
    }
    
    /**
     * Invoked when a BroadcastTextWebSocketMessageEvent is published. 
     * Will publish the message from the event to all connected websocket clients.
     * 
     * @param event Event to broadcast to all websocket clients
     **/
    public void onBroadcastTextMessage(@Observes BroadcastWebSocketTextMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        webSocket.broadcast(event.getMessage());
    }
    
    /**
     * Invoked when a BroadcastTextWebSocketMessageEvent is published. 
     * Will publish the message from the event to all connected websocket clients.
     * 
     * @param event Event to broadcast to all websocket clients
     **/
    public void onBroadcastTextProtocolMessage(@Observes BroadcastObjectAsWebSocketTextMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        webSocket.broadcastObject(event.getMessage(), event.getProtocolHandler());
    }
    
    /**
     * Invoked when a BroadcastBinaryWebSocketMessageEvent is published. 
     * Will publish the message from the event to all connected websocket clients.
     * 
     * @param event Event to broadcast to all websocket clients
     **/
    public void onBroadcastBinaryMessage(@Observes BroadcastWebSocketBinaryMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        webSocket.broadcastBinary(event.getMessage());
    }
    
    /**
     * Invoked when a BroadcastBinaryWebSocketMessageEvent is published. 
     * Will publish the message from the event to all connected websocket clients.
     * 
     * @param event Event to broadcast to all websocket clients
     **/
    public void onBroadcastBinaryProtocolMessage(@Observes BroadcastObjectAsWebSocketBinaryMessageEvent event) {
        WebSocketChannel webSocket = event.getSource();
        webSocket.broadcastBinaryObject(event.getMessage(), event.getProtocolHandler());
    }
    
    /**
     * Handles events from websockets. 
     * All incoming events from websockets is published onto the CDI event system.
     * 
     * @param event Incoming websocket event.
     **/
    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
       
       Annotation annotation = mapping.getAnnotationForSocket(event.getSource().getUrl()); 
        
       if(event instanceof WebSocketTextMessageEvent) {
           if (annotation == null) {
               socketEvent.select(WebSocketTextMessageEvent.class).fire((WebSocketTextMessageEvent) event);
           } else {
               socketEvent.select(WebSocketTextMessageEvent.class, annotation).fire((WebSocketTextMessageEvent) event);
           }
       } else if(event instanceof WebSocketBinaryMessageEvent) {
           if (annotation == null) {
               socketEvent.select(WebSocketBinaryMessageEvent.class).fire((WebSocketBinaryMessageEvent) event);
           } else {
               socketEvent.select(WebSocketBinaryMessageEvent.class, annotation).fire((WebSocketBinaryMessageEvent) event);
           }
       } else if(event instanceof WebSocketClientConnectionEvent) {
           if (annotation == null) {
               socketEvent.select(WebSocketClientConnectionEvent.class).fire((WebSocketClientConnectionEvent) event);
           } else {
               socketEvent.select(WebSocketClientConnectionEvent.class, annotation).fire((WebSocketClientConnectionEvent) event);
           }
       } else if(event instanceof WebSocketClientDisconnectedEvent) {
           if (annotation == null) {
               socketEvent.select(WebSocketClientDisconnectedEvent.class).fire((WebSocketClientDisconnectedEvent) event);
           } else {
               socketEvent.select(WebSocketClientDisconnectedEvent.class, annotation).fire((WebSocketClientDisconnectedEvent) event);
           }
       } else {
           if (annotation == null) {
               socketEvent.fire(event);
           } else {
               socketEvent.select(annotation).fire(event);
           }
       }
       
    
    }

    /**
     * Returns the name of what websocket name to handle.
     * 
     * This class should handle all websockets, therefore * is returned.
     * 
     * @return * for all websockets.
     **/
    @Override
    public String getWebSocketChannelName() {
        return "*";
    }
    
    
}
