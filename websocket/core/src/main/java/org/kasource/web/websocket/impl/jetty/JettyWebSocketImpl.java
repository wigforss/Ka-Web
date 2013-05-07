package org.kasource.web.websocket.impl.jetty;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JettyWebSocketImpl extends WebSocketServlet {
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketImpl.class);
    private static final long serialVersionUID = 1L;
  
    
    private ServletConfigUtil configUtil; 
 
    private WebSocketServletConfig webSocketServletConfig;
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        webSocketServletConfig = configUtil.getConfiguration();
       
        
        configUtil.validateMapping(webSocketServletConfig.isDynamicAddressing());
        if(!webSocketServletConfig.isDynamicAddressing()) {
            webSocketServletConfig.getWebSocketManager(configUtil.getMaping());
        }
        LOG.info("Initialization completed.");
    }

    
    
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        String url = configUtil.getMaping();
        
        // Only accept protocols configured
        if(protocol != null && !protocol.isEmpty() && !webSocketServletConfig.hasProtocol(protocol, url)) {
            return null;
        }
        
        
        if(webSocketServletConfig.isDynamicAddressing()) {
            url = request.getRequestURI().substring(request.getContextPath().length());
        }
        WebSocketManager manager =  webSocketServletConfig.getWebSocketManager(url);
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
                                                    .subProtocol(protocol)
                                                    .build();
         
        JettyWebSocketClient client = new JettyWebSocketClient(clientConfig);
        LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + clientId + " on " + url);
        return client;
    }


    
    /**
     * 
     * Example: The origin white list for the example.com Web server could be the strings 
     * "http://example.com", "https://example.com", "http://www.example.com", and "https://www.example.com". 
     *
     * @param origin    The value of the origin header from the request which
     *                  may be <code>null</code>
     *
     * @return  <code>true</code> to accept the request. <code>false</code> to
     *          reject it. This default implementation always returns
     *          <code>true</code>.
     */
    @Override
    public boolean checkOrigin(HttpServletRequest request, String origin) {
        boolean validOrigin = webSocketServletConfig.isValidOrigin(origin);;
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
    }

   
}
