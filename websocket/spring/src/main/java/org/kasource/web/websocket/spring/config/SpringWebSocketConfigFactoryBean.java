package org.kasource.web.websocket.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.client.id.DefaultClientIdGenerator;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringWebSocketConfigFactoryBean  implements FactoryBean<WebSocketConfig>, ApplicationContextAware {
  
    
    private ProtocolHandlerRepository protocolHandlerRepository;
    
    private WebSocketManagerRepository managerRepository;
    
    private WebSocketChannelFactory channelFactory;
  
    
    private ApplicationContext applicationContext;
    
    private ClientIdGenerator clientIdGenerator;
    
    @Override
    public WebSocketConfig getObject() throws Exception {
        OriginWhiteListConfig originList =  null;
        try {
             originList = applicationContext.getBean(OriginWhiteListConfig.class);
        } catch (Exception e) {
           if(applicationContext.getBeanNamesForType(OriginWhiteListConfig.class).length > 0) {
               throw e;
           }
        }
        Map<String, WebSocketServletConfigImpl> servletConfigBeans = applicationContext.getBeansOfType(WebSocketServletConfigImpl.class);
        Map<String, WebSocketServletConfig> servletConfigs = new HashMap<String, WebSocketServletConfig>();
        for(WebSocketServletConfigImpl servletConfigBean : servletConfigBeans.values()) {
            if(originList != null && originList.getOriginWhiteList() != null) {
                servletConfigBean.setOriginWhitelist(originList.getOriginWhiteList());
            } 
            if(servletConfigBean.getClientIdGenerator() == null) {
                servletConfigBean.setClientIdGenerator(clientIdGenerator != null ? clientIdGenerator : new DefaultClientIdGenerator());
            }
            servletConfigs.put(servletConfigBean.getServletName(), servletConfigBean);
        }
       
        
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        config.setChannelFactory(channelFactory);
        config.setManagerRepository(managerRepository);
        config.setProtocolHandlerRepository(protocolHandlerRepository);
        if(originList != null && originList.getOriginWhiteList() != null) {
            config.setOrginWhitelist(originList.getOriginWhiteList());
        }
        config.setServletConfigs(servletConfigs);
       
        return config;
    }

    @Override
    public Class<?> getObjectType() {
       
        return WebSocketConfig.class;
    }

    @Override
    public boolean isSingleton() {
   
        return true;
    }

    
    /**
     * @param protocolHandlerRepository the protocolHandlerRepository to set
     */
    @Required
    public void setProtocolHandlerRepository(ProtocolHandlerRepository protocolHandlerRepository) {
        this.protocolHandlerRepository = protocolHandlerRepository;
    }

    /**
     * @param managerRepository the managerRepository to set
     */
    @Required
    public void setManagerRepository(WebSocketManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    @Required
    public void setChannelFactory(WebSocketChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }




    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        
    }

    /**
     * @param clientIdGenerator the clientIdGenerator to set
     */
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }

}
