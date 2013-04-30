package org.kasource.web.websocket.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;



public class ServletConfigUtil {
    
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
    
    public void validateMapping(boolean useDynamicAddressing) throws ServletException {
        String mapping = getMaping();
        if(useDynamicAddressing && !mapping.endsWith("/*")) {
            throw new ServletException("When using dynamicAddressing: " + servletConfig.getServletName() + " must be mapped in web.xml with a * wildcard, make sure the url-pattern end with /*");
        } else if(!useDynamicAddressing  && mapping.endsWith("/*")){
            throw new ServletException(servletConfig.getServletName() + " in web.xml may not be mapped with a * wildcard, unless its configured to use dynamicAddressing. Make sure the url-pattern does not ends with /*.");
        }
    }
    

    /**
     * Returns the first URL mapped for this servlet.
     * 
     * @return URL mapped for the servlet.
     **/
    public String getMaping() {
        ServletRegistration reg = servletConfig.getServletContext().getServletRegistration(servletConfig.getServletName());
        return reg.getMappings().iterator().next();
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAttributeByClass(Class<T> ofClass) {
        return (T) servletConfig.getServletContext().getAttribute(ofClass.getName());
    }
    
    
    
}
