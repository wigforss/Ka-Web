package org.kasource.web.websocket.guice;



import org.kasource.web.websocket.guice.extension.InjectionListenerRegister;
import org.kasource.web.websocket.guice.extension.InjectionTypeListener;
import org.kasource.web.websocket.guice.registration.WebSocketListenerInjectionListener;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class KaWebSocketModule  extends AbstractModule {

    private WebSocketListenerInjectionListener listener;
    
    @Override
    protected void configure() {
        listener = new WebSocketListenerInjectionListener();
        InjectionTypeListener typeListener = new InjectionTypeListener(getInjectionListenerRegister(listener));
       
        bindListener(Matchers.any(), typeListener); 
       
        requestInjection(listener);
        
    }
    
 
    /**
     * Returns the InjectionListenerRegister to use.
     * 
     * @param webSocketListener The websocket listener register.
     * 
     * @return a new InjectionListenerRegister.
     **/
    protected InjectionListenerRegister getInjectionListenerRegister(WebSocketListenerInjectionListener webSocketListenerRegister) {
        InjectionListenerRegister register = new InjectionListenerRegister();
        register.addListener(webSocketListenerRegister);
        return register;
    }
    
    
    
}
