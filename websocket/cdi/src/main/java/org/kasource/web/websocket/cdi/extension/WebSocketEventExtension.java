package org.kasource.web.websocket.cdi.extension;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.servlet.ServletContext;

import org.kasource.web.websocket.register.WebSocketListenerRegister;
import org.kasource.web.websocket.register.WebSocketListenerRegisterImpl;


/**
 * Can observe
 * 
 * BeforeBeanDiscovery
 * ProcessAnnotatedType
 * ProcessInjectionTarget and ProcessProducer
 * ProcessBean and ProcessObserverMethod 
 * AfterBeanDiscovery
 * AfterDeploymentValidation
 *  
 * @author rikardwi
 **/
public class WebSocketEventExtension implements Extension {
    
    private Set<Object> listenerCandidates = new HashSet<Object>();
    
    
    /**
     * Handle all injections
     * 
     * @param pit ProcessInjectionTarget to inspect.
     **/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void processInjectorTarger(@Observes ProcessInjectionTarget<Object> pit) {
         pit.setInjectionTarget(new RegisterWebSocketEventListenerInjectionTarget(pit.getInjectionTarget(), listenerCandidates));         
       
    }

    
   

    /**
     * Invoked when a initialized ServletContext has been published.
     * 
     * @param servletContext ServletContext to use.
     **/
    public void onServletContextInitialized(@Observes ServletContext servletContext) {
        WebSocketListenerRegister listenerRegister = new WebSocketListenerRegisterImpl(servletContext);
        for(Object listenerCandidate : listenerCandidates) {
            listenerRegister.registerListener(listenerCandidate);
        }
    }
    

    
}
