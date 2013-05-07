
package org.kasource.web.websocket.impl.tomcat;



import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class TomcatWebsocketImpl extends WebSocketServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TomcatWebsocketImpl.class);
   
    private ServletConfigUtil configUtil; 
    private WebSocketServletConfig webSocketServletConfig;
 
    private int outboundByteBufferSize;

    private int outboundCharBufferSize;
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        configUtil = new ServletConfigUtil(getServletConfig());
        webSocketServletConfig = configUtil.getConfiguration();
     
        
        outboundByteBufferSize = configUtil.parseInitParamAsInt("outboundByteBufferSize");
        outboundCharBufferSize = configUtil.parseInitParamAsInt("outboundCharBufferSize");
       
        configUtil.validateMapping(webSocketServletConfig.isDynamicAddressing());
        if(!webSocketServletConfig.isDynamicAddressing()) {
            webSocketServletConfig.getWebSocketManager(configUtil.getMaping());
           
        } 
        LOG.info("Initialization completed.");
    }

  
    

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        
            String url = configUtil.getMaping();
       
            if(webSocketServletConfig.isDynamicAddressing()) {
                url = request.getRequestURI().substring(request.getContextPath().length());
            }
       
            WebSocketManager manager =   webSocketServletConfig.getWebSocketManager(url);
            String username = null;
            try {
                 username = manager.authenticate(request);
            } catch (AuthenticationException e) {
                LOG.error("Unauthorized access for " + request.getRemoteHost(), e);
                throw e;
            }
        
            String id = webSocketServletConfig.getClientIdGenerator().getId(request, manager);
            WebSocketClientConfig clientConfig = new WebSocketClientConfig.Builder(manager)
                                                    .url(url)
                                                    .clientId(id)
                                                    .username(username)
                                                    .connectionParams(request.getParameterMap())
                                                    .subProtocol(subProtocol)
                                                    .build();
            TomcatWebSocketClient client = new TomcatWebSocketClient(clientConfig);
   
        
            if (outboundByteBufferSize != 0) {
                client.setOutboundByteBufferSize(outboundByteBufferSize);
            }
            if (outboundCharBufferSize != 0) {
                client.setOutboundCharBufferSize(outboundCharBufferSize);
            }
            LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + id + " on " + url);
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
        boolean validOrigin = webSocketServletConfig.isValidOrigin(origin);;
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
        
    }
    
    protected String selectSubProtocol(List<String> subProtocols) {     
        for (String clientProtocol : subProtocols) {
            if(webSocketServletConfig.hasProtocol(clientProtocol, configUtil.getMaping())) {
                LOG.info("Requested protocol "+ clientProtocol + " found by server");
                return clientProtocol;
            }
        }
        LOG.info("Protocols " + subProtocols + " requested by client, but no such protocols found on server.");
        return null;
    }
}
