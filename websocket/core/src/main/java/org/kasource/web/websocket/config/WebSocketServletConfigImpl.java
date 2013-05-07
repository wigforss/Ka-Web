package org.kasource.web.websocket.config;

import java.util.HashSet;
import java.util.Set;

import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;

public class WebSocketServletConfigImpl implements WebSocketServletConfig {
    private String servletName;
    private boolean dynamicAddressing;
    private ClientIdGenerator clientIdGenerator;
    private WebSocketManagerRepository managerRepository;
    private ProtocolHandlerRepository protocolRepository = new ProtocolHandlerRepositoryImpl();
    private Set<String> originWhitelist = new HashSet<String>();
    
    @Override
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }
    
    @Override
    public ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
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
        if(originWhitelist != null && !originWhitelist.isEmpty()) {
            return originWhitelist.contains(origin);
        }
        return true;
    }

    /**
     * @return the servletName
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * @param servletName the servletName to set
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    /**
     * @return the managerRepository
     */
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    /**
     * @param managerRepository the managerRepository to set
     */
    public void setManagerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * @return the protocolRepository
     */
    public ProtocolHandlerRepository getProtocolRepository() {
        return protocolRepository;
    }

    /**
     * @param protocolRepository the protocolRepository to set
     */
    public void setProtocolRepository(ProtocolHandlerRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * @return the originWhitelist
     */
    public Set<String> getOriginWhitelist() {
        return originWhitelist;
    }

    /**
     * @param originWhitelist the originWhitelist to set
     */
    public void setOriginWhitelist(Set<String> originWhitelist) {
        this.originWhitelist = originWhitelist;
    }

    /**
     * @param dynamicAddressing the dynamicAddressing to set
     */
    public void setDynamicAddressing(boolean dynamicAddressing) {
        this.dynamicAddressing = dynamicAddressing;
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }
    
    
}
