package org.kasource.web.websocket.cdi.channel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.channel.WebSocketChannel;
import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.channel.WebSocketChannelFactoryImpl;
import org.kasource.web.websocket.listener.WebSocketEventListener;

@ApplicationScoped
public class CdiWebSocketChannelFactory implements WebSocketChannelFactory {

    private static CdiWebSocketChannelFactory instance = null;
    
    private WebSocketChannelFactory factory;
    
    
    public static WebSocketChannelFactory getInstance() {
        return instance;
    }
    
    /**
     * Invoked when a initialized ServletContext has been published.
     * 
     * @param servletContext ServletContext to use.
     **/
    public void onServletContextInitialized(@Observes ServletContext servletContext) {
        factory = (WebSocketChannelFactory) servletContext.getAttribute(WebSocketChannelFactoryImpl.class.getName());
        instance = this;
    }

    @Override
    public WebSocketChannel get(String socketChannelName) {
        if(factory != null) {
            return factory.get(socketChannelName);
        } else {
           throw new IllegalStateException("CdiWebSocketChannelFactory has not yet been initialized with a ServletContext");
        }
        
    }

    @Override
    public void listenTo(String socketChannelName, WebSocketEventListener listener) {
        if(factory != null) {
             factory.listenTo(socketChannelName, listener);
        } else {
           throw new IllegalStateException("CdiWebSocketChannelFactory has not yet been initialized with a ServletContext");
        }
    }
}
