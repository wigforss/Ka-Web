package org.kasource.web.websocket.event;


import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.client.WebSocketClient;


/**
 * Event emitted when a binary message has been received from 
 * a client.
 * 
 * @author rikardwi
 **/
public class WebSocketBinaryMessageEvent extends WebSocketClientEvent {

    private static final long serialVersionUID = 1L;
    
    private final byte[] message;
   
    /**
     * Constructor.
     * 
     * @param socket    web socket the message was sent to.
     * @param message   The message sent.
     * @param clientId  The ID of client who sent the message
     **/
    public WebSocketBinaryMessageEvent(WebSocketChannel socket, byte[] message, WebSocketClient client) {
        super(socket, client);
        this.message = message;
      
    }
    
    

    /**
     * Returns the message.
     * 
     * @return the message
     */
    public byte[] getMessage() {
        return message;
    }

  
    

    
}
