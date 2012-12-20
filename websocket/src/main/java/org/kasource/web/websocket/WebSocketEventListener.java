package org.kasource.web.websocket;

import org.kasource.web.websocket.event.WebSocketEvent;

public interface WebSocketEventListener {
    
    public void onWebSocketEvent(WebSocketEvent event);
    
}
