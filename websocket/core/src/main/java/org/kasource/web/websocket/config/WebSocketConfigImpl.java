package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;

import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;

public class WebSocketConfigImpl implements WebSocketConfig {
   
    
    private Set<String> orginWhitelist = new HashSet<String>();
    
    private ProtocolHandlerRepository protocolHandlerRepository;
    
    private WebSocketManagerRepository managerRepository;
    
    private WebSocketChannelFactory channelFactory;

    private Map<String, WebSocketServletConfig> servletConfigs = new HashMap<String, WebSocketServletConfig>();
   

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

    /**
     * @return the servletConfig
     */
    @Override
    public WebSocketServletConfig getServletConfig(String servletName) {
        return servletConfigs.get(servletName);
    }

    /**
     * @return the servletConfigs
     */
    public Map<String, WebSocketServletConfig> getServletConfigs() {
        return servletConfigs;
    }

    /**
     * @param servletConfigs the servletConfigs to set
     */
    public void setServletConfigs(Map<String, WebSocketServletConfig> servletConfigs) {
        this.servletConfigs = servletConfigs;
    }

    
  
}
