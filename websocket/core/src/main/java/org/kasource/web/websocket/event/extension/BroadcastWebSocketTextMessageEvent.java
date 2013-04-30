package org.kasource.web.websocket.event.extension;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketEvent;

/**
 * Event for broadcasting a text message.
 * 
 * @author rikardwi
 **/
public class BroadcastWebSocketTextMessageEvent extends WebSocketEvent {
    private static final long serialVersionUID = 1L;
    
    private final String message;
    
    /**
     * Constructor.
     * 
     * @param websocket The web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastWebSocketTextMessageEvent(WebSocketChannel websocket, String message) {
        super(websocket);
        this.message = message;
    }

    /**
     * Constructor.
     * 
     * @param event     Event that contains the web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastWebSocketTextMessageEvent(WebSocketEvent event, String message) {
        super(event.getSource());
        this.message = message;
    }

    
    /**
     * Returns the message to be broadcasted.
     * 
     * @return the message to be broadcasted.
     */
    public String getMessage() {
        return message;
    }
}
