package org.kasource.web.websocket.guice.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;

@Singleton
public class GuiceKaWebSocketConfigurer implements ServletContextAttributeListener {
    private Injector injector;
    private WebSocketChannelFactory channelFactory;
    private WebSocketConfig config;
    private ServletContext servletContext;
    
    
    public  GuiceKaWebSocketConfigurer() { 
    }

    @Inject
    public void initialize(Injector injector, WebSocketConfig config, WebSocketChannelFactory channelFactory) {
        this.injector = injector;
        this.servletContext = injector.getInstance(ServletContext.class);
        this.channelFactory = channelFactory;
        this.config = config;
        configure();
    }
    
    public void configure() {
        
        servletContext.addListener(this);
        servletContext.setAttribute(WebSocketConfig.class.getName(), config);
       
        registerServletConfigs();
    }
    
   
    
    private void registerServletConfigs() {
        for (Key<?> bindingKey : injector.getBindings().keySet()) {
            if (WebSocketServletConfigImpl.class.isAssignableFrom(bindingKey.getTypeLiteral().getRawType())) {
                WebSocketServletConfigImpl servletConfig = (WebSocketServletConfigImpl) injector.getInstance(bindingKey);
                if(servletConfig.getServletName() != null) {
                    config.registerServlet(servletConfig);
                }
            }
            
        }
    }
    
    
    
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        channelFactory.addWebSocketManagerFromAttribute(event.getName(), event.getValue());
        
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
      
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
       
        
    }
    
    
}
