package org.kasource.web.websocket.event;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.protocol.ConversionException;
import org.kasource.web.websocket.protocol.ProtocolHandler;

public class WebSocketTextObjectMessageEvent  extends WebSocketTextMessageEvent {
    private static final long serialVersionUID = 1L;
   
    private final ProtocolHandler<String> protocolHandler;
   
    /**
     * Constructor.
     * 
     * @param socket    web socket the message was sent to.
     * @param message   The message sent.
     * @param clientId  The ID of client who sent the message
     **/
    public WebSocketTextObjectMessageEvent(WebSocketChannel socket, 
                                     String message, 
                                     WebSocketClient client,
                                     ProtocolHandler<String> protocolHandler) {
        super(socket, message, client);
        this.protocolHandler = protocolHandler;
          
    }
    
   
    /**
     * Returns the message as Object.
     * 
     * @return the message
     */
    public <T> T getMessageAsObject(Class<T> ofType) throws ConversionException {
        return protocolHandler.toObject(getMessage(), ofType);
    }


    /**
     * @return the protocolHandler
     */
    public ProtocolHandler<String> getProtocolHandler() {
        return protocolHandler;
    }
}
