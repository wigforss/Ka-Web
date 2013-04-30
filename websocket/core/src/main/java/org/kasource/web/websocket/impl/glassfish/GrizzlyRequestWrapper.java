package org.kasource.web.websocket.impl.glassfish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.util.http.Parameters;
import com.sun.grizzly.util.http.ServerCookie;

/**
 * A Grizzly HttpServletRequest which implements parameter, header and cookie data getters. 
 * 
 * This wrapper throws IllegalStateException if an unsupported method is invoked.
 * 
 * @author rikardwi
 **/
public class GrizzlyRequestWrapper implements HttpServletRequest {
    
    private Request request;
    
    public GrizzlyRequestWrapper(Request request) {
        this.request = request;
    }

    @Override
    public String getProtocol() {
        return request.protocol().getString();
    }

    @Override
    public String getScheme() {
        
        return request.scheme().getString();
    }

    @Override
    public String getServerName() {
        
        return request.serverName().getString();
    }

    @Override
    public int getServerPort() {
      
        return request.getServerPort();
    }

    @Override
    public String getRemoteAddr() {
        return request.getRemoteUser().getString();
    }

    @Override
    public String getRemoteHost() {
        return request.getRemoteUser().getString();
    }

    @Override
    public int getRemotePort() {
        return request.getRemotePort();
    }

    @Override
    public String getLocalAddr() {
        return request.localAddr().getString();
    }

    @Override
    public String getLocalName() {
       return request.localName().getString();
    }

    @Override
    public int getLocalPort() {
       return request.getLocalPort();
       
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        request.setCharacterEncoding(s);   
    }

    @Override
    public String getParameter(String s) {
        return request.getParameters().getParameter(s);
    }

    @Override
    public String[] getParameterValues(String s) {
        return request.getParameters().getParameterValues(s);
    }

    @Override
    public Enumeration<String> getParameterNames() {      
        return request.getParameters().getParameterNames();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> params = new HashMap<String, String[]>();
        Parameters parameters  = request.getParameters();
        Enumeration<String> paramtersNames = parameters.getParameterNames();
        while (paramtersNames.hasMoreElements()) {
            String name = paramtersNames.nextElement();
            params.put(name, parameters.getParameterValues(name));
        }
        return params;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public BufferedReader getReader() throws IOException, IllegalStateException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getCharacterEncoding() {
        
        return request.getCharacterEncoding();
    }

    @Override
    public int getContentLength() {
        return request.getContentLength();
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public Locale getLocale() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean isSecure() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public Object getAttribute(String s) {
        return request.getAttribute(s);
    }

    @Override
    public void setAttribute(String s, Object obj) {
        request.setAttribute(s, obj);
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration<String> getAttributeNames() {
        return new com.sun.grizzly.util.http.Enumerator<String>(request.getAttributes().keySet());
    }

    @Override
    public void removeAttribute(String s) {
        request.getAttributes().remove(s);
        
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getRealPath(String s) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public ServletContext getServletContext() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletrequest, ServletResponse servletresponse)
                throws IllegalStateException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean isAsyncStarted() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean isAsyncSupported() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getMethod() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getRequestURI() {
       
        return request.requestURI().getString();
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getContextPath() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getServletPath() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getPathInfo() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getPathTranslated() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getQueryString() {
       
        return request.queryString().getString();
    }

    @Override
    public String getHeader(String s) {
       
        return request.getHeader(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        Set<String> headers = new HashSet<String>();
        headers.add(request.getHeader(s));
        return new com.sun.grizzly.util.http.Enumerator<String>(headers);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> headerNames = new ArrayList<String>();
        for(int i = 0; i < request.getMimeHeaders().getMaxNumHeaders(); ++i) {
            headerNames.add(request.getMimeHeaders().getName(i).getString());
        }
        return new com.sun.grizzly.util.http.Enumerator<String>(headerNames);
    }

    @Override
    public int getIntHeader(String s) {
        return Integer.valueOf(getHeader(s));
    }

    @Override
    public long getDateHeader(String s) {
       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       try {
        return df.parse(getHeader(s)).getTime();
    } catch (ParseException e) {
       throw new IllegalArgumentException("Could not convert "+getHeader(s)+ " to date");
    }
      
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = new Cookie[request.getCookies().getCookieCount()];
        for(int i = 0; i < request.getCookies().getCookieCount(); ++i) {
            ServerCookie grizzlyCookie = request.getCookies().getCookie(i);
            cookies[i] = new Cookie(grizzlyCookie.getCookieHeaderName(), grizzlyCookie.getValue().getString());
        }
        return cookies;
    }

    @Override
    public HttpSession getSession(boolean create) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public HttpSession getSession() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getRequestedSessionId() {
        return request.getMimeHeaders().getHeader("JSESSIONID");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public String getAuthType() {
        return request.getAuthType().getString();
    }

    @Override
    public String getRemoteUser() {
        
        return request.getRemoteUser().getString();
    }

    @Override
    public boolean isUserInRole(String s) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public boolean authenticate(HttpServletResponse httpservletresponse) throws IOException, ServletException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void login(String s, String s1) throws ServletException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void logout() throws ServletException {
        throw new IllegalStateException("Not supported");
        
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        throw new IllegalStateException("Not supported");
    }
}
