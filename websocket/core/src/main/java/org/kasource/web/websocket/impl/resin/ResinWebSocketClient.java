package org.kasource.web.websocket.impl.resin;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.kasource.web.websocket.impl.WebSocketClient;
import org.kasource.web.websocket.protocol.ProtocolHandler;
import org.kasource.web.websocket.util.IoUtils;

import com.caucho.websocket.WebSocketContext;
import com.caucho.websocket.WebSocketListener;

public class ResinWebSocketClient implements WebSocketListener, WebSocketClient  {

    private ResinWebSocketManager manager;
    private String id;
    private WebSocketContext context;
    private Map<String, String[]> connectionParameters;
    private IoUtils ioUtils = new IoUtils();
    private ProtocolHandler<String> textProtocol;
    private ProtocolHandler<byte[]> binaryProtocol;
    
    public ResinWebSocketClient(ResinWebSocketManager manager, String clientId, Map<String, String[]> connectionParameters) {
        this.id = clientId;
        this.manager = manager;
        this.connectionParameters = connectionParameters;
    }



    @Override
    public void onClose(WebSocketContext context) throws IOException {

    }



    @Override
    public void onDisconnect(WebSocketContext context) throws IOException {
        manager.unregisterClient(id);
    }



    @Override
    public void onReadBinary(WebSocketContext context, InputStream in) throws IOException {
        manager.onWebSocketMessage(ioUtils.toByteArray(in), binaryProtocol, id);
       

    }



    @Override
    public void onReadText(WebSocketContext context, Reader reader) throws IOException {
        manager.onWebSocketMessage(ioUtils.readString(reader), textProtocol, id);

    }



    @Override
    public void onStart(WebSocketContext context) throws IOException {
        this.context = context;
        manager.registerClient(id, this, connectionParameters);
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
     * @param textProtocol the textProtocol to set
     */
    public void setTextProtocol(ProtocolHandler<String> textProtocol) {
        this.textProtocol = textProtocol;
    }


    /**
     * @param binaryProtocol the binaryProtocol to set
     */
    public void setBinaryProtocol(ProtocolHandler<byte[]> binaryProtocol) {
        this.binaryProtocol = binaryProtocol;
    }


}
