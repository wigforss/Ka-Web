package org.kasource.web.websocket.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

import org.kasource.web.websocket.config.WebSocketServletConfigImpl;


public  class RegisterWebSocketEventListenerInjectionTarget<T> implements InjectionTarget<T> {
   private Set<Object> listenerCandidates;
   private Set<WebSocketServletConfigImpl> servletConfigs;
   private InjectionTarget<T> injectionTarget;

   
    public RegisterWebSocketEventListenerInjectionTarget(InjectionTarget<T> injectionTarget, Set<Object> listenerCandidates, Set<WebSocketServletConfigImpl> servletConfigs) {
        this.injectionTarget = injectionTarget;
        this.listenerCandidates = listenerCandidates;
        this.servletConfigs = servletConfigs;
    }


    
    @Override
    public void dispose(T instance) {
        injectionTarget.dispose(instance);
        
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        
        return injectionTarget.getInjectionPoints();
    }

    @Override
    public T produce(CreationalContext<T> ctx) {
        return injectionTarget.produce(ctx);
    }

    @Override
    public void inject(T instance, CreationalContext<T> ctx) {
        injectionTarget.inject(instance, ctx);
        
    }

    @Override
    public void postConstruct(T instance) {
        injectionTarget.postConstruct(instance);
        if(instance instanceof WebSocketServletConfigImpl) {
            servletConfigs.add((WebSocketServletConfigImpl) instance);
        } else {
            listenerCandidates.add(instance);
        }
    }

    @Override
    public void preDestroy(T instance) {
        injectionTarget.preDestroy(instance);
       
       
    }
    
   
  
  
    
    
}
