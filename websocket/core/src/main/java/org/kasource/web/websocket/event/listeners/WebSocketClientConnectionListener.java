package org.kasource.web.websocket.event.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.WebSocketClientConnectionEvent;

/**
 * Listener interface for WebSocketClientConnectionEventEvent.
 * 
 * @author rikardwi
 **/
public interface WebSocketClientConnectionListener extends EventListener {
    
    /**
     * Called when a client has connected.
     * 
     * @param event Connection event
     **/
    public void onClientConnection(WebSocketClientConnectionEvent event);
}
