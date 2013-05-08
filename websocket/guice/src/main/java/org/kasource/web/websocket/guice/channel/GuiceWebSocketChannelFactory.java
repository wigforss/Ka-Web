package org.kasource.web.websocket.guice.channel;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.channel.WebSocketChannelFactoryImpl;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.listener.WebSocketEventListener;

/**
 * Guice injectable WebSocketChannelFactory.
 * 
 * @author rikardwi
 **/
public class GuiceWebSocketChannelFactory extends WebSocketChannelFactoryImpl {

   
  
    
    @Inject
    public GuiceWebSocketChannelFactory(ServletContext servletContext) {
       try {
           initialize(servletContext);
       } catch (Exception e) {
           throw new WebSocketConfigException("Could not create "+ GuiceWebSocketChannelFactory.class.getName(), e);
       }
    }

   
}
