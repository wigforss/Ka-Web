package org.kasource.web.websocket.event;

import org.kasource.web.websocket.channel.WebSocketChannel;

public class WebSocketUserEvent extends WebSocketEvent {
    private static final long serialVersionUID = 1L;
    protected static final String DEFAULT_USER = "Anonymous";
    private final String username;
    
    /**
     * Constructor.
     * 
     * @param websocket The source web socket.
     * @param username  User name 
     **/
    public WebSocketUserEvent(WebSocketChannel websocket, String username) {
        super(websocket);
        this.username = username;
        
    }
    
    
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
}
