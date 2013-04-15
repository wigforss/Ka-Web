package org.kasource.web.websocket.event;

import java.util.Map;

import org.kasource.web.websocket.channel.WebSocketChannel;

/**
 * Event emitted when a client has connected to a web socket.
 * 
 * @author rikardwi
 **/
public class WebSocketClientConnectionEvent extends WebSocketClientEvent {
    private static final long serialVersionUID = 1L;
    
    private final Map<String, String[]> connectionParameters;
   
    /**
     * Constructor.
     * 
     * @param websocket             The websocket that a client connected to.
     * @param clientId              The ID of the client 
     * @param connectionParameters  The connection parameters
     **/
    public WebSocketClientConnectionEvent(WebSocketChannel websocket, 
                                          String clientId, 
                                          Map<String, String[]> connectionParameters) {
        super(websocket, clientId);
        this.connectionParameters = connectionParameters;
    }

    /**
     * Returns the connectionParameters.
     * 
     * @return the connectionParameters
     */
    public Map<String, String[]> getConnectionParameters() {
        return connectionParameters;
    }

    
   

}
