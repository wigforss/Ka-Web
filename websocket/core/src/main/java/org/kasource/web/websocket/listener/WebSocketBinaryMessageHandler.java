package org.kasource.web.websocket.listener;

import org.kasource.web.websocket.event.WebSocketBinaryMessageEvent;
import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.listeners.WebSocketBinaryMessageListener;

public class WebSocketBinaryMessageHandler implements WebSocketEventListener{
   private WebSocketBinaryMessageListener listener;
    
    public WebSocketBinaryMessageHandler(WebSocketBinaryMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if(event instanceof WebSocketBinaryMessageEvent){
            listener.onBinaryMessage((WebSocketBinaryMessageEvent) event);
        }
        
    }
    
}
