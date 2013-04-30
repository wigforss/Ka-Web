package org.kasource.web.websocket.client.id;




import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.manager.WebSocketManager;

/**
 * Generates a client ID.
 * 
 * @author rikardwi
 **/
public class ClientIdGeneratorImpl extends AbstractClientIdGenerator {
   
 
    @Override
    public String getId(HttpServletRequest request, WebSocketManager manager) {
        String clientId = getIdValue(request);
        if(manager.hasClient(clientId)) {
            clientId = clientId + "-" + getUuid();
        }
        if(clientId == null) {
            clientId = getUuid();
        }
        
        return clientId;
    }
    
}
