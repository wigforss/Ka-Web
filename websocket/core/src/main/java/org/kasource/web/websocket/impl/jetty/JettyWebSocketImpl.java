package org.kasource.web.websocket.impl.jetty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.manager.WebSocketManagerRepository;
import org.kasource.web.websocket.util.ServletWebSocketConfig;


public class JettyWebSocketImpl extends WebSocketServlet implements WebSocketManagerRepository {
   
    private static final long serialVersionUID = 1L;
  
    private ServletWebSocketConfig config; 
   
    private Map<String, JettyWebSocketManager> managers = new ConcurrentHashMap<String, JettyWebSocketManager>();
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        config = new ServletWebSocketConfig(getServletConfig());
        config.validateMapping();
        if(!config.isDynamicAddressing()) {
            getWebSocketManager(config.getMaping());
        }
        
    }

    
    
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
       
        // Only accept protocols configured
        if(protocol != null && !protocol.isEmpty() && !config.getProtocolHandlerFactory().hasProtocol(protocol)) {
            return null;
        }
        String managerName = config.getMaping();
        
        if(config.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
        JettyWebSocketManager manager = (JettyWebSocketManager) getWebSocketManager(managerName);
        String clientId = config.getIdGeneator().getId(request, manager);
        
         
        JettyWebSocketClient client = new JettyWebSocketClient(manager, clientId, request.getParameterMap());
        client.setBinaryProtocol(config.getProtocolHandlerFactory().getBinaryProtocol(protocol, true));
        client.setTextProtocol(config.getProtocolHandlerFactory().getTextProtocol(protocol, true));
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
        if(!config.getOrginWhiteList().isEmpty()) {
            return config.getOrginWhiteList().contains(origin);
        }
        return true;
    }

    

    
    @Override
    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            JettyWebSocketManager manager = new JettyWebSocketManager();
            managers.put(socketName, manager);
            getServletContext().setAttribute(ServletWebSocketConfig.ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }
}
