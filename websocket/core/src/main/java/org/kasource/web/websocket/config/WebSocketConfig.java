package org.kasource.web.websocket.config;


import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;


public interface WebSocketConfig {
 
    public Set<String> getOrginWhitelist();
    
    public WebSocketServletConfig getServletConfig(String servletName);
    
    public ProtocolHandlerRepository getProtocolHandlerRepository();
    
    public WebSocketManagerRepository getManagerRepository();
    
    public WebSocketChannelFactory getChannelFactory();
}
