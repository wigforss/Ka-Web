package org.kasource.web.websocket.config.xml;

import java.util.Set;


import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.xml.jaxb.WebsocketXml;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;

public class XmlWebSocketServletConfig implements WebSocketServletConfig {

    private String servletName;
    private boolean dynamicAddressing;
    private ClientIdGenerator clientIdGenerator;
    private WebSocketManagerRepository managerRepository;
    private ProtocolHandlerRepository protocolRepository;
    private Set<String> originWhitelist;
    
    public XmlWebSocketServletConfig(WebsocketXml config, 
                                     WebSocketManagerRepository managerRepository, 
                                     Set<String> originWhitelist) {
        servletName = config.getServletName();
        dynamicAddressing = config.isDynamicAddressing();
        this.managerRepository = managerRepository;
        this.protocolRepository = managerRepository.getProtocolHandlerRepository();
        this.originWhitelist = originWhitelist;
        if(config.getClientIdGenerator() != null) {
            clientIdGenerator = new XmlClientIdGenerator(config.getClientIdGenerator()).getClientIdGenerator();
        }
    }
    
    
    
    public String getServletName() {
        return servletName;
    }
    
    @Override
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    @Override
    public ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
    }



    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }



    @Override
    public WebSocketManager getWebSocketManager(String url) {
        return managerRepository.getWebSocketManager(url);
    }



    @Override
    public boolean hasProtocol(String protocol, String url) {
        return protocolRepository.hasProtocol(protocol, url);
    }



    @Override
    public boolean isValidOrigin(String origin) {
        if(origin == null) {
            return false;
        }
        if(!originWhitelist.isEmpty()) {
            return originWhitelist.contains(origin);
        }
        return true;
        
    }

}
