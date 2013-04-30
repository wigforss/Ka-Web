package org.kasource.web.websocket.bean;

public interface BeanResolver {
    public <T> T getBean(String name, Class<T> ofType);
    
    public <T> T getBean(Class<T> byType);
}
