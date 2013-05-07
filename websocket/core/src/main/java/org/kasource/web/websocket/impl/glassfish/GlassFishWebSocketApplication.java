package org.kasource.web.websocket.impl.glassfish;



import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import com.sun.grizzly.websockets.WebSocketListener;


/**
 * GlassFish websocket application.
 * 
 * @author rikardwi
 **/
public class GlassFishWebSocketApplication extends WebSocketApplication  {
    private static final Logger LOG = LoggerFactory.getLogger(GlassFishWebSocketApplication.class);
    private ServletConfigUtil configUtil; 

    private WebSocketServletConfig webSocketServletConfig;
 

    public GlassFishWebSocketApplication(ServletConfig config) throws ServletException {
        this.configUtil = new ServletConfigUtil(config);
        webSocketServletConfig = configUtil.getConfiguration();
    
     
        configUtil.validateMapping(webSocketServletConfig.isDynamicAddressing());
        initialize();
        LOG.info("Initialization completed.");
    }
 
    /**
     * initialize websocket application
     */
    private void initialize() {
        if(!webSocketServletConfig.isDynamicAddressing()) {
            webSocketServletConfig.getWebSocketManager(configUtil.getMaping());
        }
    }
    
    @Override
    public boolean isApplicationRequest(Request request) {
       
        return true;
    }
    
    public WebSocket createSocket(final ProtocolHandler handler, 
            final Request requestPacket,
            final WebSocketListener... listeners) {
        HttpServletRequest request = new GrizzlyRequestWrapper(requestPacket);
        String url = configUtil.getMaping();
       
        if(webSocketServletConfig.isDynamicAddressing()) {
            url = request.getRequestURI().substring(configUtil.getMaping().length());
        }
        
        WebSocketManager manager = webSocketServletConfig.getWebSocketManager(url);
        String username = null;
        try {
             username = manager.authenticate(request);
        } catch (AuthenticationException e) {
            LOG.error("Unauthorized access for " + request.getRemoteHost(), e);
            throw e;
        }
        String clientId = webSocketServletConfig.getClientIdGenerator().getId(request, manager);
        WebSocketClientConfig clientConfig = new WebSocketClientConfig.Builder(manager)
                                                    .url(url)
                                                    .clientId(clientId)
                                                    .username(username)
                                                    .connectionParams(request.getParameterMap())
                                                    .subProtocol(handler.toString())
                                                    .build();
        GlassFishWebSocketClient client = new GlassFishWebSocketClient(handler, listeners, clientConfig);
        LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + clientId + " on " + url);
        return client;
    }
   

}
