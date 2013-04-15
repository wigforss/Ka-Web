package org.kasource.web.websocket.cdi.event;

import java.lang.annotation.Annotation;

/**
 * Looks up annotation configured for a specific context path.
 * 
 * @author rikardwi
 **/
public interface AnnotationWebsocketBinding {
    
    /**
     * Returns the annotation that has been configured for the supplied
     * context path.
     * 
     * @param socketContextPath Context path of a web socket.
     * 
     * @return the annotation that has been configured for the supplied
     * context path.
     **/
    public Annotation getAnnotationForSocket(String socketContextPath);
}
