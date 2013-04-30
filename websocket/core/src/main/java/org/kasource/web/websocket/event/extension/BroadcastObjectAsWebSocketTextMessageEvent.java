package org.kasource.web.websocket.event.extension;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextObjectMessageEvent;
import org.kasource.web.websocket.protocol.ProtocolHandler;

/**
 * Event for broadcasting a text message.
 * 
 * @author rikardwi
 **/
public class BroadcastObjectAsWebSocketTextMessageEvent extends WebSocketEvent {
    private static final long serialVersionUID = 1L;
    
    private final Object message;
    private ProtocolHandler<String> protocolHandler;
    /**
     * Constructor.
     * 
     * @param websocket The web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastObjectAsWebSocketTextMessageEvent(WebSocketChannel websocket, Object message, ProtocolHandler<String> protocolHandler) {
        super(websocket);
        this.message = message;
        this.protocolHandler = protocolHandler;
    }

    /**
     * Constructor.
     * 
     * @param event     Event that contains the web socket which will broadcast the message.
     * @param message   The message to broadcast.
     **/
    public BroadcastObjectAsWebSocketTextMessageEvent(WebSocketTextObjectMessageEvent event, Object message) {
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
    public ProtocolHandler<String> getProtocolHandler() {
        return protocolHandler;
    }
}
