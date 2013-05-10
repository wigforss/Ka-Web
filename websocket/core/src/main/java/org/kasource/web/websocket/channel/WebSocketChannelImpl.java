package org.kasource.web.websocket.channel;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.event.WebSocketAuthenticationFailedEvent;
import org.kasource.web.websocket.event.WebSocketAuthenticationSuccessEvent;
import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketBinaryObjectMessageEvent;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.WebSocketTextObjectMessageEvent;
import org.kasource.web.websocket.internal.ClientListener;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.kasource.web.websocket.protocol.XmlProtocolHandler;
/**
 * Web Socket Channel Implementation.
 * 
 * @author rikardwi
 **/
public class WebSocketChannelImpl implements WebSocketChannel, ClientListener {
    private String url;
    private WebsocketMessageSender sender;
    private TextProtocolHandler json = new JsonProtocolHandler();
    private TextProtocolHandler jaxb = new XmlProtocolHandler();
    private Set<WebSocketEventListener> eventListeners = new HashSet<WebSocketEventListener>(); 
        
    /**
     * Constructor used for lazy initialization. 
     * 
     * Its expected that the initialize will be invoked before this
     * instance is used, otherwise an Exception will be thrown.
     **/
    public WebSocketChannelImpl() {
        
    }
    
    /**
     * Constructor used for immediate initialization.
     * 
     * @param sender WebSocket manager used to send messages with.
     **/
    public WebSocketChannelImpl(String url, WebSocketManager sender) { 
        initialize(url, sender);
    }
    
   
    
    /**
     * Initialize this WebSocket.
     * 
     * @param manager WebSocketManager to use.
     */
    public void initialize(String url, WebSocketManager manager) {
        this.url = url;
        this.sender = manager;
        manager.addClientListener(this);
    }
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     **/
    @Override
    public void broadcast(String message) {
        if(sender != null) {
            sender.broadcast(message);
        }
        
    }
    
    @Override
    public void broadcastJsonMessage(Object message) {
        broadcast(json.toMessage(message));     
    }
    
    @Override
    public void broadcastXmlMessage(Object message) {
        broadcast(jaxb.toMessage(message));     
    }
    
    @Override
    public void broadcastObject(Object message, ProtocolHandler<String> protocolHandler) {
        broadcast(protocolHandler.toMessage(message));     
    }

    /**
     * @param message
     * @see org.kasource.web.websocket.channel.spring.websocket.WebsocketMessageSender#broadcastBinary(byte[])
     */
    @Override
    public void broadcastBinary(byte[] message) {
        if(sender != null) {
            sender.broadcastBinary(message);
        }
        
    }
    
    @Override
    public void broadcastBinaryObject(Object message, ProtocolHandler<byte[]> protocolHandler) {
        broadcastBinary(protocolHandler.toMessage(message));
    }

    /**
     * Send a message to the web socket client with clientId.
     * 
     * @param clientId  ID of the client to send message to.
     * @param message   Message to send.
     * 
     * @throws IOException If the message could not be sent.
     * @see org.kasource.web.websocket.channel.spring.websocket.WebsocketMessageSender#sendMessage(java.lang.String, java.lang.String)
     */
    @Override
    public void sendMessage(String message, String recipient, RecipientType recipientType) throws IOException {
        if(sender != null) {
            sender.sendMessage(message, recipient, recipientType);
        }
        
    }
    
    @Override
    public void sendMessageAsJson(Object message, String recipient, RecipientType recipientType) throws IOException {
        sendMessage(json.toMessage(message), recipient, recipientType);
    }
        
    
    @Override
    public void sendMessageAsXml(Object message, String recipient, RecipientType recipientType) throws IOException {
        sendMessage(jaxb.toMessage(message), recipient, recipientType);
    }
    
    @Override
    public void sendMessage(Object message, ProtocolHandler<String> protocolHandler, String recipient, RecipientType recipientType) throws IOException {
        if(sender != null) {
            sender.sendMessage(protocolHandler.toMessage(message), recipient, recipientType);
        }
        
    }
    
 

    /**
     * Send a binary message to a specific client.
     * 
     * @param clientId ID of the client recipient.
     * @param message  Message to send.
     * 
     * @throws IOException If the message could not be sent.
     */
    @Override
    public void sendBinaryMessage(byte[] message, String recipient, RecipientType recipientType) throws IOException {
        if(sender != null) {
            sender.sendBinaryMessage(message, recipient, recipientType);
        }
        
    }
    
    @Override
    public void sendBinaryMessage(Object message, ProtocolHandler<byte[]> protocolHandler, String recipient, RecipientType recipientType) throws IOException {
        if(sender != null) {
            sender.sendBinaryMessage(protocolHandler.toMessage(message), recipient, recipientType);
        }
        
    }
    
    


    /**
     * Handle in-bound text messages.
     * 
     * @param message Message received from web socket.
     **/
    @Override
    public void onMessage(WebSocketClient client, String message, ProtocolHandler<String> textProtocol) {
        if(textProtocol == null) {
            fireEvent(new WebSocketTextMessageEvent(this, message, client));
        } else {
            fireEvent(new WebSocketTextObjectMessageEvent(this, message, client, textProtocol));
        }
       
    }

    /**
     * Handle in-bound binary messages.
     * 
     * @param message Message received from web socket.
     **/
    @Override
    public void onBinaryMessage(WebSocketClient client, byte[] message, ProtocolHandler<byte[]> binaryProtocol) {
        if(binaryProtocol == null) {
            fireEvent(new WebSocketBinaryMessageEvent(this, message, client));
        } else {
            fireEvent(new WebSocketBinaryObjectMessageEvent(this, message, client, binaryProtocol));
        }
    }
    
    
    /**
     * Add a listener to this channel.
     * 
     * @param listener The listener to add.
     **/
    @Override
    public void addListener(WebSocketEventListener listener) {
        eventListeners.add(listener);      
    }

    

    /**
     * Returns the name of this web socket channel.
     * 
     * @return name of this web socket channel.
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Handle in-bound connection event.
     * 
     * @param clientId             ID of the client that has connected.
     * @param connectionParameters Connection Properties on the client connection request.
     **/
    @Override
    public void onConnect(WebSocketClient client) {
        fireEvent(new WebSocketClientConnectionEvent(this, client));
        
    }

    /**
     * Handle in-bound disconnect event.
     * 
     * @param clientId ID of the client that was disconnected.
     **/
    @Override
    public void onDisconnect(WebSocketClient client) {
        fireEvent(new WebSocketClientDisconnectedEvent(this, client));
        
    }
    
    /**
     * Fires events to all listeners.
     * 
     * @param event Event to emit.
     **/
    private void fireEvent(WebSocketEvent event) {
        for(WebSocketEventListener listener : eventListeners) {
            listener.onWebSocketEvent(event);
        }
    }

    @Override
    public void onAuthentication(String username, HttpServletRequest request, Throwable error) {
       if(error == null) {
           fireEvent(new WebSocketAuthenticationSuccessEvent(this, username, request));
       } else {
           fireEvent(new WebSocketAuthenticationFailedEvent(this, username, request, error));
       }
        
    }

    @Override
    public void broadcast(Object message) {
        if(sender != null) {
            sender.broadcast(message);
        }
        
    }

    @Override
    public void broadcastBinary(Object message) {
        if(sender != null) {
            sender.broadcastBinary(message);
        }
        
    }

    @Override
    public void sendMessage(Object message, String recipient, RecipientType recipientType) throws IOException,
                NoSuchWebSocketClient {
        if(sender != null) {
            sender.sendMessage(message, recipient, recipientType);
        }
        
    }

    @Override
    public void sendBinaryMessage(Object message, String recipient, RecipientType recipientType) throws IOException,
                NoSuchWebSocketClient {
        if(sender != null) {
            sender.sendBinaryMessage(message, recipient, recipientType);
        }
        
    }

    
}
