package org.kasource.web.websocket.channel;

import java.io.IOException;

import org.kasource.web.websocket.RecipientType;



/**
 * Web Socket Message Sender.
 * 
 * Allows messages to be sent to Web Socket clients.
 * 
 * @author rikardwi
 **/
public interface WebsocketMessageSender  {

    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     **/
    public void broadcast(String message);
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     **/
    public void broadcast(Object message);
    
    
    /**
     * Broadcasts a binary message to all clients.
     * 
     * @param message Message to send.
     **/
    public void broadcastBinary(byte[] message);
    
    public void broadcastBinary(Object message);
    
    /**
     * Sends a text message to a specific client.
     * 
     * @param message Message to send.
     * @param recipient ID of the recipient
     * @param recipientType Type of recipient
     * 
     * @throws IOException if message could not be sent.
     * @throws NoSuchWebSocketClient if there is no connected client with the supplied clientId.
     **/
    public void sendMessage(String message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient; 
    
    
    public void sendMessage(Object message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient;
    
    /**
     * Sends a binary message to a specific client.
     * 
     * @param message Message to send.
     * @param recipient ID of the recipient
     * @param recipientType Type of recipient
     * 
     * @throws IOException if message could not be sent.
     * @throws NoSuchWebSocketClient if there is no connected client with the supplied clientId.
     **/
    public void sendBinaryMessage(byte[] message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient; 
    
    public void sendBinaryMessage(Object message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient; 
   
    
}
