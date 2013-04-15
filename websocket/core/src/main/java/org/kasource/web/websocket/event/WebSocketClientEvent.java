package org.kasource.web.websocket.event;

import org.kasource.web.websocket.channel.WebSocketChannel;

/**
 * Base class for client related web socket events.
 * 
 * @author rikardwi
 **/
public abstract class WebSocketClientEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;

    private final String clientId;
    
    /**
     * Constructor.
     * 
     * @param websocket The source web socket.
     * @param clientId  Client ID
     **/
    public WebSocketClientEvent(WebSocketChannel websocket, String clientId) {
        super(websocket);
        this.clientId = clientId;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }
    
    
}
