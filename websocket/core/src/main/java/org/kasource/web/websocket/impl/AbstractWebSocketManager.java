package org.kasource.web.websocket.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.internal.ClientListener;

import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.protocol.ProtocolHandler;

/**
 * Abstract WebSocketManager with support for sending and receiving messages to and from multiple clients.
 * 
 * @author rikardwi
 **/
public abstract class AbstractWebSocketManager implements WebSocketManager {

    private Map<String, WebSocketClient> clients = new ConcurrentHashMap<String, WebSocketClient>();
   
    private Set<ClientListener> webSocketClientListeners = new HashSet<ClientListener>();
    

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
     * Add event listener.
     * 
     * @param webSocketEventListener the webSocketEventListener to add
     */
    public void addClientListener(ClientListener listener) {
        this.webSocketClientListeners.add(listener);
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
        if (!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onConnect(id, connectionParameters);
            }
        }
    }
    
   
    /**
     * Remove client with id from the register and notifies listeners about the removed client.
     * 
     * @param id ID of the client to remove.
     */
    public void unregisterClient(String id) {
        clients.remove(id);
        if (!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onDisconnect(id);
            }
        }
    }
   
  
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    public void onWebSocketMessage(String message, ProtocolHandler<String> protocol, String id) {
        if(!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onMessage(message, protocol, id);
            }  
        }
    }
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    public void onWebSocketMessage(byte[] message, ProtocolHandler<byte[]> protocol, String id) {
        if(!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onBinaryMessage(message, protocol, id);
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
