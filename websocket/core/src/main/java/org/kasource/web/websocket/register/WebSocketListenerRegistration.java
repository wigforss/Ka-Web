package org.kasource.web.websocket.register;


/**
 * Registers the implementing class as a listener to specified web socket.
 * 
 * Note: The annotation @WebSocketListener can be used for the same purpose.
 * 
 * Registers any class that implements this interface as listeners on 
 * the specified web socket if it also implements any of the specified listener interfaces
 * such as WebSocketEventListener. 
 * 
 * {@code
 *  public class ChatServer implements WebSocketListenerRegistration, WebSocketEventListener {
 *     
 *      public String getWebSocketChannelName() {
 *          return "/chat";
 *      }
 *      
 *      public void onWebSocketEvent(WebSocketTextMessageEvent event) {
 *          event.getSource().broadcast(event.getMessage());
 *      }
 *  }
 * }
 * 
 * @author rikardwi
 **/
public interface WebSocketListenerRegistration {
    
    /**
     * Return the name of the web socket to listen to.
     * 
     * @return name of the web socket to listen to.
     **/
    public String getWebSocketChannelName();
}
