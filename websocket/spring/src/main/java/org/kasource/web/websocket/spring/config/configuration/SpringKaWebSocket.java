package org.kasource.web.websocket.spring.config.configuration;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.spring.channel.SpringWebSocketChannelFactory;
import org.kasource.web.websocket.spring.config.KaWebSocketBean;
import org.kasource.web.websocket.spring.config.SpringWebSocketConfigFactoryBean;
import org.kasource.web.websocket.spring.config.SpringWebSocketConfigurer;
import org.kasource.web.websocket.spring.manager.WebSocketManagerRepositoryFactoryBean;
import org.kasource.web.websocket.spring.protocol.ProtocolHandlerRepositoryFactoryBean;
import org.kasource.web.websocket.spring.registration.SpringWebSocketListenerRegister;
import org.kasource.web.websocket.spring.registration.WebSocketListenerPostBeanProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.ServletContextAware;

@Configuration
public class SpringKaWebSocket implements ServletContextAware {
    
    private ServletContext servletContext;
    
   
    @Autowired
    private ApplicationContext applicationContext;
    

    @Autowired
    @Bean(name = KaWebSocketBean.CONFIGURER_ID)
    public SpringWebSocketConfigurer getConfigurer(WebSocketConfig config, WebSocketChannelFactory channelFactory) {
        SpringWebSocketConfigurer configurer = new SpringWebSocketConfigurer(config);
        configurer.setServletContext(servletContext);
        configurer.setChannelFactory(channelFactory);
        configurer.configure();
        return configurer;
    }
    
    @Bean(name = KaWebSocketBean.PROTOCOL_REPO_ID)
    public ProtocolHandlerRepository getProtocolHandlerRepository() throws Exception {
        ProtocolHandlerRepositoryFactoryBean factory = new ProtocolHandlerRepositoryFactoryBean();
        factory.setApplicationContext(applicationContext);
        return factory.getObject();
    }
    
    @Bean(name = KaWebSocketBean.MANAGER_REPO_ID)
    public WebSocketManagerRepository getWebSocketManagerRepository() throws Exception {
        WebSocketManagerRepositoryFactoryBean factory = new WebSocketManagerRepositoryFactoryBean();
        factory.setApplicationContext(applicationContext);
        factory.setServletContext(servletContext);
        return factory.getObject();
    }
    
 
    @Bean(name = KaWebSocketBean.CHANNEL_FACTORY_ID)
    public WebSocketChannelFactory getWebSocketChannelFactory() throws Exception {
        SpringWebSocketChannelFactory channelFactory = new SpringWebSocketChannelFactory();
        channelFactory.setServletContext(servletContext);
        channelFactory.afterPropertiesSet();
        return channelFactory;
    }
    
    @Bean(name = KaWebSocketBean.LISTENER_REGISTER_ID)
    public WebSocketListenerRegister getWebSocketListenerRegister() throws Exception {
        SpringWebSocketListenerRegister listenerRegister = new SpringWebSocketListenerRegister();
        listenerRegister.setServletContext(servletContext);
        listenerRegister.afterPropertiesSet();
        return listenerRegister;
    }

    @Autowired
    @Bean(name = KaWebSocketBean.POST_BEAN_PROCESSOR_ID)
    @DependsOn(KaWebSocketBean.CHANNEL_FACTORY_ID)
    public WebSocketListenerPostBeanProcessor getWebSocketListenerPostBeanProcessor(WebSocketListenerRegister register) throws Exception {
        WebSocketListenerPostBeanProcessor processor = new WebSocketListenerPostBeanProcessor();
        processor.setServletContext(servletContext);
        processor.setListenerRegister(register);
        processor.afterPropertiesSet();
        return processor;
    }
    
    @Autowired
    @Bean(name = KaWebSocketBean.CONFIG_ID)
    public WebSocketConfig getWebSocketConfig(WebSocketChannelFactory channelFactory,
                                              WebSocketManagerRepository managerRepository,
                                              ProtocolHandlerRepository protocolHandlerRepository) throws Exception {
        SpringWebSocketConfigFactoryBean factory = new SpringWebSocketConfigFactoryBean();
        factory.setApplicationContext(applicationContext);
        factory.setChannelFactory(channelFactory);
        factory.setManagerRepository(managerRepository);
        factory.setProtocolHandlerRepository(protocolHandlerRepository);
        try {
            ClientIdGenerator idGenerator = applicationContext.getBean(ClientIdGenerator.class);
            factory.setClientIdGenerator(idGenerator);
        } catch (Exception e) {
            
        }
        return factory.getObject();
    }
    
    @Autowired
    @Bean(name = "ServletConfig")
    @Scope("prototype")
    public WebSocketServletConfigImpl getWebSocketServletConfig(WebSocketManagerRepository managerRepository,
                                                                ProtocolHandlerRepository protocolHandlerRepository) {
        WebSocketServletConfigImpl servletConfig = new WebSocketServletConfigImpl();
        servletConfig.setManagerRepository(managerRepository);
        servletConfig.setProtocolRepository(protocolHandlerRepository);
        try {
            OriginWhiteListConfig originWhiteListConfig = applicationContext.getBean(OriginWhiteListConfig.class);
            servletConfig.setOriginWhitelist(originWhiteListConfig.getOriginWhiteList());
        } catch (Exception e) {
            
        }
        return servletConfig;
    }
    
    @Override
    public void setServletContext(ServletContext servletContext) {
       this.servletContext = servletContext;
        
    }

    
    
   
}
