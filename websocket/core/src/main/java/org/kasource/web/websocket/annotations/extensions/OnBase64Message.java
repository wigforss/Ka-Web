package org.kasource.web.websocket.annotations.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.web.websocket.annotations.WebSocketEventAnnotation;
import org.kasource.web.websocket.listener.WebSocketEventListener;
import org.kasource.web.websocket.listener.extension.Base64MessageListener;

/**
 * Convenience annotation that can be used convert a Base64 text message to byte[], which can be used for automatic conversion
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
 *  @OnBase64Message
 *  public void onMessage(byte[] message) {
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
public @interface OnBase64Message {
    Class<? extends WebSocketEventListener> value() default Base64MessageListener.class; 
    Class<?>[] arguments() default {byte[].class};
}
