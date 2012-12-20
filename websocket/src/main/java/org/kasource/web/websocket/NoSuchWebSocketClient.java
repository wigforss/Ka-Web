package org.kasource.web.websocket;

public class NoSuchWebSocketClient extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchWebSocketClient(String message) {
        super(message);
    }
}
