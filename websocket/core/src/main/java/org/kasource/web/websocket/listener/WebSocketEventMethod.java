package org.kasource.web.websocket.listener;



import java.lang.reflect.Method;

import org.kasource.web.websocket.event.WebSocketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketEventMethod implements WebSocketEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketEventMethod.class);
    private Object listener;
    private Method method;

    private Class<?> eventType;
    
    public WebSocketEventMethod(Object listener, Method method) {
        if(method.isAccessible()) {
            throw new IllegalArgumentException("WebSocket Event Listener method must be public");
        }
        Class<?>[] params = method.getParameterTypes();
        if(params.length != 1) {
            throw new IllegalArgumentException("WebSocket Event Listener method must have one parameter");
        }
        
        if(!WebSocketEvent.class.isAssignableFrom(params[0])) {
            throw new IllegalArgumentException("WebSocket Event Listeners method argument must be a WebSocketEvent or subclass thereof.");
        }
       
        this.method = method;
        this.listener = listener;
        this.eventType = method.getParameterTypes()[0];
       
    }



    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        try {
            if(eventType.isAssignableFrom(event.getClass())) {
                method.invoke(listener, event);
            }
        } catch (Exception e) {
           LOG.error("Could not invoke " + method, e);
        } 
        
    }


   
}
