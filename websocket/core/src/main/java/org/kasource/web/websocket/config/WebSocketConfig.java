package org.kasource.web.websocket.config;


import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.register.WebSocketListenerRegister;


public interface WebSocketConfig {
 
    public Set<String> getOriginWhitelist();
    
    public WebSocketServletConfig getServletConfig(String servletName);
    
    public ProtocolHandlerRepository getProtocolHandlerRepository();
    
    public WebSocketManagerRepository getManagerRepository();
    
    public WebSocketChannelFactory getChannelFactory();
    
    public WebSocketListenerRegister getListenerRegister();
    
    public void registerServlet(WebSocketServletConfigImpl servlet);
}
