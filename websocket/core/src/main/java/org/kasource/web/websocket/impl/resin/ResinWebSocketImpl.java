
package org.kasource.web.websocket.impl.resin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.security.AuthenticationException;
import org.kasource.web.websocket.util.ServletConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;



public class ResinWebSocketImpl extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ResinWebSocketImpl.class);
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
            if(webSocketServletConfig.hasProtocol(clientProtocol, configUtil.getMaping())) {
                return clientProtocol;
            }
        }
        return null;
    }



    private boolean validOrigin(String origin) {
        boolean validOrigin = webSocketServletConfig.isValidOrigin(origin);;
        LOG.warn("Invalid origin: " + origin +" in connection attempt");
        return validOrigin;
    }



    private WebSocketListener createClient(HttpServletRequest request, String protocol) {
        String url = configUtil.getMaping();
        
        if(webSocketServletConfig.isDynamicAddressing()) {
            url = request.getRequestURI().substring(request.getContextPath().length());
        }
        WebSocketManager manager = webSocketServletConfig.getWebSocketManager(url);
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
                                                .subProtocol(protocol)
                                                .build();
        
        ResinWebSocketClient client = new ResinWebSocketClient(clientConfig);
        LOG.info("Client connecion created for " + request.getRemoteHost() + " with username " + username + " and ID " + id + " on " + url);
        return client;
    }

   
    



 

}
