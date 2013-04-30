package org.kasource.web.websocket.event.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.WebSocketClientDisconnectedEvent;

/**
 * Listener interface for WebSocketClientDisconnectedEvent.
 * 
 * @author rikardwi
 **/
public interface WebSocketClientDisconnectedListener extends EventListener {
    
    /**
     * Called when a client has been disconnected.
     * 
     * @param event Disconnection event
     */
    public void onClientDisconnected(WebSocketClientDisconnectedEvent event);
}
