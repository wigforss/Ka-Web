package org.kasource.web.websocket;




/**
 * Listener for Web Socket messages.
 * 
 * @author rikardwi
 **/
public interface WebSocketMessageListener {
    
    /**
     * Called when a text message has been received from a client.
     * 
     * @param data      Message.
     * @param clientId  Id of the client who sent the message.
     **/
    public void onMessage(String message, String clientId);
    
    /**
     * Called when a binary message has been received from a client.
     * 
     * @param is        Binary message as an InputStream.
     * @param clientId  Id of the client who sent the message.
     **/
    public void onBinaryMessage(byte[] message, String clientId);
}
