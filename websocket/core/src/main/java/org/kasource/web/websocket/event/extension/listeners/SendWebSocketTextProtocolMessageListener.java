package org.kasource.web.websocket.event.extension.listeners;

import java.util.EventListener;

import org.kasource.web.websocket.event.extension.SendObjectAsWebSocketTextMessageEvent;

/**
 * Listener Interface for SendWebSocketTextMessageEvent.
 * 
 * @author rikardwi
 **/
public interface SendWebSocketTextProtocolMessageListener extends EventListener {
    
    /**
     * Called when a text message should be sent to a specific client.
     * 
     * @param event message send event.
     */
    void SendWebSocketTextMessage(SendObjectAsWebSocketTextMessageEvent event);
}
