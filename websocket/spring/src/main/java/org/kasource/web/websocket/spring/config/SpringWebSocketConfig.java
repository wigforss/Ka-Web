package org.kasource.web.websocket.spring.config;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;

public class SpringWebSocketConfig extends WebSocketConfigImpl {

    private ClientIdGenerator clientIdGenerator;
    
    public void registerServlet(WebSocketServletConfigImpl servlet) {
       
        if(getOrginWhitelist() != null) {
            servlet.setOriginWhitelist(getOrginWhitelist());
        } 
        if(servlet.getClientIdGenerator() == null) {
            servlet.setClientIdGenerator(clientIdGenerator != null ? clientIdGenerator : new DefaultClientIdGenerator());
        }
        getServletConfigs().put(servlet.getServletName(), servlet);
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }
}
