package org.kasource.web.websocket.listener;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;


import org.kasource.web.websocket.WebSocketEventListener;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketEventType;


public class WebSocketEventMethod implements WebSocketEventListener {

    private Object listener;
    private Method method;
    private EnumSet<WebSocketEventType> filters;
    
    
    public WebSocketEventMethod(Object listener, Method method, WebSocketEventType... filter) {
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
        if(filter.length != 0) {
            filters = EnumSet.copyOf(Arrays.asList(filter));
           
        }
    }



    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        try {
            if(filters == null || filters.isEmpty()) {
                method.invoke(listener, event);
            } else if (filters.contains(event.getType())) {
                method.invoke(listener, event);
            }
           
        } catch (IllegalArgumentException e) {
            
        } catch (IllegalAccessException e) {
           
        } catch (InvocationTargetException e) {
            
        }
        
    }


   
}
