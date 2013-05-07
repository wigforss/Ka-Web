package org.kasource.web.websocket.protocol;


public interface ProtocolHandlerRepository {
    public boolean hasProtocol(String protocol, String url); 
    
    public ProtocolHandler<String> getTextProtocol(String protocol, String url, boolean useDefault);
    
    public ProtocolHandler<byte[]> getBinaryProtocol(String protocol, String url, boolean useDefault);
    
   
    public ProtocolHandler<String> getDefaultTextProtocol();

    /**
     * @return the defaultBinaryProtocol
     */
    public ProtocolHandler<byte[]> getDefaultBinaryProtocol();
}
