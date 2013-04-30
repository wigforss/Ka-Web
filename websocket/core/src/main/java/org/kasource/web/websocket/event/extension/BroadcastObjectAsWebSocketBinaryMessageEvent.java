package org.kasource.web.websocket.event.extension;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketBinaryObjectMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.protocol.ProtocolHandler;

/**
 * Event for broadcasting a binary message.
 * 
 * @author rikardwi
 **/
public class BroadcastObjectAsWebSocketBinaryMessageEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;
    
    private final ProtocolHandler<byte[]> protocolHandler;
    private final Object message;
    
    /**
     * Constructor.
     * 
     * @param websocket The web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastObjectAsWebSocketBinaryMessageEvent(WebSocketChannel websocket, Object message, ProtocolHandler<byte[]> protocolHandler) {
        super(websocket);
        this.message = message;
        this.protocolHandler = protocolHandler;
    }
    
    /**
     * Constructor.
     * 
     * @param websocket The event that contains the web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastObjectAsWebSocketBinaryMessageEvent(WebSocketBinaryObjectMessageEvent event, Object message) {
        super(event.getSource());
        this.message = message;
        this.protocolHandler = event.getProtocolHandler();
    }

    /**
     * Returns the message to be broadcasted.
     * 
     * @return the message to be broadcasted.
     */
    public Object getMessage() {
        return message;
    }

    /**
     * @return the protocolHandler
     */
    public ProtocolHandler<byte[]> getProtocolHandler() {
        return protocolHandler;
    }
   

}
