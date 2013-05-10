package org.kasource.web.websocket.manager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.channel.NoSuchWebSocketClient;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.internal.ClientListener;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.security.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract WebSocketManager with support for sending and receiving messages to and from multiple clients.
 * 
 * @author rikardwi
 **/
public class WebSocketManagerImpl implements WebSocketManager {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketManagerImpl.class);
    
    private Map<String, WebSocketClient> clients = new ConcurrentHashMap<String, WebSocketClient>();
    private Map<String, Set<WebSocketClient>> clientsByUser = new ConcurrentHashMap<String, Set<WebSocketClient>>();

    private Set<ClientListener> webSocketClientListeners = new HashSet<ClientListener>();
 
    private AuthenticationProvider authenticationProvider;
    private ProtocolHandlerRepository protocolHandlerRepository;
    
    
    /**
     * Broadcast a text message to all clients.
     * 
     * @param message message to send.
     **/
    @Override
    public void broadcast(String message) {
        
        for (WebSocketClient client : clients.values()) {
            try {
                client.sendMessageToSocket(message);   
            } catch (Exception e) {
                LOG.debug("Could not broadcast to " + client.getUsername() + " with id " + client.getId(), e);
            }
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
            try {
             client.sendMessageToSocket(message);
            } catch (Exception e) {
                LOG.debug("Could not broadcast to " + client.getUsername() + " with id " + client.getId(), e);
            }
        }
        
    }

    /**
     * Sends a text message to a specific client
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    @Override
    public void sendMessage(String message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient {
       if(recipientType == RecipientType.USERNAME) {
           sendMessageToUser(recipient, message);
       } else {
        WebSocketClient client = clients.get(recipient);
        if(client == null) {
            throw new NoSuchWebSocketClient("No client found with ID " + recipient);
        }
        client.sendMessageToSocket(message);
       }
    }
    
    /**
     * Sends a text message to a specific user
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    private void sendMessageToUser(String username, String message) throws IOException, NoSuchWebSocketClient {      
        Set<WebSocketClient> clientsForUser = clientsByUser.get(username);
        if(clientsForUser == null || clientsForUser.isEmpty()) {
            throw new NoSuchWebSocketClient("No client found for user " + username);
        }
        for(WebSocketClient client : clientsForUser) {
            client.sendMessageToSocket(message);
        }
        
        
    }

    /**
     * Sends a binary message to a specific client
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    @Override
    public void sendBinaryMessage(byte[] message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient {
        if(recipientType == RecipientType.USERNAME) {
            sendBinaryMessageToUser(recipient, message);
        } else {
            WebSocketClient client = clients.get(recipient);
            if(client == null) {
                throw new NoSuchWebSocketClient("No client found with ID " + recipient);
            }
            client.sendMessageToSocket(message);
        }
    }
    
    /**
     * Sends a binary message to a specific client
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    private void sendBinaryMessageToUser(String username, byte[] message) throws IOException, NoSuchWebSocketClient {
        Set<WebSocketClient> clientsForUser = clientsByUser.get(username);
        if(clientsForUser == null || clientsForUser.isEmpty()) {
            throw new NoSuchWebSocketClient("No client found for user " + username);
        }
        for(WebSocketClient client : clientsForUser) {
            client.sendMessageToSocket(message);
        }
        
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
    @Override
    public void registerClient(WebSocketClient client) {
        ProtocolHandler<String> textProtocolHandler = protocolHandlerRepository.getTextProtocol(client.getSubProtocol(), client.getUrl(), true);
        client.setTextProtocolHandler(textProtocolHandler);
        ProtocolHandler<byte[]> binaryProtocolHandler = protocolHandlerRepository.getBinaryProtocol(client.getSubProtocol(), client.getUrl(), true);
        client.setBinaryProtocolHandler(binaryProtocolHandler);
        clients.put(client.getId(), client);
        addClientForUser(client);
        if (!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onConnect(client);
            }
        }
    }
    
    
    private void addClientForUser(WebSocketClient client) {
        if(client.getUsername() != null) {
            Set<WebSocketClient> clientsForUser = clientsByUser.get(client.getUsername());
            if(clientsForUser == null) {
                clientsForUser = new HashSet<WebSocketClient>();
                clientsByUser.put(client.getUsername(), clientsForUser);
            }
            clientsForUser.add(client);
        }
    }
    
    @Override
    public String authenticate(HttpServletRequest request) throws AuthenticationException {
        if(authenticationProvider != null) {
            try {
                String username = authenticationProvider.authenticate(request);
                fireAuthentication(username, request, null);
                return username;
            } catch(AuthenticationException ae) {
                LOG.warn("Unauthorized access by " + ae.getUsername() + " : " +ae.getMessage());
                fireAuthentication(ae.getUsername(), request, ae);
                throw ae;
            }    
            
        }
        return null;
       
    }
    
    private void fireAuthentication(String username, HttpServletRequest request, Throwable error) {
        if(!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onAuthentication(username, request, error);
            }
        }
    }
   
    /**
     * Remove client with id from the register and notifies listeners about the removed client.
     * 
     * @param id ID of the client to remove.
     */
    public void unregisterClient(WebSocketClient client) {
        clients.remove(client.getId());
        removeClientForUser(client);
        if (!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onDisconnect(client);
            }
        }
    }

    private void removeClientForUser(WebSocketClient client) {
        if(client.getUsername() != null) {
            Set<WebSocketClient> clientsForUser = clientsByUser.get(client.getUsername());
            if(clientsForUser != null) {
                clientsForUser.remove(client);
            }
        }
    }
   
  
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    @Override
    public void onWebSocketMessage(WebSocketClient client, String message) {
       
        if(!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onMessage(client, message, client.getTextProtocolHandler());
            }  
        }
    }
    
    /**
     * Notifies listeners on new message from a client.
     * 
     * @param message   Message from client
     * @param id        ID of the client
     */
    @Override
    public void onWebSocketMessage(WebSocketClient client, byte[] message) {
      
       
        if(!webSocketClientListeners.isEmpty()) {
            for(ClientListener listener: webSocketClientListeners) {
                listener.onBinaryMessage(client, message, client.getBinaryProtocolHandler());
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

    /**
     * @param authenticationProvider the authenticationProvider to set
     */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public ProtocolHandlerRepository getProtocolHandlerRepository() {
        return protocolHandlerRepository;
    }

    /**
     * @param protocolHandlerRepository the protocolHandlerRepository to set
     */
    public void setProtocolHandlerRepository(ProtocolHandlerRepository protocolHandlerRepository) {
        this.protocolHandlerRepository = protocolHandlerRepository;
    }

    @Override
    public void broadcast(Object message) {
        for (WebSocketClient client : clients.values()) {
            try {
                client.sendTextMessageToSocket(message);
            } catch (Exception e) {
                LOG.debug("Could not broadcast to " + client.getUsername() + " with id " + client.getId(), e);
            }
        }
        
    }

    @Override
    public void broadcastBinary(Object message) {
        for (WebSocketClient client : clients.values()) {
            try {
                client.sendBinaryMessageToSocket(message);
            } catch (Exception e) {
                LOG.debug("Could not broadcast to " + client.getUsername() + " with id " + client.getId(), e);
            }
       }
        
    }

    @Override
    public void sendMessage(Object message, String recipient, RecipientType recipientType) throws IOException,
                NoSuchWebSocketClient {
        if(recipientType == RecipientType.USERNAME) {
            sendMessageAsObjectToUser(recipient, message);
        } else {
         WebSocketClient client = clients.get(recipient);
         if(client == null) {
             throw new NoSuchWebSocketClient("No client found with ID " + recipient);
         }
         client.sendTextMessageToSocket(message);         
        }
       
        
    }

    
    /**
     * Sends a text message to a specific user
     * 
     * @param clientId ID of the recipient
     * @param message to send.
     **/
    private void sendMessageAsObjectToUser(String username, Object message) throws IOException, NoSuchWebSocketClient {      
        Set<WebSocketClient> clientsForUser = clientsByUser.get(username);
        if(clientsForUser == null || clientsForUser.isEmpty()) {
            throw new NoSuchWebSocketClient("No client found for user " + username);
        }
        for(WebSocketClient client : clientsForUser) {
            client.sendTextMessageToSocket(message);
        }
        
        
    }

    
    @Override
    public void sendBinaryMessage(Object message, String recipient, RecipientType recipientType) throws IOException,
                NoSuchWebSocketClient {
        if(recipientType == RecipientType.USERNAME) {
            sendBinaryAsObjectMessageToUser(recipient, message);
        } else {
            WebSocketClient client = clients.get(recipient);
            if(client == null) {
                throw new NoSuchWebSocketClient("No client found with ID " + recipient);
            }
            client.sendBinaryMessageToSocket(message);         
        }
        
    }
     
    private void sendBinaryAsObjectMessageToUser(String username, Object message) throws IOException, NoSuchWebSocketClient {
        Set<WebSocketClient> clientsForUser = clientsByUser.get(username);
        if(clientsForUser == null || clientsForUser.isEmpty()) {
            throw new NoSuchWebSocketClient("No client found for user " + username);
        }
        for(WebSocketClient client : clientsForUser) {
            client.sendBinaryMessageToSocket(message);
        }
        
    }
   

}
