package org.kasource.web.websocket.config;


import java.util.Set;

public class OriginWhiteListConfig {
    private Set<String> originWhiteList;

    /**
     * @return the originWhiteList
     */
    public Set<String> getOriginWhiteList() {
        return originWhiteList;
    }

    /**
     * @param originWhiteList the originWhiteList to set
     */
    public void setOriginWhiteList(Set<String> originWhiteList) {
        this.originWhiteList = originWhiteList;
    }
    
    
}
