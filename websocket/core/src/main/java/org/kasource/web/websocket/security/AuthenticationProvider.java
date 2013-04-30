package org.kasource.web.websocket.security;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationProvider {
    
    /**
     * Authenticate user request.
     * 
     * @param request Request to authenticate.
     *
     * @return The username of the authenticated user.
     * 
     * @throws AuthenticationException if autentication was not successful.
     */
    public String authenticate(HttpServletRequest request) throws AuthenticationException;
    
    
}
