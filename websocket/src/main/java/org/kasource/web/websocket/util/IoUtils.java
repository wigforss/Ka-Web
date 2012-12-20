package org.kasource.web.websocket.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class IoUtils {
    
    /**
     * Converts the supplied input stream to a byte array. 
     * This method closes the supplied input stream. 
     * 
     * @param is The input stream to convert.
     * 
     * @return the supplied input stream as a byte array.
     * 
     * @throws IOException If any exception occurred while reading or closing the input stream.
     **/
    public byte[] toByteArray(InputStream is) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buff = new byte[1024]; 
            int bytesRead = 0;
            while((bytesRead = bis.read(buff)) != -1) {
                bos.write(buff, 0, bytesRead);
            }
        } finally {
               is.close();
        }
        return bos.toByteArray();
    }

    /**
     * Reads all content from the supplied reader and returns it as String.
     * 
     * @param r  Reader to read content from.
     * 
     * @return Content from the reader as a string.
     * 
     * @throws IOException f any exception occurred while reading.
     **/
    public String readString(Reader r) throws IOException {
        BufferedReader reader = new BufferedReader(r);
        StringBuilder str = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }
        return str.toString();
    }
}
