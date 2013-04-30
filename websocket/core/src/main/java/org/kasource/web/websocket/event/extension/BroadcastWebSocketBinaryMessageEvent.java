package org.kasource.web.websocket.event.extension;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketEvent;

/**
 * Event for broadcasting a binary message.
 * 
 * @author rikardwi
 **/
public class BroadcastWebSocketBinaryMessageEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;
    
    private final byte[] message;
    
    /**
     * Constructor.
     * 
     * @param websocket The web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastWebSocketBinaryMessageEvent(WebSocketChannel websocket, byte[] message) {
        super(websocket);
        this.message = message;
    }
    
    /**
     * Constructor.
     * 
     * @param websocket The event that contains the web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastWebSocketBinaryMessageEvent(WebSocketEvent event, byte[] message) {
        super(event.getSource());
        this.message = message;
    }

    /**
     * Returns the message to be broadcasted.
     * 
     * @return the message to be broadcasted.
     */
    public byte[] getMessage() {
        return message;
    }
   

}
