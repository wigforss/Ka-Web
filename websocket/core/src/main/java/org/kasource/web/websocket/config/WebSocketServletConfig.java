package org.kasource.web.websocket.config;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;

public interface WebSocketServletConfig {
    public boolean isDynamicAddressing();
    
    public ClientIdGenerator getClientIdGenerator();
    
    public WebSocketManager getWebSocketManager(String url);
    
    public boolean hasProtocol(String protocol, String url);
    
    public boolean isValidOrigin(String origin);
}
