package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to map which websocket to listen to. 
 * 
 * If this annotation is applied on a class level it will
 * be the default value for all methods. Method level overrides the class
 * level annotation.
 * 
 * @{code
 * 
 * @WebSocketListener("/chat")
 * public class ChatServer {
 * 
 *  @OnWebSocketEvent
 *  public void onTextMessage(WebSocketTextMessageEvent event) {
 *   ...
 *  }
 *  
 *  @WebSocketListener("/chat2")
 *  @OnWebSocketEvent
 *  public void onTextMessage(WebSocketTextMessageEvent event) {
 *   ...
 *  }
 * }
 * 
 * }
 * 
 * @author rikardwi
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WebSocketListener {
    /**
     * The web socket channel to listen on.
     **/
    public String value();
    
    
}
