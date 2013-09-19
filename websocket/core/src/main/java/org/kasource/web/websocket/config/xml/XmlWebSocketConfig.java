package org.kasource.web.websocket.config.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.config.xml.jaxb.WebsocketXml;
import org.kasource.web.websocket.config.xml.jaxb.WebsocketXmlConfigRoot;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;

public class XmlWebSocketConfig implements WebSocketConfig {

    private WebsocketXmlConfigRoot config;
    private ClientIdGenerator idGenerator = new DefaultClientIdGenerator();
    private Set<String> originWhitelist = new HashSet<String>();
    private Map<String, WebSocketServletConfig> servletConfigs = new HashMap<String, WebSocketServletConfig>();
    private ProtocolHandlerRepository protocolHandlerRepository;
    private WebSocketManagerRepositoryImpl managerRepository;
    private WebSocketChannelFactory channelFactory;
    private WebSocketListenerRegister listenerRegister;
    public XmlWebSocketConfig(WebsocketXmlConfigRoot config, ServletContext servletContext) {
        this.config = config;
        initialize(servletContext);
    }
    
   
    
    private void initialize(ServletContext servletContext) {
        loadProtocols();
        
        channelFactory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactory.class.getName());
        XmlAuthentication authentication = null;
        if(config.getAuthentication() != null) {
            authentication = new XmlAuthentication(config.getAuthentication());
        }
       
        if(config.getClientIdGenerator() != null) {
            idGenerator = new XmlClientIdGenerator(config.getClientIdGenerator()).getClientIdGenerator();
        }
        
        
        if(config.getOrginWhitelist() != null) {
            originWhitelist.addAll(config.getOrginWhitelist().getOrigin());
        }
        managerRepository = new WebSocketManagerRepositoryImpl(servletContext);
        if(authentication != null) {
            managerRepository.setDefaultAuthenticationProvider(authentication.getAutenticationProvider());
            managerRepository.setAutenticationProviders(authentication.getAuthenticationUrlMapping());
        }
        managerRepository.setProtocolHandlerRepository(protocolHandlerRepository);
        loadServletConfigs(idGenerator);
        listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
    }
    
    private void loadServletConfigs(ClientIdGenerator idGenerator) {
        for(WebsocketXml websocket :config.getWebsocket()) {
            XmlWebSocketServletConfig servletConfig = new XmlWebSocketServletConfig(websocket, managerRepository, originWhitelist);
            if(servletConfig.getClientIdGenerator() == null) {
                servletConfig.setClientIdGenerator(idGenerator);
            }
            servletConfigs.put(servletConfig.getServletName(), servletConfig);
        }
    }

    private void loadProtocols() {
        ProtocolHandlerConfig<String> textProtocolHandlerConfig = null;
        ProtocolHandlerConfig<byte[]> binaryProtocolHandlerConfig = null;
        if(config.getTextProtocolHandler() != null) {
            textProtocolHandlerConfig = new XmlTextProtocolHandlerConfig(config.getTextProtocolHandler());
        }
        
        if(config.getBinaryProtocolHandler() != null) {
            binaryProtocolHandlerConfig = new XmlBinaryProtocolHandlerConfig(config.getBinaryProtocolHandler());
        }
        
        try {
            protocolHandlerRepository = new ProtocolHandlerRepositoryImpl(textProtocolHandlerConfig, binaryProtocolHandlerConfig); 
        } catch (Exception e) {
           throw new WebSocketConfigException("Could not load Protocol Handlers", e);
        }
    }
    
   
    
   

  

    @Override
    public ProtocolHandlerRepository getProtocolHandlerRepository() {
        return protocolHandlerRepository;
    }
    
    @Override
    public Set<String> getOriginWhitelist() {
        
        return originWhitelist;
    }


    @Override
    public WebSocketManagerRepository getManagerRepository() {
        return managerRepository;
    }

    @Override
    public WebSocketChannelFactory getChannelFactory() {
        return channelFactory;
    }

    @Override
    public WebSocketServletConfig getServletConfig(String servletName) {
        return servletConfigs.get(servletName);
    }



    @Override
    public WebSocketListenerRegister getListenerRegister() {
        return listenerRegister;
    }



    @Override
    public void registerServlet(WebSocketServletConfigImpl servlet) {
        
    }

}
