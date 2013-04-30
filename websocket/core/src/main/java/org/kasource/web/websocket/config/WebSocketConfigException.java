package org.kasource.web.websocket.config;

public class WebSocketConfigException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WebSocketConfigException(String message) {
        super(message);
    }
    
    public WebSocketConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
