package org.kasource.web.websocket.listener;



import java.lang.reflect.Method;
import java.util.Arrays;

import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketBinaryObjectMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.WebSocketTextObjectMessageEvent;
import org.kasource.web.websocket.protocol.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketMessageMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketMessageMethod.class);
    
    private Object listener;
    private Method method;

    private Class<?> argumentType;
    
    public WebSocketMessageMethod(Object listener, Method method) {
        if(method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Message Listener method must be public");
        }
        Class<?>[] params = method.getParameterTypes();
        if(params.length != 1) {
            throw new IllegalArgumentException("WebSocket Message Listener method must have one parameter");
        }
        
       
        this.method = method;
        this.listener = listener;
        this.argumentType = method.getParameterTypes()[0];
       
    }

    
    private void onTextMessage(WebSocketTextMessageEvent messageEvent) {
       if(messageEvent instanceof WebSocketTextObjectMessageEvent) {
           invokeTextMessageMethodConverted((WebSocketTextObjectMessageEvent) messageEvent);
       } else {
           invokeTextMessageMethod(messageEvent);
       }
       
    }
    
    private void invokeTextMessageMethod(WebSocketTextMessageEvent messageEvent) {
        if(String.class.isAssignableFrom(argumentType)) {
            try {
             method.invoke(listener, messageEvent.getMessage());
            } catch (Exception e) {
             LOG.warn("Could not invoke " + method, e);
            } 
        }
    }
    
    private void invokeTextMessageMethodConverted(WebSocketTextObjectMessageEvent messageEvent) {
        try {
            method.invoke(listener, messageEvent.getMessageAsObject(argumentType));
        } catch(ConversionException e){
            LOG.warn("Could not convert " + messageEvent.getMessage() + " to " + argumentType + " as " + messageEvent.getProtocolHandler().getProtocolName());
        } catch (Exception e) {
            LOG.warn("Could not invoke " + method, e);
        }        
    }

    
    private void onBinaryMessage(WebSocketBinaryMessageEvent messageEvent) {
        if(messageEvent instanceof WebSocketBinaryObjectMessageEvent) {
            invokeBinaryMessageMethodConverted((WebSocketBinaryObjectMessageEvent) messageEvent);
        } else {
            invokeBinaryMessageMethod(messageEvent);
        }
        
    }

    private void invokeBinaryMessageMethod(WebSocketBinaryMessageEvent messageEvent) {
        if(byte[].class.isAssignableFrom(argumentType)) {
            try {
                byte[] data =  messageEvent.getMessage();
                method.invoke(listener, listener, Arrays.asList(data).toArray());
            } catch (Exception e) {
             LOG.warn("Could not invoke " + method, e);
            } 
        }
        
    }
    
    private void invokeBinaryMessageMethodConverted(WebSocketBinaryObjectMessageEvent messageEvent) {
        try {
            method.invoke(listener, messageEvent.getMessageAsObject(argumentType));
        } catch(ConversionException e){
            LOG.warn("Could not convert " + messageEvent.getMessage() + " to " + argumentType + " as " + messageEvent.getProtocolHandler().getProtocolName());
        } catch (Exception e) {
            LOG.warn("Could not invoke " + method, e);
        } 
        
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {   
        if(event instanceof WebSocketTextMessageEvent) {
            onTextMessage((WebSocketTextMessageEvent) event);
        } else if(event instanceof WebSocketBinaryMessageEvent) {
            onBinaryMessage((WebSocketBinaryMessageEvent) event);
        }
        
    }



   
}
