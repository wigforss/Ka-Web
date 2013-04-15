
package org.kasource.web.websocket.impl.resin;



import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kasource.web.websocket.manager.WebSocketManager;
import org.kasource.web.websocket.util.ServletWebSocketConfig;

import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;



public class ResinWebSocketImpl extends HttpServlet {
   
    private static final long serialVersionUID = 1L;
   
    private Map<String, ResinWebSocketManager> managers = new ConcurrentHashMap<String, ResinWebSocketManager>();
    private ServletWebSocketConfig config;
   


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
            if(config.getProtocolHandlerFactory().hasProtocol(clientProtocol)) {
                return clientProtocol;
            }
        }
        return null;
    }



    private boolean validOrigin(String origin) {
        if (origin == null) {
            return false;
        }
        if (!config.getOrginWhiteList().isEmpty()) {
            return config.getOrginWhiteList().contains(origin);
        }
        return true;
    }



    private WebSocketListener createClient(HttpServletRequest request, String protocol) {
        String managerName = config.getMaping();
        
        if(config.isDynamicAddressing()) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
        ResinWebSocketManager manager = (ResinWebSocketManager) getWebSocketManager(managerName);
        String id = config.getIdGeneator().getId(request, manager);
        ResinWebSocketClient client = new ResinWebSocketClient(manager, id, request.getParameterMap());
        client.setBinaryProtocol(config.getProtocolHandlerFactory().getBinaryProtocol(protocol, true));
        client.setTextProtocol(config.getProtocolHandlerFactory().getTextProtocol(protocol, true));
        return client;
    }

    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            ResinWebSocketManager manager = new ResinWebSocketManager();
            managers.put(socketName, manager);
            getServletContext().setAttribute(ServletWebSocketConfig.ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }
    



 

}
