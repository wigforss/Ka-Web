package org.kasource.web.websocket.spring.impl.glassfish;


import javax.servlet.ServletContext;

import org.kasource.web.websocket.impl.glassfish.GlassFishWebSocket;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;




public class SpringGlassFishWebSocket extends GlassFishWebSocket implements InitializingBean, ServletContextAware {

    
    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }


    @Override
    public void setServletContext(ServletContext servletContext) {
        setServletContext(servletContext);
        
    }
    


}
