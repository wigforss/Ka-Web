package org.kasource.web.websocket.event.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;

/**
 * Listener interface for WebSocketBinaryMessageEvennt.
 * 
 * @author rikardwi
 */
public interface WebSocketBinaryMessageListener extends EventListener {

    /**
     * Called when a new binary message have been received.
     * 
     * @param event Message event
     **/
    public void onBinaryMessage(WebSocketBinaryMessageEvent event);
}
