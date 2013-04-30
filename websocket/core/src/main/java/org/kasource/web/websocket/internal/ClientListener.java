package org.kasource.web.websocket.internal;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.protocol.ProtocolHandler;

/**
 * Listens to client messages and events. Used for internal purposes only.
 * 
 * Implemented by the WebSocketChannel Implementation to allow the vendor-specific
 * web socket implementation to call a common handler for incomming events.
 * @author rikardwi
 **/
public interface ClientListener {
    
    /**
     * Called on client connect.
     * 
     * @param client The connecting client
     **/
    public void onConnect(WebSocketClient client);
    
    /**
     * Called on client disconnect.
     * 
     * @param client The disconnecting client
     **/
    public void onDisconnect(WebSocketClient client);
    
    /**
     * Called after authentication
     * 
     * @param username Name of the user which was authenticated.
     * @param error    null if authentication was successful, else the cause of the authentication fail.
     **/
    public void onAuthentication(String username, HttpServletRequest request, Throwable error);
    
    /**
     * Called when a text message has been received from a client.
     * 
     * @param data      Message.
     * @param clientId  Id of the client who sent the message.
     **/
    public void onMessage(WebSocketClient client, String message, ProtocolHandler<String> protocol);
    
    /**
     * Called when a binary message has been received from a client.
     * 
     * @param is        Binary message as an InputStream.
     * @param clientId  Id of the client who sent the message.
     **/
    public void onBinaryMessage(WebSocketClient client, byte[] message, ProtocolHandler<byte[]> protocol);
}
