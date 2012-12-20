package org.kasource.web.websocket;

import java.io.IOException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



import org.kasource.web.websocket.event.WebSocketEvent;

public class WebSocketImpl implements WebSocket, WebSocketMessageListener, WebSocketEventListener {
    
    private WebsocketMessageSender sender;
    private Set<WebSocketMessageListener> messageListeners = new HashSet<WebSocketMessageListener>(); 
    private Set<WebSocketEventListener> eventListeners = new HashSet<WebSocketEventListener>(); 
        
    /**
     * Constructor used for lazy initialization. 
     * 
     * Its expected that the initialize will be invoked before this
     * instance is used, otherwise an Exception will be thrown.
     **/
    public WebSocketImpl() {
        
    }
    
    /**
     * Constructor used for immediate initialization.
     * 
     * @param sender WebSocket manager used to send messages with.
     **/
    public WebSocketImpl(WebSocketManager sender) { 
        initialize(sender);
    }
    
   
    
    /**
     * Initialize this WebSocket.
     * 
     * @param manager WebSocketManager to use.
     */
    public void initialize(WebSocketManager manager) {
        this.sender = manager;
        manager.addWebSocketMessageListener(this);
        manager.addWebSocketEventListener(this);
    }
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     **/
    public void broadcast(String message) {
        if(sender != null) {
            sender.broadcast(message);
        }
        
    }

    /**
     * @param message
     * @see org.kasource.spring.websocket.WebsocketMessageSender#broadcastBinary(byte[])
     */
    public void broadcastBinary(byte[] message) {
        if(sender != null) {
            sender.broadcastBinary(message);
        }
        
    }

    /**
     * @param clientId
     * @param message
     * @throws IOException
     * @see org.kasource.spring.websocket.WebsocketMessageSender#sendMessage(java.lang.String, java.lang.String)
     */
    public void sendMessage(String clientId, String message) throws IOException {
        if(sender != null) {
            sender.sendMessage(clientId, message);
        }
        
    }

    /**
     * @param clientId
     * @param message
     * @throws IOException
     * @see org.kasource.spring.websocket.WebsocketMessageSender#sendBinaryMessage(java.lang.String, byte[])
     */
    public void sendBinaryMessage(String clientId, byte[] message) throws IOException {
        if(sender != null) {
            sender.sendBinaryMessage(clientId, message);
        }
        
    }

    @Override
    public void onMessage(String data, String clientId) {
        Iterator<WebSocketMessageListener> iter = messageListeners.iterator();
        while (iter.hasNext()) {
            WebSocketMessageListener listener = iter.next();
            listener.onMessage(data, clientId);
        }
    }

    @Override
    public void onBinaryMessage(byte[] data, String clientId) {
        Iterator<WebSocketMessageListener> iter = messageListeners.iterator();
        while (iter.hasNext()) {
            WebSocketMessageListener listener = iter.next();
            listener.onBinaryMessage(data, clientId);
        }
        
    }
    
 

    @Override
    public void addMessageListener(WebSocketMessageListener listener) {
        messageListeners.add(listener);      
    }
    
    @Override
    public void addEventListener(WebSocketEventListener listener) {
        eventListeners.add(listener);      
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        for( WebSocketEventListener listener : eventListeners) {
            listener.onWebSocketEvent(event);
        } 
    }
    
   
}
