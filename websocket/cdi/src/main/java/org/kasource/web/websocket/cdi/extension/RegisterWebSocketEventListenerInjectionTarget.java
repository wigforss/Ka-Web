package org.kasource.web.websocket.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;


public  class RegisterWebSocketEventListenerInjectionTarget<T> implements InjectionTarget<T> {
   private Set<Object> listenerCandidates;
   
   private InjectionTarget<T> injectionTarget;

   
    public RegisterWebSocketEventListenerInjectionTarget(InjectionTarget<T> injectionTarget, Set<Object> listenerCandidates) {
        this.injectionTarget = injectionTarget;
        this.listenerCandidates = listenerCandidates;
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
        listenerCandidates.add(instance);       
    }

    @Override
    public void preDestroy(T instance) {
        injectionTarget.preDestroy(instance);
       
       
    }
    
   
  
  
    
    
}
