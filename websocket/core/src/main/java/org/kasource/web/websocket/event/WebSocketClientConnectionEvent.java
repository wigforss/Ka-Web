package org.kasource.web.websocket.event;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.client.WebSocketClient;

/**
 * Event emitted when a client has connected to a web socket.
 * 
 * @author rikardwi
 **/
public class WebSocketClientConnectionEvent extends WebSocketClientEvent {
    private static final long serialVersionUID = 1L;
    

    /**
     * Constructor.
     * 
     * @param websocket             The websocket that a client connected to.
     * @param clientId              The ID of the client 
     * @param connectionParameters  The connection parameters
     **/
    public WebSocketClientConnectionEvent(WebSocketChannel websocket, 
                                          WebSocketClient client) {
        super(websocket, client);
        
    }

   
    
   

}
