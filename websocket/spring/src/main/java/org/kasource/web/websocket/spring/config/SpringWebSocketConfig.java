package org.kasource.web.websocket.spring.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigImpl;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.web.context.ServletContextAware;

public class SpringWebSocketConfig extends WebSocketConfigImpl implements ServletContextAware, ServletContextAttributeListener {

    private ServletContext servletContext;
    
    
    public void initialize() throws BeanCreationException {
      if(getChannelFactory() == null) {
          throw new BeanCreationException("Can not create bean for class " + SpringWebSocketConfig.class.getName() + " channelFactory is not set");
      }
     
      servletContext.setAttribute(WebSocketConfig.class.getName(), this);
      servletContext.addListener(this);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
       this.servletContext = servletContext;
        
    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        getChannelFactory().addWebSocketManagerFromAttribute(event.getName(), event.getValue());
        
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
       
        
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
     
        
    }

}
