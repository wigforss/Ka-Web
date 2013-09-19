package org.kasource.web.websocket.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;

import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;

public class WebSocketConfigImpl implements WebSocketConfig {
   
    
    private Set<String> originWhitelist = new HashSet<String>();
    
    private ProtocolHandlerRepository protocolHandlerRepository;
    
    private WebSocketManagerRepository managerRepository;
    
    private WebSocketChannelFactory channelFactory;

    private Map<String, WebSocketServletConfig> servletConfigs = new HashMap<String, WebSocketServletConfig>();
   
    private WebSocketListenerRegister listenerRegister;
    
    private ClientIdGenerator clientIdGenerator;
    
    public void registerServlet(WebSocketServletConfigImpl servlet) {
       
        if(getOriginWhitelist() != null) {
            servlet.setOriginWhitelist(getOriginWhitelist());
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
    
    /**
     * @return the orginWhitelist
     */
    @Override
    public Set<String> getOriginWhitelist(){
        return originWhitelist;
    }

    /**
     * @param orginWhitelist the orginWhitelist to set
     */
    public void setOriginWhitelist(Set<String> orginWhitelist) {
        this.originWhitelist = orginWhitelist;
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

    /**
     * @return the listenerRegister
     */
    @Override
    public WebSocketListenerRegister getListenerRegister() {
        return listenerRegister;
    }

    /**
     * @param listenerRegister the listenerRegister to set
     */
    public void setListenerRegister(WebSocketListenerRegister listenerRegister) {
        this.listenerRegister = listenerRegister;
    }

    /**
     * @return the clientIdGenerator
     */
    protected ClientIdGenerator getClientIdGenerator() {
        return clientIdGenerator;
    }

    
  
}
