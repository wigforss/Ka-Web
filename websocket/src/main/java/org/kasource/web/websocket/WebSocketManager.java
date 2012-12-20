package org.kasource.web.websocket;

/**
 * Web Socket Manager.
 * 
 * This interface is implemented by vendor specific WebSocketServlet implementations.
 * 
 * @author rikardwi
 **/
public interface WebSocketManager extends WebsocketMessageSender {
    
    public void addWebSocketMessageListener(WebSocketMessageListener webSocketMessageListener);
    
    public void addWebSocketEventListener(WebSocketEventListener webSocketEventListener) ;
    
}
