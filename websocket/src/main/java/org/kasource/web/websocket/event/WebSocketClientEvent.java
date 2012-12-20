package org.kasource.web.websocket.event;

import org.kasource.web.websocket.WebSocketManager;

public class WebSocketClientEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;

    private String clientId;
    
    public WebSocketClientEvent(WebSocketManager websocket, WebSocketEventType type, String clientId) {
        super(websocket, type);
        this.clientId = clientId;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }
    
    
}
