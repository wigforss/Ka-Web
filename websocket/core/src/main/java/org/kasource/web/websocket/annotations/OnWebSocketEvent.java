package org.kasource.web.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to register a method which should listen to websocket events.
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
 * }
 * 
 * }
 * 
 * 
 * @author rikardwi
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnWebSocketEvent {
   
}
