package org.kasource.web.websocket.event;

import java.util.Map;

import org.kasource.web.websocket.WebSocketManager;

public class WebSocketClientConnectionEvent extends WebSocketClientEvent {
    private static final long serialVersionUID = 1L;
    private Map<String, String[]> connectionParameters;
    
    public WebSocketClientConnectionEvent(WebSocketManager websocket, WebSocketEventType type, String clientId, Map<String, String[]> connectionParameters) {
        super(websocket, type, clientId);
        this.connectionParameters = connectionParameters;
    }

    /**
     * @return the connectionParameters
     */
    public Map<String, String[]> getConnectionParameters() {
        return connectionParameters;
    }

    
   

}
