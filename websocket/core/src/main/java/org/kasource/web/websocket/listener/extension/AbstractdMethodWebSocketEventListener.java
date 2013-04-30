package org.kasource.web.websocket.listener.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;


public abstract class AbstractdMethodWebSocketEventListener implements ExtendedWebSocketEventListener {

    protected Method method;
    protected Object listener;
    protected Annotation annotation;
    protected Class<?>[] arguments;
    
    @Override
    public void initialize() {
        arguments = getArguments(annotation);
        if(method.isAccessible()) {
            throw new IllegalArgumentException(method + " annotated with " + annotation.annotationType() + "must be public");
        }
        Class<?>[] params = method.getParameterTypes();
        if(params.length != arguments.length) {
            throw new IllegalArgumentException(method + " annotated with " + annotation.annotationType() + " must have " + arguments.length + " parameter(s)");
        }
        
        validateArguments(params);
        
    }
    
    private void validateArguments(Class<?>[] params) {
        for(int i = 0; i < arguments.length; ++i) {      
            Class<?> argument = arguments[i];
            Class<?> methodsParameter = params[i];
            if(!argument.isAssignableFrom(methodsParameter)) {
                throw new IllegalArgumentException(method + "annotated with " + annotation.annotationType() + " does not have to correct parameters, should be " + Arrays.asList(arguments) + "or subclass thereof.");
            }
           
        }
    }

    
    private Class<?>[] getArguments(Annotation annotation) {
        try {
            Method method = annotation.annotationType().getMethod("arguments");
            return (Class<?>[]) method.invoke(annotation);
        } catch (Exception e) {
        } 
       return new Class<?>[] {};
    }

    @Override
    public void setMethod(Method method) {
       this.method = method;
        
    }

    @Override
    public void setListener(Object listener) {
        this.listener = listener;
        
    }

    @Override
    public void setAnnotation(Annotation annotation) {
      this.annotation = annotation;
        
    }

    

}
