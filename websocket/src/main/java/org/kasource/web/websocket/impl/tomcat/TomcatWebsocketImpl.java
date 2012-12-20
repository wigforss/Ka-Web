
package org.kasource.web.websocket.impl.tomcat;



import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.kasource.web.websocket.WebSocketImpl;
import org.kasource.web.websocket.WebSocketManager;
import org.kasource.web.websocket.WebSocketManagerRepository;
import org.kasource.web.websocket.util.ClientIdGenerator;




public class TomcatWebsocketImpl extends WebSocketServlet implements WebSocketManagerRepository {
    private static final String ATTRIBUTE_PREFIX = WebSocketImpl.class.getPackage().getName()+"."; 
    private static final long serialVersionUID = 1L;

    private Map<String, TomcatWebSocketManager> managers = new ConcurrentHashMap<String, TomcatWebSocketManager>();
    
    private static final String DEFAULT_CLIENT_ID_PARAMETER = "username";
    private int outboundByteBufferSize;

    private int outboundCharBufferSize;
    
    private Set<String> orginWhiteList = new HashSet<String>();

    private String clientIdParameter = DEFAULT_CLIENT_ID_PARAMETER;
    private ClientIdGenerator idGeneator;
    private boolean dynamicAddressing;
 
    public TomcatWebsocketImpl() {
        System.out.println("TomcatWebsocketImpl started");
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
     
        
        outboundByteBufferSize = parseInitParamAsInt("outboundByteBufferSize");
        outboundCharBufferSize = parseInitParamAsInt("outboundCharBufferSize");
        
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


    private int parseInitParamAsInt(String param) throws ServletException {
        String paramValue = getInitParameter(param);
        try {
            if (paramValue != null && !paramValue.isEmpty()) {
                return Integer.parseInt(paramValue);
            }
        } catch (NumberFormatException nfe) {
            throw new ServletException(param + " must be an integer value.", nfe);
        }
        return 0;
    }



    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        String managerName = this.getServletName();
       
        if(dynamicAddressing) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
       
        TomcatWebSocketManager manager = (TomcatWebSocketManager) getWebSocketManager(managerName);
        String id = idGeneator.getId(request, manager);
        TomcatWebSocketClient client = new TomcatWebSocketClient(manager, id, request.getParameterMap());
        if (outboundByteBufferSize != 0) {
            client.setOutboundByteBufferSize(outboundByteBufferSize);
        }
        if (outboundCharBufferSize != 0) {
            client.setOutboundCharBufferSize(outboundCharBufferSize);
        }
        return client;
    }

    @Override
    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            TomcatWebSocketManager manager = new TomcatWebSocketManager();
            managers.put(socketName, manager);
            getServletContext().setAttribute(ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }
   


    /**
     * Intended to be overridden by sub-classes that wish to verify the origin
     * of a WebSocket request before processing it.
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
    protected boolean verifyOrigin(String origin) {
        if(origin == null) {
            return false;
        }
        if(!orginWhiteList.isEmpty()) {
            return orginWhiteList.contains(origin);
        }
        return true;
    }
}
