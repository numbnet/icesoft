package org.icefaces.component.fileentry;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.HashMap;
import java.net.URL;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;

/**
 * The fileEntry progress information is accessed via a dynamic resource
 */
public class ProgressResource extends Resource {
    private String uniqueIdentifier;
    private String progressInfo;
    private int deltaGottenPushed;

    ProgressResource(String identifier) {
        this.uniqueIdentifier = identifier + ".txt";
        this.progressInfo = "";
        this.deltaGottenPushed = 0;
        setContentType("text/plain");
        setResourceName(PushUtils.PROGRESS_PREFIX + uniqueIdentifier);
    }

    int updateProgressInfo(String progressInfo) {
        this.progressInfo = progressInfo;
        return ++this.deltaGottenPushed;
    }

    public Map<String, String> getResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<String, String>(1);
        responseHeaders.put("Content-type", "text/plain; charset=utf-8");

        responseHeaders.put("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
        responseHeaders.put("Pragma", "no-cache");//HTTP 1.0
        responseHeaders.put("Expires", "0");//prevents proxy caching
        
System.out.println("PR.getResponseHeaders()  responseHeaders: " + responseHeaders);
        return responseHeaders;
    }

    public String getRequestPath() {
System.out.println("PR.getRequestPath()  " + PushUtils.PROGRESS_PREFIX + uniqueIdentifier);
        return PushUtils.PROGRESS_PREFIX + uniqueIdentifier;
    }

    public URL getURL() {
System.out.println("PR.getURL()");
        return null;
    }

    public boolean userAgentNeedsUpdate(FacesContext facesContext) {
System.out.println("PR.userAgentNeedsUpdate()");
        return true;
    }

    public InputStream getInputStream() {
System.out.println("PR.getInputStream()  progressInfo: " + progressInfo);
        deltaGottenPushed--;
        if (progressInfo == null) {
            return null;
        }
        try {
            byte[] progressBytes = progressInfo.getBytes("UTF-8");
            return new ByteArrayInputStream(progressBytes);
        } catch(UnsupportedEncodingException e) {
System.out.println("PR.getInputStream()  UnsupportedEncodingException: " + e);
        }
        return null;
    }
}
