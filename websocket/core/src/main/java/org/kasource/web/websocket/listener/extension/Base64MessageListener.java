package org.kasource.web.websocket.listener.extension;


import java.util.Arrays;

import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.protocol.Base64ProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base64MessageListener extends AbstractdMethodWebSocketEventListener {
    private static Logger LOG = LoggerFactory.getLogger(Base64MessageListener.class);
   
    private TextProtocolHandler base64 = new Base64ProtocolHandler();
    
    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        String message = "";
        try {
            if(WebSocketTextMessageEvent.class.isAssignableFrom(event.getClass())) {
                message = ((WebSocketTextMessageEvent) event).getMessage();
                byte[] data = base64.toObject(message, byte[].class);
                
                method.invoke(listener, Arrays.asList(data).toArray());
            }
        } catch (Exception e) {
            LOG.warn(Base64MessageListener.class + " could not invoke " + method + " message: " + message, e);
        } 
        
    }
}
