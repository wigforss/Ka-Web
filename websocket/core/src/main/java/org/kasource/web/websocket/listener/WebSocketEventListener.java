package org.kasource.web.websocket.listener;

import java.util.EventListener;

import org.kasource.web.websocket.event.WebSocketEvent;

public interface WebSocketEventListener extends EventListener {
    
    public void onWebSocketEvent(WebSocketEvent event);
    
}
