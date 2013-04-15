package org.kasource.web.websocket.cdi.event;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

/**
 * Configures which annotation is mapped to what context path. 
 * 
 * This allows the qualification annotations to be used in
 * the org.kasource.web.websocket.cdi.event.EventBridge.
 * 
 * Note that annotations used must be annotated with javax.inject.Qualifier
 * 
 * @author rikardwi
 **/
public class CdiWebSocketMapping implements AnnotationWebsocketBinding {
    private final Map<String, Annotation> mapping;
    
    public CdiWebSocketMapping() {
        mapping = new HashMap<String, Annotation>();
    }
    
    private CdiWebSocketMapping(Builder builder) {
        mapping = builder.mapping;
    }
    
    public static class Builder {
        private Map<String, Annotation> mapping = new HashMap<String, Annotation>();
        
        public Builder map(String socketName, Class<? extends Annotation> annotationClass) {
            if(annotationClass.isAnnotationPresent(Qualifier.class)) {
                mapping.put(socketName, getAnnotation(annotationClass));
                return this;
            } else {
                throw new IllegalArgumentException(annotationClass + " must be annotated with @"+Qualifier.class);
            }
        }
        
        @SuppressWarnings({ "unchecked", "serial" })
        private <T extends Annotation> T getAnnotation(final Class<T> annoClass) {
            return (T) new AnnotationLiteral<T>() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return annoClass;
                }
            };
        }
        
        public CdiWebSocketMapping build() {
            return new CdiWebSocketMapping(this);
        }
    }
    
     
    public Annotation getAnnotationForSocket(String socketName) {
        return mapping.get(socketName);
    }
    
   
}
