package org.kasource.web.websocket.config;

import java.util.HashSet;
import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;

public class WebSocketConfigImpl implements WebSocketConfig {
   
    
    private Set<String> orginWhitelist = new HashSet<String>();
    
    private boolean dynamicAddressing;
    
    private ClientIdGenerator clientIdGenerator = new DefaultClientIdGenerator();
    
    private ProtocolHandlerRepository protocolHandlerRepository = new ProtocolHandlerRepositoryImpl(null, null);
    
    private WebSocketManagerRepository managerRepository;
    
    private WebSocketChannelFactory channelFactory;

    
   

    /**
     * @return the orginWhitelist
     */
    @Override
    public Set<String> getOrginWhitelist() {
        return orginWhitelist;
    }

    /**
     * @param orginWhitelist the orginWhitelist to set
     */
    public void setOrginWhitelist(Set<String> orginWhitelist) {
        this.orginWhitelist = orginWhitelist;
    }

    /**
     * @return the dynamicAddressing
     */
    @Override
    public boolean isDynamicAddressing() {
        return dynamicAddressing;
    }

    /**
     * @param dynamicAddressing the dynamicAddressing to set
     */
    public void setDynamicAddressing(boolean dynamicAddressing) {
        this.dynamicAddressing = dynamicAddressing;
    }

    /**
     * @return the clientIdGenerator
     */
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

    /**
     * @return the protocolHandlerRepository
     */
    @Override
    public ProtocolHandlerRepository getProtocolHandlerRepository() {
        return protocolHandlerRepository;
    }

    /**
     * @param protocolHandlerRepository the protocolHandlerRepository to set
     */
    public void setProtocolHandlerRepository(ProtocolHandlerRepository protocolHandlerRepository) {
        this.protocolHandlerRepository = protocolHandlerRepository;
    }

 

    /**
     * @return the channelFactory
     */
    @Override
    public WebSocketChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    public void setChannelFactory(WebSocketChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    /**
     * @return the managerRepository
     */
    @Override
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    /**
     * @param managerRepository the managerRepository to set
     */
    public void setManagerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

  
}
