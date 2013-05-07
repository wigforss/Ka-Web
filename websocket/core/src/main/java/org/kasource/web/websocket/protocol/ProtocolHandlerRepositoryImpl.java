package org.kasource.web.websocket.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.web.websocket.config.ProtocolHandlerConfig;
import org.kasource.web.websocket.config.WebSocketConfigException;

public class ProtocolHandlerRepositoryImpl implements ProtocolHandlerRepository {
    private Map<String, ProtocolHandler<String>> textProtocols = new HashMap<String, ProtocolHandler<String>>();
    private Map<String, ProtocolHandler<byte[]>> binaryProtocols = new HashMap<String, ProtocolHandler<byte[]>>();
    private Map<String, Map<String, ProtocolHandler<byte[]>>> binaryProtocolUrlMap = new HashMap<String, Map<String, ProtocolHandler<byte[]>>>();
    private Map<String, Map<String, ProtocolHandler<String>>> textProtocolUrlMap = new HashMap<String, Map<String, ProtocolHandler<String>>>();
    private Map<String, ProtocolHandler<String>> defaultUrlTextProtocol;
    private Map<String, ProtocolHandler<byte[]>> defaultUrlBinaryProtocol;
    private ProtocolHandler<String> defaultTextProtocol;
    private ProtocolHandler<byte[]> defaultBinaryProtocol;

    public ProtocolHandlerRepositoryImpl() {
    }

    public ProtocolHandlerRepositoryImpl(ProtocolHandlerConfig<String> textProtocolConfig,
                ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {
        try {
            setTextProtocolHandlerConfig(textProtocolConfig);
            setBinaryProtocolHandlerConfig(binaryProtocolConfig);
        } catch (Exception e) {
            throw new WebSocketConfigException("Could not initialize ProtocolHandlerRepository", e);
        }
    }

    public void setTextProtocolHandlerConfig(ProtocolHandlerConfig<String> textProtocolConfig) {
        if (textProtocolConfig != null) {
            defaultTextProtocol = textProtocolConfig.getDefaultHandler();
            loadTextProtocols(textProtocolConfig);
        }
    }

    public void setBinaryProtocolHandlerConfig(ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {
        if (binaryProtocolConfig != null) {
            defaultBinaryProtocol = binaryProtocolConfig.getDefaultHandler();
            loadBinaryProtocols(binaryProtocolConfig);
        }
    }

    public void initialize(ProtocolHandlerConfig<String> textProtocolConfig,
                ProtocolHandlerConfig<byte[]> binaryProtocolConfig) throws ClassNotFoundException,
                InstantiationException, IllegalAccessException {

    }

    @Override
    public boolean hasProtocol(String protocol, String url) {
        return hasTextProtocol(protocol, url) || hasBinaryProtocol(protocol, url);
    }

    private boolean hasTextProtocol(String protocol, String url) {
        Map<String, ProtocolHandler<String>> textProtocolMap = getTextProtocolsForUrl(url);
        if (textProtocolMap != null && !textProtocolMap.isEmpty()) {
            if (textProtocolMap.containsKey(protocol)) {
                return true;
            } else if (getDefaultTextProtocolForUrl(url) != null
                        && getDefaultTextProtocolForUrl(url).getProtocolName().equals(protocol)) {
                return true;
            }
        } else {
            if (textProtocols.containsKey(protocol)) {
                return true;
            } else if (defaultTextProtocol != null) {
                return defaultTextProtocol.getProtocolName().equals(protocol);
            }
        }
        return false;
    }

    private boolean hasBinaryProtocol(String protocol, String url) {
        Map<String, ProtocolHandler<byte[]>> binaryProtocolMap = getBinaryProtocolsForUrl(url);
        if (binaryProtocolMap != null && !binaryProtocolMap.isEmpty()) {
            if (binaryProtocolMap.containsKey(protocol)) {
                return true;
            } else if (getDefaultBinaryProtocolForUrl(url) != null
                        && getDefaultBinaryProtocolForUrl(url).getProtocolName().equals(protocol)) {
                return true;
            }
        } else {
            if (binaryProtocols.containsKey(protocol)) {
                return true;
            } else if (defaultBinaryProtocol != null) {
                return defaultBinaryProtocol.getProtocolName().equals(protocol);
            }
        }
        return false;
    }

    private void loadTextProtocols(ProtocolHandlerConfig<String> textProtocolConfig) {

        List<ProtocolHandler<String>> protocols = textProtocolConfig.getHandlers();
        if(protocols != null) {
            for (ProtocolHandler<String> protocol : protocols) {
                textProtocols.put(protocol.getProtocolName(), protocol);
            }
        }
        if (textProtocolConfig.getProtocolUrlMap() != null && !textProtocolConfig.getProtocolUrlMap().isEmpty()) {
            for (Map.Entry<String, List<ProtocolHandler<String>>> entry : textProtocolConfig.getProtocolUrlMap()
                        .entrySet()) {
                Map<String, ProtocolHandler<String>> textProtocolsMap = new HashMap<String, ProtocolHandler<String>>();
                protocols = entry.getValue();
                for (ProtocolHandler<String> protocol : protocols) {
                    textProtocolsMap.put(protocol.getProtocolName(), protocol);
                }
                textProtocolUrlMap.put(entry.getKey(), textProtocolsMap);
            }
        }
        defaultUrlTextProtocol = textProtocolConfig.getDefaultProtocolUrlMap();
    }

    private void loadBinaryProtocols(ProtocolHandlerConfig<byte[]> binaryProtocolConfig) {

        List<ProtocolHandler<byte[]>> protocols = binaryProtocolConfig.getHandlers();
        if(protocols != null) {
            for (ProtocolHandler<byte[]> protocol : protocols) {
                binaryProtocols.put(protocol.getProtocolName(), protocol);
            }
        }
        if (binaryProtocolConfig.getProtocolUrlMap() != null && !binaryProtocolConfig.getProtocolUrlMap().isEmpty()) {
            for (Map.Entry<String, List<ProtocolHandler<byte[]>>> entry : binaryProtocolConfig.getProtocolUrlMap()
                        .entrySet()) {
                Map<String, ProtocolHandler<byte[]>> textProtocolsMap = new HashMap<String, ProtocolHandler<byte[]>>();
                protocols = entry.getValue();
                for (ProtocolHandler<byte[]> protocol : protocols) {
                    textProtocolsMap.put(protocol.getProtocolName(), protocol);
                }
                binaryProtocolUrlMap.put(entry.getKey(), textProtocolsMap);

            }
        }
        defaultUrlBinaryProtocol = binaryProtocolConfig.getDefaultProtocolUrlMap();

    }

    /**
     * @return the defaultTextProtocol
     */
    @Override
    public ProtocolHandler<String> getDefaultTextProtocol() {
        return defaultTextProtocol;
    }

    /**
     * @return the defaultBinaryProtocol
     */
    @Override
    public ProtocolHandler<byte[]> getDefaultBinaryProtocol() {
        return defaultBinaryProtocol;
    }

    @Override
    public ProtocolHandler<String> getTextProtocol(String protocol, String url, boolean useDefault) {
        ProtocolHandler<String> textProtocol = null;
        Map<String, ProtocolHandler<String>> textProtocolMap = getTextProtocolsForUrl(url);
        if (hasUrl(textProtocolUrlMap, url) || hasUrl(defaultUrlTextProtocol, url)) {
            if(textProtocolMap != null) {
                textProtocol = textProtocolMap.get(protocol);
            }
            if (textProtocol == null && useDefault) {
                return getDefaultTextProtocolForUrl(url);
            }
        } else {
            textProtocol = textProtocols.get(protocol);
            if (textProtocol == null && useDefault) {
                return defaultTextProtocol;
            }
        }
        return textProtocol;
    }
    
    @Override
    public ProtocolHandler<byte[]> getBinaryProtocol(String protocol, String url, boolean useDefault) {
        ProtocolHandler<byte[]> binaryProtocol = null;
        Map<String, ProtocolHandler<byte[]>> binaryProtocolMap = getBinaryProtocolsForUrl(url);
        if (hasUrl(binaryProtocolUrlMap, url) || hasUrl(defaultUrlBinaryProtocol, url)) {
            if(binaryProtocolMap != null ) {
                binaryProtocol = binaryProtocolMap.get(protocol);
            }
            if (binaryProtocol == null && useDefault) {
                return getDefaultBinaryProtocolForUrl(url);
            }
        } else {
            binaryProtocol = binaryProtocols.get(protocol);
            if (binaryProtocol == null && useDefault) {
                return defaultBinaryProtocol;
            }
        }
        return binaryProtocol;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ProtocolHandler<String>> getTextProtocolsForUrl(String url) {
        return (Map<String, ProtocolHandler<String>>) getValueByUrl(textProtocolUrlMap, url);
    }

    @SuppressWarnings("unchecked")
    private ProtocolHandler<String> getDefaultTextProtocolForUrl(String url) {
        return (ProtocolHandler<String>) getValueByUrl(defaultUrlTextProtocol, url);
    }

    private boolean hasUrl(@SuppressWarnings("rawtypes") Map map, String url) {
        if(map == null || map.isEmpty()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Object key = getKeyForUrl(map.keySet(), url);
        if(key != null) {
            return true;
        } else {
            return false;
        }
      
    }
    
    private Object getKeyForUrl(Set<Object> keys, String url) {
        for (Object key : keys) {
            String urlPattern = key.toString();
            if (urlPattern.contains("*")) {
                urlPattern = urlPattern.replace("*", ".*");
            }
            if (url.matches(urlPattern)) {
                return key;
            }
        }
        return null;
    }
    
    private Object getValueByUrl(@SuppressWarnings("rawtypes") Map map, String url) {
        if(map == null || map.isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Object key = getKeyForUrl(map.keySet(), url);
        if(key != null) {
            return map.get(key);
        } else {
            return null;
        }

    }

    

    @SuppressWarnings("unchecked")
    private Map<String, ProtocolHandler<byte[]>> getBinaryProtocolsForUrl(String url) {
        return (Map<String, ProtocolHandler<byte[]>>) getValueByUrl(binaryProtocolUrlMap, url);
    }

    @SuppressWarnings("unchecked")
    private ProtocolHandler<byte[]> getDefaultBinaryProtocolForUrl(String url) {
        return (ProtocolHandler<byte[]>) getValueByUrl(defaultUrlBinaryProtocol, url);
    }

}
