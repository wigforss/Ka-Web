package org.kasource.web.websocket.guice.channel;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.channel.WebSocketChannelFactoryImpl;
import org.kasource.web.websocket.listener.WebSocketEventListener;

/**
 * Guice injectable WebSocketChannelFactory.
 * 
 * @author rikardwi
 **/
public class GuiceWebSocketChannelFactory implements WebSocketChannelFactory {

   
    private WebSocketChannelFactory factory;
    
    @Inject
    public GuiceWebSocketChannelFactory(ServletContext servletContext) {
        factory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactory.class.getName());
    }

    @Override
    public WebSocketChannel get(String socketChannelName) {
       return factory.get(socketChannelName);
    }

    @Override
    public void listenTo(String socketChannelName, WebSocketEventListener listener) {
       factory.listenTo(socketChannelName, listener);
        
    }
}
