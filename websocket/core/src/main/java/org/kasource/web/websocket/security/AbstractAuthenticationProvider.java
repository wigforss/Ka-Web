package org.kasource.web.websocket.security;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {
    private static final String DEFAULT_USERNAME_KEY = "username";
    private static final String DEFAULT_PASSWORD_KEY = "password";
    
    private boolean headerBased;
    private String usernameKey = DEFAULT_USERNAME_KEY;
    private String passwordKey = DEFAULT_PASSWORD_KEY;
    
    protected String getUsername(HttpServletRequest request) {
        String username = null;
        if(headerBased) {
            username = request.getHeader(usernameKey);
        } else {
            username = request.getParameter(usernameKey);
        }
      
        return username;
    }
    
    protected String getPassword(HttpServletRequest request) {
        String password = null;
        if(headerBased) {
            password = request.getHeader(passwordKey);
        } else {
            password = request.getParameter(passwordKey);
        }
       
        return password;
    }

    /**
     * @return the headerBased
     */
    public boolean isHeaderBased() {
        return headerBased;
    }

    /**
     * @param headerBased the headerBased to set
     */
    public void setHeaderBased(boolean headerBased) {
        this.headerBased = headerBased;
    }

    /**
     * @return the usernameKey
     */
    public String getUsernameKey() {
        return usernameKey;
    }

    /**
     * @param usernameKey the usernameKey to set
     */
    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    /**
     * @return the passwordKey
     */
    public String getPasswordKey() {
        return passwordKey;
    }

    /**
     * @param passwordKey the passwordKey to set
     */
    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
}
