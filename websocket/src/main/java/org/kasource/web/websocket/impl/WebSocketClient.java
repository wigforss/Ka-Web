package org.kasource.web.websocket.impl;

/**
 * WebSocketClient that one can send messages to.
 * 
 * @author rikardwi
 **/
public interface WebSocketClient {
   
    /**
     * Sends a text message
     * 
     * @param message message to send.
     **/
    public void sendMessageToSocket(String message);
    
    /**
     * Sends a binary message
     * 
     * @param message message to send.
     **/
    public void sendMessageToSocket(byte[] message);
}
