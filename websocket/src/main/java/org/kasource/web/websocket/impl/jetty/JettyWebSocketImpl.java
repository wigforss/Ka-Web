package org.kasource.web.websocket.impl.jetty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.kasource.web.websocket.WebSocketImpl;
import org.kasource.web.websocket.WebSocketManager;
import org.kasource.web.websocket.WebSocketManagerRepository;
import org.kasource.web.websocket.util.ClientIdGenerator;


public class JettyWebSocketImpl extends WebSocketServlet implements WebSocketManagerRepository {
    private static final String ATTRIBUTE_PREFIX = WebSocketImpl.class.getPackage().getName() + "."; 
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CLIENT_ID_PARAMETER = "username";
    
   
    private Map<String, JettyWebSocketManager> managers = new ConcurrentHashMap<String, JettyWebSocketManager>();
    private Set<String> orginWhiteList = new HashSet<String>();
    private String clientIdParameter = DEFAULT_CLIENT_ID_PARAMETER;
    private ClientIdGenerator idGeneator;
    private boolean dynamicAddressing;
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        String orgins = getInitParameter("orginWhiteList");
        if(orgins != null && !orgins.isEmpty()) {
            String[] orginArr = orgins.split(",");
            orginWhiteList.addAll(Arrays.asList(orginArr));
        }
        
        String clientId = getInitParameter("clientIdParameter");
        if(clientId != null && !clientId.isEmpty()) {
            clientIdParameter = clientId;
        }
        idGeneator = new ClientIdGenerator(clientIdParameter);
        setDymamicAddressing();
        if(!dynamicAddressing) {
            getWebSocketManager(getServletName());
        }
        
    }

    private void setDymamicAddressing() {
        String dynamic = getInitParameter("dynamicAddressing");
        dynamicAddressing = (dynamic != null && dynamic.toLowerCase().trim().equals("true"));
    }
    
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        String managerName = this.getServletName();
        
        if(dynamicAddressing) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
        JettyWebSocketManager manager = (JettyWebSocketManager) getWebSocketManager(managerName);
        String clientId = idGeneator.getId(request, manager);
         
        return new JettyWebSocketClient(manager, clientId, request.getParameterMap());
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
        if(!orginWhiteList.isEmpty()) {
            return orginWhiteList.contains(origin);
        }
        return true;
    }

    

    
    @Override
    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            JettyWebSocketManager manager = new JettyWebSocketManager();
            managers.put(socketName, manager);
            getServletContext().setAttribute(ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }
}
