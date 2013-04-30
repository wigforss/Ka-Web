package org.kasource.web.websocket.event.extension;


import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.event.WebSocketClientEvent;
import org.kasource.web.websocket.event.WebSocketEvent;

/**
 * Event for sending a text message to a specific client.
 * 
 * @author rikardwi
 **/
public class SendWebSocketTextMessageEvent extends WebSocketEvent {

    private static final long serialVersionUID = 1L;
    
    private final String message;
    private final String recipient;
    private final RecipientType recipientType;
    
    /**
     * Constructor.
     * 
     * @param socket    The web socket to send message to
     * @param message   The message to send
     * @param clientId  The recipient of the message
     **/
    public SendWebSocketTextMessageEvent(WebSocketChannel socket, String message, String recipient, RecipientType recipientType) {
        super(socket);      
        this.message = message;
        this.recipient = recipient;
        this.recipientType = recipientType;
    }
    
    public SendWebSocketTextMessageEvent(WebSocketClientEvent event, String message, RecipientType recipientType) {
        this(event.getSource(), 
             message, 
             recipientType == RecipientType.CLIENT_ID ? event.getClientId() : event.getUsername(), 
             recipientType);
        
    }
    
  
   
    /**
     * Returns the text message to send.
     * 
     * @return the text message to send.
     */
    public String getMessage() {
        return message;
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
