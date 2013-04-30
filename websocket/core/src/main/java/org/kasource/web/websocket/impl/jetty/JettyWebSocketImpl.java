package org.kasource.web.websocket.impl.jetty;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.kasource.web.websocket.bootstrap.WebSocketConfigListener;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JettyWebSocketImpl extends WebSocketServlet {
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketImpl.class);
    private static final long serialVersionUID = 1L;
  
    
    private ServletConfigUtil configUtil; 
    private WebSocketConfig webSocketConfig;
   
   
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        webSocketConfig = configUtil.getAttributeByClass(WebSocketConfig.class);
        if(webSocketConfig == null) {         
            ServletException ex = new ServletException("Could not loacate websocket configuration as ServletContext attribute, make sure to configure " 
                        + WebSocketConfigListener.class + " as listener in web.xml or use the Spring, Guice or CDI extension.");
            LOG.error("Could not loacate websocket configuration as ServletContext attribute", ex);
            throw ex;
        }
        configUtil.validateMapping(webSocketConfig.isDynamicAddressing());
        if(!webSocketConfig.isDynamicAddressing()) {
            webSocketConfig.getManagerRepository().getWebSocketManager(configUtil.getMaping());
        }
        
    }

    
    
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
      
        // Only accept protocols configured
        if(protocol != null && !protocol.isEmpty() && !webSocketConfig.getProtocolHandlerRepository().hasProtocol(protocol)) {
            return null;
        }
        String managerName = configUtil.getMaping();
        
        if(webSocketConfig.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
        WebSocketManager manager =  webSocketConfig.getManagerRepository().getWebSocketManager(managerName);
        String username = manager.authenticate(request);
        String clientId = webSocketConfig.getClientIdGenerator().getId(request, manager);
        
         
        JettyWebSocketClient client = new JettyWebSocketClient(manager, username, clientId, request.getParameterMap());
        client.setBinaryProtocol(webSocketConfig.getProtocolHandlerRepository().getBinaryProtocol(protocol, true));
        client.setTextProtocol(webSocketConfig.getProtocolHandlerRepository().getTextProtocol(protocol, true));
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
        if(origin == null) {
            return false;
        }
        if(!webSocketConfig.getOrginWhitelist().isEmpty()) {
            return webSocketConfig.getOrginWhitelist().contains(origin);
        }
        return true;
    }

   
}
