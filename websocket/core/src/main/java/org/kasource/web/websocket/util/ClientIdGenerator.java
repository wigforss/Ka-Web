package org.kasource.web.websocket.util;


import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.manager.WebSocketManager;

/**
 * Generates a client ID.
 * 
 * @author rikardwi
 **/
public class ClientIdGenerator {
    private String idParameter;
    
    public ClientIdGenerator(String idParameter) {
        this.idParameter = idParameter;
    }
    
    /**
     * Returns a new client ID from a request.
     * 
     * Tries to reuse username as client ID if possible, to allow for
     * easier debugging.
     * 
     * @param request   The HTTP Request
     * @param manager   The WebSocketManager to query for used IDs.
     * 
     * @return A unique client ID for the manager supplied.
     **/
    public String getId(HttpServletRequest request, WebSocketManager manager) {
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
