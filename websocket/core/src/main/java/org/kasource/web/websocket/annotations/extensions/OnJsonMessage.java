package org.kasource.web.websocket.annotations.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.web.websocket.annotations.WebSocketEventAnnotation;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.listener.extension.JsonMessageListener;

/**
 * Convenience annotation that can be used decode a JSON message to Object, which can be used for automatic conversion
 * of text messages, without using a protocol handler.
 * 
 * If more information than the actual message is needed use the standard method annotation @OnWebSocketEvent instead,
 * which gives you all the details of the event such as clientId and the source WebSocketChannel.
 * 
 *@{code
 * 
 * @WebSocketListener("/chat")
 * public class ChatServer {
 * 
 *  @OnJsonMessage
 *  public void onMessage(Message message) {
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
@WebSocketEventAnnotation
public @interface OnJsonMessage {
    Class<? extends WebSocketEventListener> value() default JsonMessageListener.class; 
    Class<?>[] arguments() default {Object.class};
}
