package org.kasource.web.websocket.impl.glassfish;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.sun.grizzly.websockets.WebSocketEngine;


/**
 * GlassFish WebSocket Implementation
 * <p> 
 * To enable websockets in GlassFish use:
 * {@code
 * asadmin set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.websockets-support-enabled=true
 * }
 * @author rikardwi
 **/
public class GlassFishWebSocketImpl extends HttpServlet {

    private static final long serialVersionUID = 1L;

   
    
    @Override
    public void init() throws ServletException {
        super.init();
      
       
        GlassFishWebSocketApplication app = new GlassFishWebSocketApplication(getServletConfig());
        WebSocketEngine.getEngine().register(app);
        
       
    }
}
