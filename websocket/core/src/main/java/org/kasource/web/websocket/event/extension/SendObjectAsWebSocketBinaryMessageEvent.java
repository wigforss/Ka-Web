package org.kasource.web.websocket.event.extension;


import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketBinaryObjectMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.protocol.ProtocolHandler;


/**
 * Event for sending a binary message to a specific client.
 * 
 * @author rikardwi
 **/
public class SendObjectAsWebSocketBinaryMessageEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;
    
    private final Object message;
    private final ProtocolHandler<byte[]> protocolHandler;
    private final String recipient;
    private final RecipientType recipientType;
    
    /**
     * Constructor.
     * 
     * @param socket    The web socket to send message to
     * @param message   The message to send
     * @param clientId  The recipient of the message
     **/
    public SendObjectAsWebSocketBinaryMessageEvent(WebSocketChannel socket, Object message, ProtocolHandler<byte[]> protocolHandler, String recipient, RecipientType recipientType) {
        super(socket);
        this.message = message;
        this.protocolHandler = protocolHandler;
        this.recipient = recipient;
        this.recipientType = recipientType;
       
    }
    
    public SendObjectAsWebSocketBinaryMessageEvent(WebSocketBinaryObjectMessageEvent event, Object message, RecipientType recipientType) {
        this(event.getSource(), 
             message, 
             event.getProtocolHandler(), 
             recipientType == RecipientType.CLIENT_ID ? event.getClientId() : event.getUsername(), 
             recipientType); 
       
    }

    /**
     * Returns the binary message to send.
     * 
     * @return the binary message to send.
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

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return the recipientType
     */
    public RecipientType getRecipientType() {
        return recipientType;
    }

  

    
}
