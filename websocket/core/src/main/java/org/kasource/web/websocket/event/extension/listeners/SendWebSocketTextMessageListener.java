package org.kasource.web.websocket.event.extension.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.extension.SendWebSocketTextMessageEvent;

/**
 * Listener Interface for SendWebSocketTextMessageEvent.
 * 
 * @author rikardwi
 **/
public interface SendWebSocketTextMessageListener extends EventListener {
    
    /**
     * Called when a text message should be sent to a specific client.
     * 
     * @param event message send event.
     */
    void SendWebSocketTextMessage(SendWebSocketTextMessageEvent event);
}
