package org.kasource.web.websocket.listener.extension;

import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.protocol.JsonProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMessageListener extends AbstractdMethodWebSocketEventListener {
    private static Logger LOG = LoggerFactory.getLogger(JsonMessageListener.class);
    
    private TextProtocolHandler json = new JsonProtocolHandler();

    private Class<?> parameterType;
    
    @Override
    public void initialize() {
        super.initialize();
        parameterType = method.getParameterTypes()[0];
    }
    
    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        String message = "";
        try {
            if(WebSocketTextMessageEvent.class.isAssignableFrom(event.getClass())) {
                message = ((WebSocketTextMessageEvent) event).getMessage();
                method.invoke(listener, json.toObject(message, parameterType));
            }
        } catch (Exception e) {
            LOG.warn(JsonMessageListener.class + " could not invoke method: " + method + " with message: " + message, e);
        }
        
    }
    
   
}
