package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.web.websocket.event.WebSocketEventType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebSocketEventListener {
    /**
     * The web socket servlet to listen on.
     **/
    public String value();
    
    /**
     * Events to select, leave as default for all events.
     **/
    public WebSocketEventType[] filter() default {};
}
