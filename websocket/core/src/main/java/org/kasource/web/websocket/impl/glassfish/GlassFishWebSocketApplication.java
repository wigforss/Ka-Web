package org.kasource.web.websocket.impl.glassfish;



import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.kasource.web.websocket.bootstrap.WebSocketConfigListener;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
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
    private WebSocketConfig webSocketConfig;

 

    public GlassFishWebSocketApplication(ServletConfig config) throws ServletException {
        this.configUtil = new ServletConfigUtil(config);
        webSocketConfig = configUtil.getAttributeByClass(WebSocketConfig.class);
        
        if(webSocketConfig == null) {         
            ServletException ex = new ServletException("Could not loacate websocket configuration as ServletContext attribute, make sure to configure " 
                        + WebSocketConfigListener.class + " as listener in web.xml or use the Spring, Guice or CDI extension.");
            LOG.error("Could not loacate websocket configuration as ServletContext attribute", ex);
            throw ex;
        }
        
        configUtil.validateMapping(webSocketConfig.isDynamicAddressing());
        initialize();
    }
 
    /**
     * initialize websocket application
     */
    private void initialize() {
        if(!webSocketConfig.isDynamicAddressing()) {
            webSocketConfig.getManagerRepository().getWebSocketManager(configUtil.getMaping());
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
        String managerName = configUtil.getMaping();
        if(webSocketConfig.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(configUtil.getMaping().length());
        }
        WebSocketManager manager = webSocketConfig.getManagerRepository().getWebSocketManager(managerName);
        String username = manager.authenticate(request);
        String clientId = webSocketConfig.getClientIdGenerator().getId(request, manager);
       
        GlassFishWebSocketClient client = new GlassFishWebSocketClient(manager, handler, listeners, username, clientId, request.getParameterMap());
        client.setBinaryProtocol(webSocketConfig.getProtocolHandlerRepository().getBinaryProtocol(handler.toString(), true));
        client.setTextProtocol(webSocketConfig.getProtocolHandlerRepository().getTextProtocol(handler.toString(), true));
        return client;
    }
   

}
