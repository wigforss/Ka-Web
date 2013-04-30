package org.kasource.web.websocket.event.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.WebSocketTextMessageEvent;

/**
 * Listener interface for WebSocketBinaryMessageEvennt.
 * 
 * @author rikardwi
 */
public interface WebSocketTextMessageListener extends EventListener {
   
    /**
     * Called when a new text message have been received.
     * 
     * @param event Message event
     **/
    public void onMessage(WebSocketTextMessageEvent event);
}
