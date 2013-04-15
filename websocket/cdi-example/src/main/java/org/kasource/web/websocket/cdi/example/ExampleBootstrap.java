package org.kasource.web.websocket.cdi.example;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.cdi.event.AnnotationWebsocketBinding;
import org.kasource.web.websocket.cdi.event.CdiWebSocketMapping;
import org.kasource.web.websocket.cdi.event.Configured;


@ApplicationScoped
public class ExampleBootstrap {
    
    /**
     * Invoked when a initialized ServletContext has been published. 
     * Makes sure this instance is created at startup
     * 
     * @param servletContext ServletContext 
     **/
    public void initialize(@Observes ServletContext servletContext) {      
    }
    
    /** Make sure the chat server is created **/
    @SuppressWarnings("unused")
    @Inject
    private ChatServer chatServer;
    
    
     
    /**
     * Maps context path "/chat" to @Chat.
     * 
     * @return path to annotation mapping for example.
     **/
    @Produces @ApplicationScoped @Configured
    public AnnotationWebsocketBinding getCdiWebSocketMapping() {
        return new CdiWebSocketMapping.Builder().map("/chat", Chat.class).build();
    }
}
