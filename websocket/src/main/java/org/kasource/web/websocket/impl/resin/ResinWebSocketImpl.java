
package org.kasource.web.websocket.impl.resin;



import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kasource.web.websocket.WebSocketImpl;
import org.kasource.web.websocket.WebSocketManager;
import org.kasource.web.websocket.util.ClientIdGenerator;

import com.caucho.websocket.WebSocketListener;
import com.caucho.websocket.WebSocketServletRequest;



public class ResinWebSocketImpl extends HttpServlet {
    private static final String ATTRIBUTE_PREFIX = WebSocketImpl.class.getPackage().getName() + "."; 
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CLIENT_ID_PARAMETER = "username";
    private Map<String, ResinWebSocketManager> managers = new ConcurrentHashMap<String, ResinWebSocketManager>();
  
    private Set<String> orginWhiteList = new HashSet<String>();
    private String clientIdParameter = DEFAULT_CLIENT_ID_PARAMETER;
    private boolean dynamicAddressing;
    private ClientIdGenerator idGeneator;


    @Override
    public void init() throws ServletException {
        super.init();
        String orgins = getInitParameter("orginWhiteList");
        if (orgins != null && !orgins.isEmpty()) {
            String[] orginArr = orgins.split(",");
            orginWhiteList.addAll(Arrays.asList(orginArr));
        }

        String clientId = getInitParameter("clientIdParameter");
        if (clientId != null && !clientId.isEmpty()) {
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

        wsReq.startWebSocket(createClient(req));
    }



    private String selectSubProtocol(String[] subProtocols) {
        return null;
    }



    private boolean validOrigin(String origin) {
        if (origin == null) {
            return false;
        }
        if (!orginWhiteList.isEmpty()) {
            return orginWhiteList.contains(origin);
        }
        return true;
    }



    private WebSocketListener createClient(HttpServletRequest request) {
        String managerName = this.getServletName();
        
        if(dynamicAddressing) {
            managerName = request.getRequestURI().substring(request.getContextPath().length());
        }
        ResinWebSocketManager manager = (ResinWebSocketManager) getWebSocketManager(managerName);
        String id = idGeneator.getId(request, manager);
        return new ResinWebSocketClient(manager, id, request.getParameterMap());
    }

    public WebSocketManager getWebSocketManager(String socketName) {
        if (!managers.containsKey(socketName)) {
            ResinWebSocketManager manager = new ResinWebSocketManager();
            managers.put(socketName, manager);
            getServletContext().setAttribute(ATTRIBUTE_PREFIX + socketName, manager);
            return manager;
        }
        return managers.get(socketName);
    }
    



 

}
