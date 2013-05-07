package org.kasource.web.websocket.channel;

import java.io.IOException;

import org.kasource.web.websocket.RecipientType;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.protocol.ConversionException;
import org.kasource.web.websocket.protocol.ProtocolHandler;



/**
 * Web Socket Channel.
 * 
 * Exposes methods that can be invoked on an underlying Web Socket Servlet.
 * 
 * @author rikardwi
 **/
public interface WebSocketChannel extends WebsocketMessageSender {
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     **/
    public void broadcastJsonMessage(Object message);
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     **/
    public void broadcastXmlMessage(Object message);
    
    /**
     * Broadcasts a text message to all clients.
     * 
     * @param message Message to send.
     * @param protocolHandler The protocol handler
     **/
    public void broadcastObject(Object message, ProtocolHandler<String> protocolHandler);
    
    
    /**
     * Broadcasts a binary message to all clients.
     * 
     * @param message Message to send.
     * @param protocolHandler The protocol handler
     **/
    public void broadcastBinaryObject(Object message, ProtocolHandler<byte[]> protocolHandler);
    
    
    public void sendMessage(Object message, ProtocolHandler<String> protocolHandler, String recipient, RecipientType recipientType) throws IOException; 
    
    /**
     * Sends an Object as JSON to a specific client.
     * 
     * @param message Message to send.
     * @param clientId ID of the client to receive the message.
     * 
     * @throws IOException if message could not be sent.
     * @throws NoSuchWebSocketClient if there is no connected client with the supplied clientId.
     * @throws ConversionException if message could  not be serialized to JSON.
     **/
    public void sendMessageAsJson(Object message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient, ConversionException; 
    
    /**
     * Sends an Object as JSON to a specific client.
     * 
     * @param message Message to send.
     * @param clientId ID of the client to receive the message.
     * 
     * @throws IOException if message could not be sent.
     * @throws NoSuchWebSocketClient if there is no connected client with the supplied clientId.
     * @throws ConversionException if message could  not be serialized to JSON.
     **/
    public void sendMessageAsXml(Object message, String recipient, RecipientType recipientType) throws IOException, NoSuchWebSocketClient, ConversionException; 
    
    
    public void sendBinaryMessage(Object message, ProtocolHandler<byte[]> protocolHandler, String recipient, RecipientType recipientType) throws IOException; 
    
     

    
    /**
     * Adds an event listener.
     * 
     * @param listener Event listener to add.
     **/
    public void addListener(WebSocketEventListener listener);
    
    /**
     * Returns the name of the web socket.
     * 
     * @return name of the web socket.
     **/
    public String getUrl();
}
