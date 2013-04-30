package org.kasource.web.websocket.event;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Event emitted when a client was disconnected.
 * 
 * @author rikardwi
 **/
public class WebSocketClientDisconnectedEvent extends WebSocketClientEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param webSocket The web socket the client closed connection to
     * @param clientId  The ID of the client who closed the connection
     */
    public WebSocketClientDisconnectedEvent(WebSocketChannel webSocket, WebSocketClient client) {
        super(webSocket, client);
    }
}
