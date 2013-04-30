
package org.kasource.web.websocket.impl.resin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kasource.web.websocket.bootstrap.WebSocketConfigListener;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;



public class ResinWebSocketImpl extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ResinWebSocketImpl.class);
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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String origin = req.getHeader("Sec-WebSocket-Origin");
        if (!validOrigin(origin)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String protocols = req.getHeader("Sec-WebSocket-Protocol");
        String subProtocol = null;
        if (protocols != null) {
            subProtocol = selectSubProtocol(protocols.split(","));
        }

        if (subProtocol != null) {
            res.setHeader("Sec-WebSocket-Protocol", subProtocol);
        }

        WebSocketServletRequest wsReq = (WebSocketServletRequest) req;

        wsReq.startWebSocket(createClient(req, subProtocol));
    }



    private String selectSubProtocol(String[] subProtocols) {
        for (String clientProtocol : subProtocols) {
            if(webSocketConfig.getProtocolHandlerRepository().hasProtocol(clientProtocol)) {
                return clientProtocol;
            }
        }
        return null;
    }



    private boolean validOrigin(String origin) {
        if (origin == null) {
            return false;
        }
        if (!webSocketConfig.getOrginWhitelist().isEmpty()) {
            return webSocketConfig.getOrginWhitelist().contains(origin);
        }
        return true;
    }



    private WebSocketListener createClient(HttpServletRequest request, String protocol) {
        String managerName = configUtil.getMaping();
        
        if(webSocketConfig.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
        WebSocketManager manager = webSocketConfig.getManagerRepository().getWebSocketManager(managerName);
        String username = manager.authenticate(request);
        String id = webSocketConfig.getClientIdGenerator().getId(request, manager);
        ResinWebSocketClient client = new ResinWebSocketClient(manager, username, id, request.getParameterMap());
        client.setBinaryProtocol(webSocketConfig.getProtocolHandlerRepository().getBinaryProtocol(protocol, true));
        client.setTextProtocol(webSocketConfig.getProtocolHandlerRepository().getTextProtocol(protocol, true));
        return client;
    }

   
    



 

}
