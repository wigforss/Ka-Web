
package org.kasource.web.websocket.impl.tomcat;



import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.kasource.web.websocket.bootstrap.WebSocketConfigListener;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.protocol.ProtocolHandlerRepository;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class TomcatWebsocketImpl extends WebSocketServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TomcatWebsocketImpl.class);
   
    private ServletConfigUtil configUtil; 
    private WebSocketConfig webSocketConfig;
    private int outboundByteBufferSize;

    private int outboundCharBufferSize;
    
    
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
        outboundByteBufferSize = configUtil.parseInitParamAsInt("outboundByteBufferSize");
        outboundCharBufferSize = configUtil.parseInitParamAsInt("outboundCharBufferSize");
       
        configUtil.validateMapping(webSocketConfig.isDynamicAddressing());
        if(!webSocketConfig.isDynamicAddressing()) {
            webSocketConfig.getManagerRepository().getWebSocketManager(configUtil.getMaping());
           
        } 
    }

  
    

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        String managerName = configUtil.getMaping();
       
        if(webSocketConfig.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
       
        WebSocketManager manager =   webSocketConfig.getManagerRepository().getWebSocketManager(managerName);
        
        String username = manager.authenticate(request);
     
        
        String id = webSocketConfig.getClientIdGenerator().getId(request, manager);
        TomcatWebSocketClient client = new TomcatWebSocketClient(manager, username, id, request.getParameterMap());
        ProtocolHandlerRepository protocols = webSocketConfig.getProtocolHandlerRepository();
        client.setBinaryProtocol(protocols.getBinaryProtocol(subProtocol, true));
        client.setTextProtocol(protocols.getTextProtocol(subProtocol, true));
     
        
        if (outboundByteBufferSize != 0) {
            client.setOutboundByteBufferSize(outboundByteBufferSize);
        }
        if (outboundCharBufferSize != 0) {
            client.setOutboundCharBufferSize(outboundCharBufferSize);
        }
        return client;
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
        if(!webSocketConfig.getOrginWhitelist().isEmpty()) {
            return webSocketConfig.getOrginWhitelist().contains(origin);
        }
        return true;
    }
    
    protected String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if(webSocketConfig.getProtocolHandlerRepository().hasProtocol(clientProtocol)) {
                return clientProtocol;
            }
        }
        return null;
    }
}
