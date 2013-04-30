package org.kasource.web.websocket.event;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.channel.WebSocketChannel;

public class WebSocketAuthenticationSuccessEvent extends WebSocketUserEvent {

    private static final long serialVersionUID = 1L;

    private final HttpServletRequest request;
    
    public WebSocketAuthenticationSuccessEvent(WebSocketChannel websocket, String username, HttpServletRequest request) {
        super(websocket, username);
        this.request = request;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

}
