package org.kasource.web.websocket.guice.channel;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannelFactoryImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;

import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Guice injectable WebSocketChannelFactory.
 * 
 * @author rikardwi
 **/
@Singleton
public class GuiceWebSocketChannelFactory extends WebSocketChannelFactoryImpl {
    
    @Inject
    public GuiceWebSocketChannelFactory(Injector injector) {
        this.servletContext = injector.getInstance(ServletContext.class);
       try {
           initialize(servletContext);
       } catch (Exception e) {
           throw new WebSocketConfigException("Could not create "+ GuiceWebSocketChannelFactory.class.getName(), e);
       }
    }

   
}
