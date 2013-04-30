package org.kasource.web.websocket.listener;

import org.kasource.web.websocket.event.WebSocketEvent;
import org.kasource.web.websocket.event.WebSocketTextMessageEvent;
import org.kasource.web.websocket.event.listeners.WebSocketTextMessageListener;

public class WebSocketTextMessageHandler implements WebSocketEventListener {
   private WebSocketTextMessageListener listener;
    
    public WebSocketTextMessageHandler(WebSocketTextMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onWebSocketEvent(WebSocketEvent event) {
        if(event instanceof WebSocketTextMessageEvent){
            listener.onMessage((WebSocketTextMessageEvent)event);
        }
        
    }
    
}
