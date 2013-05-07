package org.kasource.web.websocket.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.kasource.web.websocket.bootstrap.WebSocketConfigListener;
import org.kasource.web.websocket.config.WebSocketConfig;
import org.kasource.web.websocket.config.WebSocketServletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletConfigUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServletConfigUtil.class);
    private ServletConfig servletConfig;

    public ServletConfigUtil(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public int parseInitParamAsInt(String param) throws ServletException {
        String paramValue = getInitParameter(param);
        try {
            if (paramValue != null && !paramValue.isEmpty()) {
                return Integer.parseInt(paramValue);
            }
        } catch (NumberFormatException nfe) {
            throw new ServletException(param + " must be an integer value.", nfe);
        }
        return 0;
    }

    public String getInitParameter(String name) {
        return servletConfig == null ? null : servletConfig.getInitParameter(name);
    }

    public WebSocketServletConfig getConfiguration() throws ServletException {
        WebSocketConfig webSocketConfig = getAttributeByClass(WebSocketConfig.class);
        if (webSocketConfig == null || webSocketConfig.getServletConfig(servletConfig.getServletName()) == null) {
            String errorMessage = "Could not loacate websocket configuration as ServletContext attribute, make sure to configure "
                        + WebSocketConfigListener.class
                        + " as listener in web.xml or use the Spring, Guice or CDI extension.";
            if (webSocketConfig != null) {
                errorMessage = "Could not find a websocket configuration for servlet: "
                            + servletConfig.getServletName() + " in the websocket configuration.";
            }
            ServletException ex = new ServletException(errorMessage);
            LOG.error(errorMessage, ex);
            throw ex;
        }
        return webSocketConfig.getServletConfig(servletConfig.getServletName());
    }

    public void validateMapping(boolean useDynamicAddressing) throws ServletException {
        String mapping = getMaping();
        if (useDynamicAddressing && !mapping.endsWith("/*")) {
            throw new ServletException("When using dynamicAddressing: " + servletConfig.getServletName()
                        + " must be mapped in web.xml with a * wildcard, make sure the url-pattern end with /*");
        } else if (!useDynamicAddressing && mapping.endsWith("/*")) {
            throw new ServletException(
                        servletConfig.getServletName()
                                    + " in web.xml may not be mapped with a * wildcard, unless its configured to use dynamicAddressing. Make sure the url-pattern does not ends with /*.");
        }
    }

    /**
     * Returns the first URL mapped for this servlet.
     * 
     * @return URL mapped for the servlet.
     **/
    public String getMaping() {
        ServletRegistration reg = servletConfig.getServletContext().getServletRegistration(
                    servletConfig.getServletName());
        return reg.getMappings().iterator().next();
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttributeByClass(Class<T> ofClass) {
        return (T) servletConfig.getServletContext().getAttribute(ofClass.getName());
    }

}
