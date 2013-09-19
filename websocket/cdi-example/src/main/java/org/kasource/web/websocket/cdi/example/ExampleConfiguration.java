package org.kasource.web.websocket.cdi.example;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.cdi.event.AnnotationWebsocketBinding;
import org.kasource.web.websocket.cdi.event.CdiWebSocketMapping;
import org.kasource.web.websocket.cdi.event.Configured;
import org.kasource.web.websocket.config.WebSocketServletConfigImpl;


@ApplicationScoped
public class ExampleConfiguration {
    
    /** Make sure the chat servlet config is created **/
    @SuppressWarnings("unused")
    @Inject @Chat
    private WebSocketServletConfigImpl chatServletConfig; 
       
   
    /** Make sure the chat server is created **/
    @SuppressWarnings("unused")
    @Inject
    private ChatServer chatServer;
    
    /**
     * Invoked when a initialized ServletContext has been published. 
     * Makes sure this instance is created at startup
     * 
     * @param servletContext ServletContext 
     **/
    public void initialize(@Observes ServletContext servletContext) {     
        System.out.println(servletContext);
    }
    
     
    /**
     * Maps context path "/chat" to @Chat.
     * 
     * @return path to annotation mapping for example.
     **/
    @Produces @ApplicationScoped @Configured
    public AnnotationWebsocketBinding getCdiWebSocketMapping() {
        return new CdiWebSocketMapping.Builder().map("/chat", Chat.class).build();
    }
    
    @Produces @Named("chat")
    public WebSocketServletConfigImpl getChatServletConfig(WebSocketServletConfigImpl servletConfig) {
        servletConfig.setDynamicAddressing(false);
        servletConfig.setServletName("chat");
        return servletConfig;
    }
}
