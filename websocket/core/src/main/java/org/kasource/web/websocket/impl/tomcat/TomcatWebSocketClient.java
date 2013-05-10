package org.kasource.web.websocket.impl.tomcat;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TomcatWebSocketClient extends StreamInbound implements WebSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(TomcatWebSocketClient.class);
    private WebSocketClientConfig clientConfig;
    private IoUtils ioUtils = new IoUtils();
    private ProtocolHandler<String> textProtocolHandler;
    
    private ProtocolHandler<byte[]> binaryProtocolHandler;

    protected TomcatWebSocketClient(WebSocketClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }



    @Override
    protected void onBinaryData(InputStream is) throws IOException {
        clientConfig.getManager().onWebSocketMessage(this, ioUtils.toByteArray(is));

    }



    @Override
    protected void onTextData(Reader r) throws IOException {
        
        clientConfig.getManager().onWebSocketMessage(this, ioUtils.readString(r));
    }



    @Override
    protected void onOpen(WsOutbound outbound) {
        clientConfig.getManager().registerClient(this);
    }



    @Override
    protected void onClose(int status) {         
        clientConfig.getManager().unregisterClient(this);
    }
    
   
    

    @Override
    public void sendMessageToSocket(String message)  {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }



    @Override
    public void sendMessageToSocket(byte[] message) {
        try {
            getWsOutbound().writeBinaryMessage(ByteBuffer.wrap(message));
        } catch (IOException e) {
            LOG.debug("Could not send message to socket to " + clientConfig.getUsername() + " with id " + clientConfig.getClientId(), e);
        }
        
    }


   



    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return clientConfig.getUsername();
    }



    /**
     * @return the connectionParameters
     */
    @Override
    public Map<String, String[]> getConnectionParameters() {
        return clientConfig.getConnectionParameters();
    }



    /**
     * @return the id
     */
    @Override
    public String getId() {
        return clientConfig.getClientId();
    }

    @Override
    public String getUrl() {
        return clientConfig.getUrl();
    }
    
    @Override
    public String getSubProtocol() {
        return clientConfig.getSubProtocol();
    }

    /**
     * @return the textProtocolHandler
     */
    @Override
    public ProtocolHandler<String> getTextProtocolHandler() {
        return textProtocolHandler;
    }



    /**
     * @param textProtocolHandler the textProtocolHandler to set
     */
    @Override
    public void setTextProtocolHandler(ProtocolHandler<String> textProtocolHandler) {
        this.textProtocolHandler = textProtocolHandler;
    }



    /**
     * @return the binaryProtocolHandler
     */
    @Override
    public ProtocolHandler<byte[]> getBinaryProtocolHandler() {
        return binaryProtocolHandler;
    }



    /**
     * @param binaryProtocolHandler the binaryProtocolHandler to set
     */
    @Override
    public void setBinaryProtocolHandler(ProtocolHandler<byte[]> binaryProtocolHandler) {
        this.binaryProtocolHandler = binaryProtocolHandler;
    }



    @Override
    public void sendBinaryMessageToSocket(Object message) {
        if(binaryProtocolHandler == null) {
            throw new IllegalStateException("No binary handler configured for client");
        }
        sendMessageToSocket(binaryProtocolHandler.toMessage(message));
        
    }



    @Override
    public void sendTextMessageToSocket(Object message) {
        if(textProtocolHandler == null) {
            throw new IllegalStateException("No text handler configured for client");
        }
        sendMessageToSocket(textProtocolHandler.toMessage(message));
        
    }


}
