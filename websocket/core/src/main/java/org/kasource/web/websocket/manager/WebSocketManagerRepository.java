package org.kasource.web.websocket.manager;

public interface WebSocketManagerRepository {
    public static final String ATTRIBUTE_PREFIX = WebSocketManager.class.getName() + "_";
        
    
    public WebSocketManager getWebSocketManager(String socketName);
}
