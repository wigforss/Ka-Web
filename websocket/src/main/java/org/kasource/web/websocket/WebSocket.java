package org.kasource.web.websocket;


/**
 * WebSocket.
 * 
 * Exposes methods that can be invoked on an underlying Web Socket Servlet.
 * 
 * @author rikardwi
 **/
public interface WebSocket extends WebsocketMessageSender {
    
    /**
     * Adds a message listener.
     * 
     * @param listener Message Listener to add.
     **/
    public void addMessageListener(WebSocketMessageListener listener);
    
    /**
     * Adds an event listener.
     * @param listener Event listener to add.
     **/
    public void addEventListener(WebSocketEventListener listener);
}
