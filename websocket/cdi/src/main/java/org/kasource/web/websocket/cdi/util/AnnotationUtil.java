package org.kasource.web.websocket.cdi.util;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;





public final class AnnotationUtil {
    
    private AnnotationUtil() { }
    
    @SuppressWarnings({ "unchecked", "serial" })
    public static <T extends Annotation> T getAnnotation(final Class<T> annoClass) {
        return (T) new AnnotationLiteral<T>() {
           
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return annoClass;
            }
            
           
        };
    }
}
