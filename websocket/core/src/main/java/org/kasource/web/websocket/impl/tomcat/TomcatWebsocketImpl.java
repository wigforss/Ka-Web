
package org.kasource.web.websocket.impl.tomcat;



import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.protocol.ProtocolHandlerFactory;
import org.kasource.web.websocket.util.ServletWebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class TomcatWebsocketImpl extends WebSocketServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TomcatWebsocketImpl.class);
   
    private ServletWebSocketConfig config; 
   
    private int outboundByteBufferSize;

    private int outboundCharBufferSize;
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        config = new ServletWebSocketConfig(getServletConfig());
      
        outboundByteBufferSize = config.parseInitParamAsInt("outboundByteBufferSize");
        outboundCharBufferSize = config.parseInitParamAsInt("outboundCharBufferSize");
       
        config.validateMapping();
        if(!config.isDynamicAddressing()) {
            config.getManagerRepository().getWebSocketManager(config.getMaping());
           
        } 
    }

  
    

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        String managerName = config.getMaping();
       
        if(config.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
       
        WebSocketManager manager =   config.getManagerRepository().getWebSocketManager(managerName);
        
        String username = manager.authenticate(request);
     
        
        String id = config.getIdGeneator().getId(request, manager);
        TomcatWebSocketClient client = new TomcatWebSocketClient(manager, username, id, request.getParameterMap());
        ProtocolHandlerFactory protocols = config.getProtocolHandlerFactory();
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
        if(!config.getOrginWhiteList().isEmpty()) {
            return config.getOrginWhiteList().contains(origin);
        }
        return true;
    }
    
    protected String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if(config.getProtocolHandlerFactory().hasProtocol(clientProtocol)) {
                return clientProtocol;
            }
        }
        return null;
    }
}