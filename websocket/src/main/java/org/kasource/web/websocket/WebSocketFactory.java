package org.kasource.web.websocket;

public interface WebSocketFactory {
    public WebSocket get(String socketName);
    
    public void listenTo(String socketName, WebSocketMessageListener listener);
    
    public void listenTo(String socketName, WebSocketEventListener listener) ;
}
