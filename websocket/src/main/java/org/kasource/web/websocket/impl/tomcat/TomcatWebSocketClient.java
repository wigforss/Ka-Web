package org.kasource.web.websocket.impl.tomcat;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.kasource.web.websocket.impl.WebSocketClient;
import org.kasource.web.websocket.util.IoUtils;



public class TomcatWebSocketClient extends StreamInbound implements WebSocketClient {

    
    private TomcatWebSocketManager manager;
    private Map<String, String[]> connectionParameters;
    private String id;
    private IoUtils ioUtils = new IoUtils();
 

    protected TomcatWebSocketClient(TomcatWebSocketManager manager, String clientId, Map<String, String[]> connectionParameters) {
        this.connectionParameters = connectionParameters;
        this.manager = manager;
        this.id = clientId;
    }



    @Override
    protected void onBinaryData(InputStream is) throws IOException {
        manager.onWebSocketMessage(ioUtils.toByteArray(is), id);

    }



    @Override
    protected void onTextData(Reader r) throws IOException {
        
        manager.onWebSocketMessage(ioUtils.readString(r), id);
    }



    @Override
    protected void onOpen(WsOutbound outbound) {
        manager.registerClient(id, this, connectionParameters);
    }



    @Override
    protected void onClose(int status) {         
        manager.unregisterClient(id);
    }
    
   
    

    @Override
    public void sendMessageToSocket(String message)  {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
        } catch (IOException e) {
        }
        
    }



    @Override
    public void sendMessageToSocket(byte[] message) {
        try {
            getWsOutbound().writeBinaryMessage(ByteBuffer.wrap(message));
        } catch (IOException e) {
        }
        
    }


}
