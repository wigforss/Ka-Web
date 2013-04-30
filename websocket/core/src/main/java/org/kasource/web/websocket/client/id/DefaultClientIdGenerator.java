package org.kasource.web.websocket.client.id;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.manager.WebSocketManager;

public class DefaultClientIdGenerator implements ClientIdGenerator {

    @Override
    public String getId(HttpServletRequest request, WebSocketManager manager) {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
