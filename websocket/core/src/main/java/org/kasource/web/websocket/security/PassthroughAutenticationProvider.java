package org.kasource.web.websocket.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Authentication provider that does not authenticate a user, but only checks that a username is present.
 * 
 * @author rikardwi
 **/
public class PassthroughAutenticationProvider extends AbstractAuthenticationProvider {
    
    @Override
    public String authenticate(HttpServletRequest request) throws AuthenticationException {
        String username = getUsername(request);
       
        if(username == null) {
            throw new AuthenticationException("No username found. Request needs to include "+(isHeaderBased() ? "header" : "parameter" + " named " + getUsernameKey()), username);
        }
        return username;
    }

  

}
