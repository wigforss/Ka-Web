package org.kasource.web.websocket.listener.extension;

import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.protocol.XmlProtocolHandler;
import org.kasource.web.websocket.protocol.TextProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbMessageListener extends AbstractdMethodWebSocketEventListener {
    private static Logger LOG = LoggerFactory.getLogger(JaxbMessageListener.class);

    private TextProtocolHandler jaxb = new XmlProtocolHandler();
    private Class<?> parameter;
    
    @Override
    public void initialize() {
        super.initialize();
        parameter = method.getParameterTypes()[0];
    }
    
    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        String message = "";
        try {
            if(WebSocketTextMessageEvent.class.isAssignableFrom(event.getClass())) {
                message = ((WebSocketTextMessageEvent) event).getMessage();
                method.invoke(listener, jaxb.toObject(message, parameter));
            }
        } catch (Exception e) {
            LOG.warn(JaxbMessageListener.class + " could not invoke method: " + method + " with message: " + message, e);
        }
        
    }
    
}
