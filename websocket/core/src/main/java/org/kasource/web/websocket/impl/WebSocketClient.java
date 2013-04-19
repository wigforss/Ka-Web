package org.kasource.web.websocket.impl;

import java.util.Map;

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
    
    /**
     * @return the username
     */
    public String getUsername();



    /**
     * @return the connectionParameters
     */
    public Map<String, String[]> getConnectionParameters();



    /**
     * @return the id
     */
    public String getId();
}