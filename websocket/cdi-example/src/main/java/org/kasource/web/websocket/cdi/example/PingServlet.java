package org.kasource.web.websocket.cdi.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.kasource.web.websocket.channel.WebSocketChannelFactory;


/**
 * Ping Servlet.
 * 
 * Uses the WebSocketChannelFactory to broadcast each ping made.
 * 
 * @author rikardwi
 **/
@WebServlet(name="PingServlet", urlPatterns = "/ping", loadOnStartup = 1)
public class PingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static String PAGE_HEADER = "<html><head /><body>";
    private static String PAGE_FOOTER = "</body></html>";
     
   @Inject
   private WebSocketChannelFactory channelFactory;
   
   @Inject 
   private ExampleConfiguration exampleConfig;
    
 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        channelFactory.get("/chat").broadcast("Ping from " + req.getRemoteHost());
        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        writer.println("<h1>" + "Ping success" + "</h1>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }
    
    
 
}
