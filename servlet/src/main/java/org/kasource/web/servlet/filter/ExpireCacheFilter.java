package org.kasource.web.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class ExpireCacheFilter implements Filter {

    private static final String DEFAULT_CACHE_TIMEOUT_SECONDS = "1";
    private String cacheTimeout = DEFAULT_CACHE_TIMEOUT_SECONDS;
    private long cacheTimeoutMs = 1000;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cacheTimeoutSeconds = filterConfig.getInitParameter("cacheTimeoutSeconds"); 
        if(cacheTimeoutSeconds != null && !cacheTimeoutSeconds.isEmpty()){
            try {
                 cacheTimeoutMs = Long.parseLong(cacheTimeoutSeconds) * 1000L;
                 cacheTimeout = cacheTimeoutSeconds;
            } catch (NumberFormatException nfe) {
                throw new ServletException("cacheTimeoutSeconds: " + cacheTimeoutSeconds + " is not an integer value", nfe);
            }
        } 
            
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if(response instanceof HttpServletResponse) {        
            setHeaders((HttpServletResponse) response);
        }
        chain.doFilter(request, response);
        
    }

    
    /**
     * Set Cache headers.
     * 
     * @param response The HttpResponse to set headers on
     **/
    private void setHeaders(HttpServletResponse response) {
        setHeader(response, "Expires", Long.valueOf(System.currentTimeMillis() + cacheTimeoutMs).toString()); 
        setHeader(response, "Cache-Control", "public, must-revalidate, max-age=" + cacheTimeout); // HTTP 1.1.
        setHeader(response, "Pragma", "public"); 
    }
    
    /**
     * Set Cache header.
     * 
     * @param response The HttpResponse to set header on
     * @param name     Name of the header to set
     * @param value    Value of the header to set.
     **/
    private void setHeader(HttpServletResponse response, String name, String value) {
        if(response.containsHeader(value)) {
              response.setHeader(name, value);
        } else {
            response.addHeader(name, value);
        }
    }
    
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}
