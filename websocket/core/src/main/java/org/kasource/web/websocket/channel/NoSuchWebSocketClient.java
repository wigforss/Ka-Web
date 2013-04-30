package org.kasource.web.websocket.channel;

/**
 * Exception thrown when no client can be found by the supplied ID.
 * 
 * @author rikardwi
 **/
public class NoSuchWebSocketClient extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param message Error message.
     **/
    public NoSuchWebSocketClient(String message) {
        super(message);
    }
}
