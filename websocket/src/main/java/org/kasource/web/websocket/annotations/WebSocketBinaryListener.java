package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate methods that wants to listen to binary messages
 * with this annotation.
 * 
 * NOTE: The method must implement the correct signature from WebSocketMessageListener.onBinaryMessage(),
 * which is two arguments, the first an InputStream and the other a String.
 * 
 * @author rikardwi
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebSocketBinaryListener {
    /**
     * The web socket servlet to listen on.
     **/
    public String value();
}

