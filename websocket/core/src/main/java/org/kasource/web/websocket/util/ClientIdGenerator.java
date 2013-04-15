package org.kasource.web.websocket.util;


import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.impl.AbstractWebSocketManager;

public class ClientIdGenerator {
    private String idParameter;
    
    public ClientIdGenerator(String idParameter) {
        this.idParameter = idParameter;
    }
    
    public String getId(HttpServletRequest request, AbstractWebSocketManager manager) {
        String clientId = request.getParameter(idParameter);
        if(manager.hasClient(clientId)) {
            clientId = clientId + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
        }
        if(clientId == null) {
            clientId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        }
        
        return clientId;
    }
    
}
