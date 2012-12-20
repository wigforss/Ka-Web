package org.kasource.web.servlet.filter;

import java.io.IOException;

import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A filter that sets no cache headers on all requests 
 * 
 * Optionally a regular expression can be set to match the query string. If this expression
 * is set, only request with query strings that matches the expression will ba handled.
 * 
 * 
 * @author rikardwi
 **/
public class NoCacheFilter implements Filter {

    private String regExpFilters;
    
    /**
     * Read the regular expression parameter.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         regExpFilters = filterConfig.getInitParameter("queryStringRegExpFilter");   
    }

    /**
     * Add no-cache headers if Query String matches reqExoFilters 
     **/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        
        if(response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String queryString = ((HttpServletRequest) request).getQueryString();
            if(queryString == null) {
                queryString = "";
            }
            if(regExpFilters == null || regExpFilters.isEmpty()) {
                setHeaders(httpResponse);
            } else {       
                if(Pattern.matches(regExpFilters, queryString)) {
                    setHeaders(httpResponse);
                }
            }
                   
        }
        chain.doFilter(request, response);
        
    }

    /**
     * Set Cache headers.
     * 
     * @param response The HttpResponse to set headers on
     **/
    private void setHeaders(HttpServletResponse response) {
        setHeader(response, "Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        setHeader(response, "Pragma", "no-cache"); // HTTP 1.0.
        setHeader(response, "Expires", "0"); // Proxy
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
    
    }

}
