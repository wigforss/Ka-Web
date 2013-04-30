package org.kasource.web.websocket.security;

public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String username;
    
    public AuthenticationException(String message, String username) {
        super(message);
        this.username = username;
    }
    
    public AuthenticationException(String message, Throwable cause, String username) {
        super(message, cause);
        this.username = username;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

}
