package org.kasource.web.websocket.impl.resin;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.kasource.web.websocket.client.WebSocketClient;
import org.kasource.web.websocket.client.WebSocketClientConfig;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.util.IoUtils;

import com.caucho.websocket.WebSocketContext;
import com.caucho.websocket.WebSocketListener;

public class ResinWebSocketClient implements WebSocketListener, WebSocketClient  {
    private  WebSocketClientConfig clientConfig;
    private IoUtils ioUtils = new IoUtils();
    private WebSocketContext context;
    private ProtocolHandler<String> textProtocolHandler;
    private ProtocolHandler<byte[]> binaryProtocolHandler;
    
    public ResinWebSocketClient(WebSocketClientConfig clientConfig) {
       this.clientConfig = clientConfig;
    }



    @Override
    public void onClose(WebSocketContext context) throws IOException {

    }



    @Override
    public void onDisconnect(WebSocketContext context) throws IOException {
        clientConfig.getManager().unregisterClient(this);
    }



    @Override
    public void onReadBinary(WebSocketContext context, InputStream in) throws IOException {
        clientConfig.getManager().onWebSocketMessage(this, ioUtils.toByteArray(in));
       

    }



    @Override
    public void onReadText(WebSocketContext context, Reader reader) throws IOException {
        clientConfig.getManager().onWebSocketMessage(this, ioUtils.readString(reader));

    }



    @Override
    public void onStart(WebSocketContext context) throws IOException {
        this.context = context;
        clientConfig.getManager().registerClient(this);
    }



    @Override
    public void onTimeout(WebSocketContext context) throws IOException {
      

    }
    
   
    public void sendMessage(String message) throws IOException  {
        BufferedWriter out = new BufferedWriter(context.startTextMessage());
        out.append(message);
        out.close();
    }



 
    public void sendBinaryMessage(byte[] message) throws IOException  {
        BufferedOutputStream out = new BufferedOutputStream(context.startBinaryMessage());
        out.write(message);
        out.close();
    }



    @Override
    public void sendMessageToSocket(String message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
        }
        
    }



    @Override
    public void sendMessageToSocket(byte[] message) {
        try {
            sendBinaryMessage(message);
        } catch (IOException e) {
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
