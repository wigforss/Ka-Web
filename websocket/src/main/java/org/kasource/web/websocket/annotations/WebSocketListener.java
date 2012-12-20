package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate classes that implements the WebSocketMessageListener interface
 * with this annotation register it as a listener.
 * 
 * @author rikardwi
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebSocketListener {
    /**
     * The web socket servlet to listen on.
     **/
    public String value();
}
