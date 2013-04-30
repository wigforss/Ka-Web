package org.kasource.web.websocket.listener.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.kasource.web.websocket.listener.WebSocketEventListener;

/**
 * A custom WebSocketListener which can be bound via annotations.
 * 
 * Implementing classes of this interface needs to provide a public empty constructor.
 * 
 * @author rikardwi
 **/
public interface ExtendedWebSocketEventListener extends WebSocketEventListener {

    void setMethod(Method method);
    
    void setListener(Object listener);
    
    void setAnnotation(Annotation annotation);
    
    void initialize();
}
