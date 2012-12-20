package org.kasource.web.websocket.event;

import java.util.EventObject;

import org.kasource.web.websocket.WebSocketManager;

public class WebSocketEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private WebSocketEventType type;
    
    
    public WebSocketEvent(WebSocketManager websocket, WebSocketEventType type) {
        super(websocket);
        this.type = type;
    }
    
    @Override
    public WebSocketManager getSource() {
        return (WebSocketManager) this.getSource();
    }

    /**
     * @return the type of event.
     */
    public WebSocketEventType getType() {
        return type;
    }
}
