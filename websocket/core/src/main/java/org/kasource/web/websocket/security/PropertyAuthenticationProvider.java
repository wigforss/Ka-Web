package org.kasource.web.websocket.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class PropertyAuthenticationProvider extends AbstractAuthenticationProvider {
    private Properties props = new Properties();
    
    
    public PropertyAuthenticationProvider(String propertyFileLocation) throws FileNotFoundException, IOException {
        props.load(new FileInputStream(propertyFileLocation));
    }
    
    @Override
    public String authenticate(HttpServletRequest request) throws AuthenticationException {
       String username = getUsername(request);
       String password = getPassword(request);
       if(username == null) {
           throw new AuthenticationException("No username found. Request needs to include "+(isHeaderBased() ? "header" : "parameter" + " named " + getUsernameKey()), username);
       }
       
       String credentials = props.getProperty(username);
       if(credentials == null || !credentials.equals(password)) {
           throw new AuthenticationException("Invalid username of password", username);
       }
       return username;
    }


}
