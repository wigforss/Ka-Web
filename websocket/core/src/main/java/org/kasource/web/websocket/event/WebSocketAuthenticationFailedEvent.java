package org.kasource.web.websocket.event;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.WebSocketChannel;

public class WebSocketAuthenticationFailedEvent extends WebSocketUserEvent {

    private static final long serialVersionUID = 1L;

    private final HttpServletRequest request;
    private final Throwable cause;

    public WebSocketAuthenticationFailedEvent(WebSocketChannel websocket, String username, HttpServletRequest request, Throwable cause) {
        super(websocket, username);
        this.cause = cause;
        this.request = request;
    }

    /**
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }
    
    
}
