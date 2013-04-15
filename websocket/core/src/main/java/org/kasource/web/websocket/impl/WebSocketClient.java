package org.kasource.web.websocket.impl;

import org.kasource.web.websocket.protocol.ProtocolHandler;

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
    
    /**
     * @param textProtocol the textProtocol to set
     */
    public void setTextProtocol(ProtocolHandler<String> textProtocol);




    /**
     * @param binaryProtocol the binaryProtocol to set
     */
    public void setBinaryProtocol(ProtocolHandler<byte[]> binaryProtocol);
}
