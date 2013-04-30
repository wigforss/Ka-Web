package org.kasource.web.websocket.event.extension.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.extension.BroadcastWebSocketBinaryMessageEvent;

/**
 * Listener Interface for BroadcastBinaryWebSocketMessageEvent.
 * 
 * @author rikardwi
 **/
public interface BroadcastBinaryWebSocketMessageListener extends EventListener {
    
    /**
     * Called when a message should be broadcasted.
     * 
     * @param event broadcast event.
     */
    public void onBroadcastBinaryWebSocketMessage(BroadcastWebSocketBinaryMessageEvent event);
}
