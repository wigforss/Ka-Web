package org.kasource.web.websocket.guice;



import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.config.AuthenticationConfig;
import org.kasource.web.websocket.config.BinaryProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.OriginWhiteListConfig;
import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.TextProtocolHandlerConfigImpl;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;
import org.kasource.web.websocket.guice.channel.GuiceWebSocketChannelFactory;
import org.kasource.web.websocket.guice.config.GuiceKaWebSocketConfigurer;
import org.kasource.web.websocket.guice.extension.InjectionListenerRegister;
import org.kasource.web.websocket.guice.extension.InjectionTypeListener;
import org.kasource.web.websocket.guice.registration.WebSocketListenerInjectionListener;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.manager.WebSocketManagerRepositoryImpl;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepositoryImpl;
import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

public class KaWebSocketModule  extends AbstractModule {

    private WebSocketListenerInjectionListener listener;
    private GuiceKaWebSocketConfigurer configurer;
    
    @Override
    protected void configure() {
        listener = new WebSocketListenerInjectionListener();
        configurer = new GuiceKaWebSocketConfigurer();
        
        InjectionTypeListener typeListener = new InjectionTypeListener(getInjectionListenerRegister(listener));
        bind(WebSocketChannelFactory.class).to(GuiceWebSocketChannelFactory.class);
        bindListener(Matchers.any(), typeListener); 
       
        requestInjection(listener);
        requestInjection(configurer);
    }
    
 
    /**
     * Returns the InjectionListenerRegister to use.
     * 
     * @param webSocketListener The websocket listener register.
     * 
     * @return a new InjectionListenerRegister.
     **/
    protected InjectionListenerRegister getInjectionListenerRegister(WebSocketListenerInjectionListener webSocketListenerRegister) {
        InjectionListenerRegister register = new InjectionListenerRegister();
        register.addListener(webSocketListenerRegister);
        return register;
    }
    
    @Provides @Singleton
    ProtocolHandlerRepository getProtocolHandlerRepository(Injector injector) {
        ProtocolHandlerConfig<String> textHandler = null;
        ProtocolHandlerConfig<byte[]> binaryHandler = null;
        try {
            textHandler = injector.getInstance(TextProtocolHandlerConfigImpl.class);
        }catch (Exception e) {
            // ignore
        }
        try {
            binaryHandler = injector.getInstance(BinaryProtocolHandlerConfigImpl.class);
        }catch (Exception e) {
            // ignore
        }
        ProtocolHandlerRepositoryImpl handler = new ProtocolHandlerRepositoryImpl();
        handler.setBinaryProtocolHandlerConfig(binaryHandler);
        handler.setTextProtocolHandlerConfig(textHandler);
        return handler;
    }
    
    @Provides @Singleton
    WebSocketManagerRepository getWebSocketManagerRepository(Injector injector, ProtocolHandlerRepository protocolHandlerRepository) {
        ServletContext servletContext = injector.getInstance(ServletContext.class);
        AuthenticationConfig authConfig = null;
        try {
            authConfig = injector.getInstance(AuthenticationConfig.class);
        }catch (Exception e) {
            // Ingore
        }
        
        WebSocketManagerRepositoryImpl managerRepo = new WebSocketManagerRepositoryImpl();
        managerRepo.setServletContext(servletContext);
        managerRepo.setProtocolHandlerRepository(protocolHandlerRepository);
        if(authConfig != null) {
            managerRepo.setDefaultAuthenticationProvider(authConfig.getDefaultAuthenticationProvider());
            managerRepo.setAutenticationProviders(authConfig.getAuthenticationUrlMapping());
        }
        return managerRepo;
    }
    
    @Provides @Singleton
    WebSocketListenerRegister getWebSocketListenerRegister(Injector injector, WebSocketChannelFactory channelFactory) {
        ServletContext servletContext = injector.getInstance(ServletContext.class);
        return new WebSocketListenerRegisterImpl(servletContext);
    }
    
    @Provides @Singleton 
    WebSocketConfig getWebSocketConfig(WebSocketChannelFactory channelFactory,
                WebSocketManagerRepository managerRepository,
                ProtocolHandlerRepository protocolHandlerRepository,
                WebSocketListenerRegister listenerRegister,
                Injector injector) {
        WebSocketConfigImpl config = new WebSocketConfigImpl();
        config.setChannelFactory(channelFactory);
        config.setManagerRepository(managerRepository);
        config.setProtocolHandlerRepository(protocolHandlerRepository);
        config.setListenerRegister(listenerRegister);
        try {
            ClientIdGenerator idGeneraror = injector.getInstance(ClientIdGenerator.class);
            config.setClientIdGenerator(idGeneraror);
        }catch (Exception e) {
            // Ignore
        }
        try {
            OriginWhiteListConfig originWhiteListConfig = injector.getInstance(OriginWhiteListConfig.class);
            config.setOriginWhitelist(originWhiteListConfig.getOriginWhiteList());
            
        } catch (Exception e) {
            
        }
        return config;
    }
    
    @Provides
    public WebSocketServletConfigImpl getWebSocketServletConfig(WebSocketManagerRepository managerRepository,
                ProtocolHandlerRepository protocolHandlerRepository, Injector injector) {
        WebSocketServletConfigImpl servletConfig = new WebSocketServletConfigImpl();
        servletConfig.setManagerRepository(managerRepository);
        servletConfig.setProtocolRepository(protocolHandlerRepository);
        
        return servletConfig;
    }
    
}
