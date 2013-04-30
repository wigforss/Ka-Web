package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * META annotation for WebSocketEvent annotations. 
 * 
 * Annotate annotations that are used for marking methods
 * as event listeners with this annotation. This will enable custom 
 * annotations to be used.
 * 
 * @author rikardwi
 **/
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketEventAnnotation {

}
