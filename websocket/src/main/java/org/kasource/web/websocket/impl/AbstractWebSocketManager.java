package org.kasource.web.websocket.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kasource.web.websocket.NoSuchWebSocketClient;
import org.kasource.web.websocket.WebSocketEventListener;
import org.kasource.web.websocket.WebSocketManager;
import org.kasource.web.websocket.WebSocketMessageListener;
import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;
import org.kasource.web.websocket.event.WebSocketClientEvent;
import org.kasource.web.websocket.event.WebSocketEventType;
import org.kasource.web.websocket.impl.WebSocketClient;

/**
 * Abstract WebSocketManager with support for sending and receiving messages to and from multiple clients.
 * 
 * @author rikardwi
 **/
public abstract class AbstractWebSocketManager implements WebSocketManager {

    private Map<String, WebSocketClient> clients = new ConcurrentHashMap<String, WebSocketClient>();
    private Set<WebSocketMessageListener> webSocketMessageListeners = new HashSet<WebSocketMessageListener>();
    private Set<WebSocketEventListener> webSocketEventListeners = new HashSet<WebSocketEventListener>();
    

    /**
     * Broadcast a text message to all clients.
     * 
     * @param message message to send.
     **/
    @Override
    public void broadcast(String message) {
        
        for (WebSocketClient client : clients.values()) {
            client.sendMessageToSocket(message);         
        }
        
    }

    /**
     * Broadcast a binary message to all clients.
     * 
     * @param message message to send.
     **/
    @Override
    public void broadcastBinary(byte[] message) {
        for (WebSocketClient client : clients.values()) {
             client.sendMessageToSocket(message);
        }
        
    }

    /**
     * Sends a text message to a specific client
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    @Override
    public void sendMessage(String clientId, String message) throws IOException, NoSuchWebSocketClient {
       
        WebSocketClient client = clients.get(clientId);
        if(client == null) {
            throw new NoSuchWebSocketClient("No client found with ID " + clientId);
        }
        client.sendMessageToSocket(message);
        
    }

    /**
     * Sends a binary message to a specific client
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    @Override
    public void sendBinaryMessage(String clientId, byte[] message) throws IOException, NoSuchWebSocketClient {
        WebSocketClient client = clients.get(clientId);
        if(client == null) {
            throw new NoSuchWebSocketClient("No client found with ID " + clientId);
        }
        client.sendMessageToSocket(message);
        
    }
    
    /**
     * Add message listener.
     * 
     * @param webSocketMessageListener the webSocketMessageListener to add
     */
    public void addWebSocketMessageListener(WebSocketMessageListener webSocketMessageListener) {
        this.webSocketMessageListeners.add(webSocketMessageListener);
    }

    /**
     * Add event listener.
     * 
     * @param webSocketEventListener the webSocketEventListener to add
     */
    public void addWebSocketEventListener(WebSocketEventListener webSocketEventListener) {
        this.webSocketEventListeners.add(webSocketEventListener);
    }
    
   

   

    /**
     * Add the supplied client to the register and notifies listeners about this new client.
     * 
     * 
     * @param id                   ID of the client to add.
     * @param client               Client to add.
     * @param connectionParameters The connection parameters.
     **/
    public void registerClient(String id, WebSocketClient client, Map<String, String[]> connectionParameters) {
        clients.put(id, client);
        if (!webSocketEventListeners.isEmpty()) {
            sendEvent(new WebSocketClientConnectionEvent(this, WebSocketEventType.CLIENT_CONNECTED, id, connectionParameters));
        }
    }
    
   
    /**
     * Remove client with id from the register and notifies listeners about the removed client.
     * 
     * @param id ID of the client to remove.
     */
    public void unregisterClient(String id) {
        clients.remove(id);
        if (!webSocketEventListeners.isEmpty()) {
            sendEvent(new WebSocketClientEvent(this, WebSocketEventType.CLIENT_DISCONNECTED, id));
        }
    }
   
    /**
     * Notifies listeners about a new event.
     * 
     * @param event Event to propagate to listeners.
     **/
    private void sendEvent(WebSocketClientEvent event) {
        for(WebSocketEventListener listener: webSocketEventListeners) {
            listener.onWebSocketEvent(event);
        }
    }
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    public void onWebSocketMessage(String message, String id) {
        if(!webSocketMessageListeners.isEmpty()) {
            for(WebSocketMessageListener listener: webSocketMessageListeners) {
                listener.onMessage(message, id);
            }  
        }
    }
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    public void onWebSocketMessage(byte[] message, String id) {
        if(!webSocketMessageListeners.isEmpty()) {
            for(WebSocketMessageListener listener: webSocketMessageListeners) {
                listener.onBinaryMessage(message, id);
            }
        }
    }
    
    
    
    /**
     * Returns true if this manager has a client registered with the supplied id.
     * @param id ID of the client to look for.
     * 
     * @return true if this manager has a client registered with the supplied id, else false.
     **/
     public boolean hasClient(String id) {
        return clients.containsKey(id);
    }

}
