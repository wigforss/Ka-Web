package org.kasource.web.websocket.register;



/**
 * Register an object as a listener on web socket events.
 * 
 * Integrations will be provided to Dependency Injection frameworks such as Spring, Guice and CDI, so that
 * no implicit calls will be needed when using these.
 * 
 * Both annotation and interface based configuration of the
 * the listener class is supported.
 * <p/>
 * Using annotations any POJO class can be annotated
 * {@code
 *   @WebSocketListener("/chat")
 *   public class ChatServer {
 *      @WebSocketEvent
 *      public void onMessage(WebSocketTextMessageEvent event) {
 *          event.getSource().broadcast(event.getMessage()):
 *      }
 *   }
 *   
 * } 
 * Using the interface approach the listener class can implement
 * the proper interfaces matching the annotations above.
 * 
 * {@code
 * public class ChatServer implements WebSocketListenerRegistration, WebSocketTextMessageListener {
 *   
 *   public String getWebSocketChannelName() {
 *      return "/chat";
 *   }
 *   
 *   public WebSocketTextMessageEvent(WebSocketChannel socket, 
 *                                    String message, 
 *                                    String clientId) {
 *      socket.broadcast(message);                                 
 *    }
 * 
 * }
 *  
 * Listener can registered by calling the registerListener method:
 * 
 * {@code
 * 
 *  public class ChatSerever {
 * 
 *   @Inject
 *   private  WebSocketListenerRegister register;
 *   
 *   @PostConstruct
 *   private void initialize() {
 *      register.registerListener(this);
 *   }
 *   
 *   }
 * }
 *  
 * @author rikardwi
 **/
public interface WebSocketListenerRegister {

    /**
     * Registers the listener object if its configuration is applicable for 
     * Web Socket listening.
     * 
     * For a listener object to qualify its class needs to either be annotated correctly or implement
     * the correct interfaces.
     * <p/>
     * When using annotations the class can be annotated with @WebSocketListener, which will provide
     * a default for all methods or annotate a method with @WebSocketListener. 
     * <p/>
     * Any method in the listener class
     * that is annotated with @OnWebSocketEvent, which also has @WebSocketListener annotation on class or
     * method level, will be eligible for registration. The method can also be annotated with an extension
     * annotation such as @OnWebSocketTextMessage and @OnWebSocketBinaryMessage instead of @OnWebSocketEvent.
     * <p/>
     * Extensions can be created to provide its own annotations and event conversion strategy, these
     * annotations need to be annotated with the Meta annotation @WebSocketListenerAnnotation and provide its 
     * implementation in the value field.
     * <p/>
     * Two such extensions are provided in this project: @OnWebSocketTextMessage and @OnWebSocketBinaryMessage.
     * <p/>
     * Using interface configuration the listener should implement WebSocketListenerRegistration together
     * with WebSocketEventListener and/or WebSocketBinaryMessageListener and/or WebSocketTextMessageListener
     * and/or WebSocketClientConnectionListener and/or WebSocketClientDisconnectedListener.
     * 
     * @param listener Object to inspect and register for event listener if properly configured.
     **/
    public void registerListener(Object listener);

}
