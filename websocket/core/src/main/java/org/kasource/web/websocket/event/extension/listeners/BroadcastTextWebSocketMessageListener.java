package org.kasource.web.websocket.event.extension.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.extension.BroadcastWebSocketTextMessageEvent;

/**
 * Listener Interface for BroadcastTextWebSocketMessageEvent.
 * 
 * @author rikardwi
 **/
public interface BroadcastTextWebSocketMessageListener extends EventListener {
    
    /**
     * Called when a message should be broadcasted.
     * 
     * @param event broadcast event.
     */
    public void onBroadcastTextWebSocketMessage(BroadcastWebSocketTextMessageEvent event);
}
