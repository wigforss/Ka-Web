package org.kasource.web.websocket.config;


import java.util.Set;

import org.kasource.web.websocket.channel.WebSocketChannelFactory;
import org.kasource.web.websocket.client.id.ClientIdGenerator;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;


public interface WebSocketConfig {
 
    public Set<String> getOrginWhitelist();
    
    public boolean isDynamicAddressing();
    
    public ClientIdGenerator getClientIdGenerator();
    
    public ProtocolHandlerRepository getProtocolHandlerRepository();
    
    public WebSocketManagerRepository getManagerRepository();
    
    public WebSocketChannelFactory getChannelFactory();
}
