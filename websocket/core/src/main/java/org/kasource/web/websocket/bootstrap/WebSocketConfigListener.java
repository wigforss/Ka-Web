package org.kasource.web.websocket.bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kasource.web.websocket.channel.WebSocketChannelFactoryImpl;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;
import org.kasource.web.websocket.config.xml.XmlWebSocketConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstraps the WebSocket Framework.
 * 
 * @author rikardwi
 **/
public class WebSocketConfigListener implements ServletContextListener, ServletContextAttributeListener {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketConfigListener.class);
    private WebSocketChannelFactoryImpl factory;

    /**
     * Listener invoked when a new attribute has been added.
     * 
     * @param attributeEvent
     *            Event with the added attribute.
     **/
    @Override
    public void attributeAdded(ServletContextAttributeEvent attributeEvent) {
        if(factory != null) {
            factory.addWebSocketManagerFromAttribute(attributeEvent.getName(), attributeEvent.getValue());
        }
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scab) {
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scab) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {

            factory = new WebSocketChannelFactoryImpl();
            factory.initialize(event.getServletContext());
            loadAndpublishConfig(event.getServletContext());

        } catch (Exception e) {
            LOG.error("Could not load websocket configuration", e);
            throw new IllegalStateException("Could not initialize " + this.getClass(), e);
        }

    }

    private void loadAndpublishConfig(ServletContext servletContext) {
        String configLocation = servletContext.getInitParameter("webSocketConfigLocation");
        // Validate configLocation
        if (configLocation == null || configLocation.isEmpty()) {
            throw new WebSocketConfigException(
                        "context-param named webSocketConfigLocation needs to be set in web.xml to use listener "
                                    + this.getClass());
        }
        // Load configuration
        XmlWebSocketConfigLoader configLoader = new XmlWebSocketConfigLoader(configLocation, servletContext);
        WebSocketConfig config = configLoader.loadConfig();
        servletContext.setAttribute(WebSocketConfig.class.getName(), config);
    }

}
