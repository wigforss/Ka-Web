package org.kasource.web.websocket.event;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Base class for client related web socket events.
 * 
 * @author rikardwi
 **/
public abstract class WebSocketClientEvent extends WebSocketUserEvent {

    private static final long serialVersionUID = 1L;

    private final WebSocketClient client;
 
    
  
    
    /**
     * Constructor.
     * 
     * @param websocket The source web socket.
     * @param clientId  Client ID
     * @param username  Username of the client
     **/
    public WebSocketClientEvent(WebSocketChannel websocket, WebSocketClient client) {
        super(websocket, client.getUsername() == null ? DEFAULT_USER : client.getUsername());
        this.client = client;
       
    }
    
    
    /**
     * @return the clientId
     */
    public String getClientId() {
        return client.getId();
    }

    /**
     * @return the client
     */
    public WebSocketClient getClient() {
        return client;
    }

    
    
}
