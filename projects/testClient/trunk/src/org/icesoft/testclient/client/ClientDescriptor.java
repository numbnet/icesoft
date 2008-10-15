package org.icesoft.testclient.client;

/**
 * All the configuration items for a client
 */
public class ClientDescriptor {

    private int initialRequestDelay;
    private int requestCount;
    private int interRequestDelay;
    private String clientClass;
    private int clientCount;
    private String url;
    private boolean isBranch;
    private boolean usingProxy;


    /**
     * 
     * @param clientClass Client subclass to do the configuration work
     * @param url Base URL of application 
     * @param clientCount Number of client threads to start
     * @param initialRequestDelay Delay before first request
     * @param interRequestDelay delay between each succeeding request
     * @param requestCount Number of requests before halting
     * @param branch Branch (or Head) version of ICEfaces
     */
    public ClientDescriptor(String clientClass,
                            String url,                            
                            int clientCount,
                            int initialRequestDelay,
                            int interRequestDelay,
                            int requestCount,
                            boolean branch,
                            boolean usingProxy) {
        
        this.clientClass = clientClass;
        this.initialRequestDelay = initialRequestDelay;
        this.clientCount = clientCount;
        this.interRequestDelay = interRequestDelay;
        isBranch = branch;
        this.requestCount = requestCount;
        this.url = url;
        this.usingProxy = usingProxy;
    }

    public int getClientCount() {
        return clientCount;
    }

    public String getClientClass() {
        return clientClass;
    }

    public int getInitialRequestDelay() {
        return initialRequestDelay;
    }

    public int getInterRequestDelay() {
        return interRequestDelay;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public boolean isBranch() {
        return isBranch;
    }

    public String getUrl() {
        return url;
    }

    public boolean isUsingProxy() {
        return usingProxy;
    }

    public void setUsingProxy(boolean usingProxy) {
        this.usingProxy = usingProxy;
    }

    public String toString() {
        StringBuffer retVal = new StringBuffer();
        retVal.append("ClientDescriptor - Class: " + getClientClass() + ", clientCount: " + getClientCount() );
        return retVal.toString();

    }
}
