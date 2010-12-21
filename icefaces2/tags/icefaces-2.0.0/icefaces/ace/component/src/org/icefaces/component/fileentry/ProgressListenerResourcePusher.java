package org.icefaces.component.fileentry;

import org.icefaces.apache.commons.fileupload.ProgressListener;
import org.icefaces.application.ResourceRegistry;
import org.icefaces.component.utils.JSONBuilder;
import javax.faces.context.FacesContext;
import javax.faces.application.Resource;
import java.util.Map;

/**
 * When the upload has progress, pushes the progress resource to the browser
 */
public class ProgressListenerResourcePusher implements ProgressListener {
    final private static long MIN_INTERVAL = 2000L;

    private Map<String, FileEntryResults> clientId2Results;
    private ProgressResource pushResource;
    private String pushGroupName;
    private long lastPushTime;
    private long lastPercent;
    private int lastDeltaGottenPushed;

    ProgressListenerResourcePusher(
            Map<String, FileEntryResults> clientId2Results) {
        this.clientId2Results = clientId2Results;
    }

    public void update(long read, long total, int chunkIndex) {
        if (read > 0 && total > 0) {
            boolean force = read == total;
            long currPercent = (read * 100L) / total;
            if (force || currPercent > lastPercent) {
                long now = System.currentTimeMillis();
                if (force || (now - lastPushTime) >= MIN_INTERVAL) {
                    if (tryPush(currPercent, force)) {
                        lastPushTime = now;
                        lastPercent = currPercent;
                    }
                }
            }
        }
        else {
            lastPercent = 0L;
        }
    }

    protected boolean tryPush(long percent, boolean force) {
        if (pushResource == null || pushGroupName == null) {
            return false;
        }
//System.out.println("tryPush()  percent: " + percent);

        //TODO Update resource contents
        int deltaGottenPushed = updateResourceContents(percent);
//System.out.println("deltaGottenPushed: " + deltaGottenPushed + "  lastDeltaGottenPushed: " + lastDeltaGottenPushed);
        int localLastDeltaGottenPushed = lastDeltaGottenPushed;
        if (!force && deltaGottenPushed > 2 && deltaGottenPushed > localLastDeltaGottenPushed + 1) {
            lastDeltaGottenPushed = deltaGottenPushed;
            return true;
        }

        //POLL: Comment this section
        PushUtils.push(pushGroupName);

//System.out.println("tryPush()  Pushed progress update");
        return true;
    }

    void setPushResourcePathAndGroupName(FacesContext facesContext,
            String pushResourcePath, String pushGroupName) {
        Resource res = ResourceRegistry.getResourceByPath(facesContext, pushResourcePath);
        this.pushResource = ((res instanceof ProgressResource) ?
                (ProgressResource) res : null);
        this.pushGroupName = pushGroupName;
    }

    void clear() {
        clientId2Results = null;
        pushResource = null;
        pushGroupName = null;
        lastPushTime = 0L;
        lastPercent = 0L;
        lastDeltaGottenPushed = 0;
    }

    private int updateResourceContents(long percent) {
        //String contents = " " + Long.toString(percent) + "% ";
        JSONBuilder contents = JSONBuilder.create().beginMap();
        contents.entry("percent", percent);
        contents.beginArray("results");
        for (String clientId : clientId2Results.keySet()) {
            contents.item(clientId);
        }
        contents.endArray();
        contents.endMap();
//System.out.println("updateResourceContents()  contents: " + contents.toString());
        return pushResource.updateProgressInfo(contents.toString());
    }
}
