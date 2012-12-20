
package org.kasource.web.websocket.spring;




import javax.servlet.ServletContext;

import org.kasource.web.websocket.WebSocketFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;


public class SpringWebSocketFactory extends WebSocketFactoryImpl implements ServletContextAware, InitializingBean {
    
    

    /**
     * Set the servlet context to use.
     **/
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        
    }



    /**
     * On initialization, create all web sockets currently available in
     * the servlet context.
     **/
    @Override
    public void afterPropertiesSet() throws Exception {
        initialize(servletContext);
        
    }




   



}
