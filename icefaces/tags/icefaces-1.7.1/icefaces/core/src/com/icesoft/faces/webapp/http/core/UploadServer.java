package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.component.FileUploadComponent;
import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.StreamingContentHandler;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.util.SeamUtilities;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class UploadServer implements Server {
    private static final Log Log = LogFactory.getLog(UploadServer.class);
    private Map views;
    private long maxSize;
    private String uploadDirectory;
    private boolean uploadDirectoryAbsolute;

    public UploadServer(Map views, Configuration configuration) {
        this.views = views;
        this.maxSize = configuration.getAttributeAsLong("uploadMaxFileSize", 3 * 1024 * 1024);//3Mb
        //Partial fix for http://jira.icefaces.org/browse/ICE-1600
        this.uploadDirectory = configuration.getAttribute("uploadDirectory", "");
        this.uploadDirectoryAbsolute = configuration.getAttributeAsBoolean("uploadDirectoryAbsolute", false);
    }

    public void service(final Request request) throws Exception {
        final ServletFileUpload uploader = new ServletFileUpload();
        final ProgressCalculator progressCalculator = new ProgressCalculator();
        uploader.setFileSizeMax(maxSize);
        uploader.setProgressListener(new ProgressListener() {
            public void update(long read, long total, int chunkIndex) {
                progressCalculator.progress(read, total);
            }
        });
        request.detectEnvironment(new Request.Environment() {
            public void servlet(Object req, Object resp) throws Exception {
                final HttpServletRequest servletRequest = (HttpServletRequest) req;
                FileItemIterator iter = uploader.getItemIterator(servletRequest);
                String viewIdentifier = null;
                String componentID = null;
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        if ("ice.component".equals(name)) {
                            componentID = Streams.asString(item.openStream());
                        } else if ("ice.view".equals(name)) {
                            viewIdentifier = Streams.asString(item.openStream());
                        }
                    } else {
                        final View view = (View) views.get(viewIdentifier);
                        final BridgeFacesContext context = view.getFacesContext();
                        final FileUploadComponent component = (FileUploadComponent) D2DViewHandler.findComponent(componentID, context.getViewRoot());
                        view.makeCurrent();
                        progressCalculator.setListenerAndContextAndPFS(component, context, view.getPersistentFacesState());
                        try {
                            component.upload(
                                    item,
                                    uploadDirectory,
                                    uploadDirectoryAbsolute,
                                    maxSize,
                                    context,
                                    servletRequest.getSession().getServletContext(),
                                    servletRequest.getRequestedSessionId());
                        } catch (IOException e) {
                            try {
                                progressCalculator.reset();
                            } catch (Throwable tr) {
                                //ignore
                            }
                        } catch (Throwable t) {
                            try {
                                progressCalculator.reset();
                            } catch (Throwable tr) {
                                //ignore
                            }
                            Log.warn("File upload failed", t);
                        } finally {
                            request.respondWith(new StreamingContentHandler("text/html", "UTF-8") {
                                public void writeTo(Writer writer) throws IOException {
                                    component.renderIFrame(writer, context);
                                }
                            });
                        }
                    }
                }
            }

            public void portlet(Object request, Object response, Object config) {
                throw new IllegalAccessError("Cannot upload using a portlet request/response.");
            }
        });
    }

    public void shutdown() {
    }

    private static class ProgressCalculator {
        private final int GRANULARITY = 10;
        private FileUploadComponent listener;
        private BridgeFacesContext context;
        private PersistentFacesState state;
        private int lastGranularlyNotifiablePercent = -1;

        public void progress(long read, long total) {
            if (total > 0) {
                int percentage = (int) ((read * 100L) / total);
                int percentageAboveGranularity = percentage % GRANULARITY;
                int granularNotifiablePercentage = percentage - percentageAboveGranularity;
                boolean shouldNotify = granularNotifiablePercentage > lastGranularlyNotifiablePercent;
                lastGranularlyNotifiablePercent = granularNotifiablePercentage;
                if (shouldNotify)
                    potentiallyNotify();
            }
        }

        public void setListenerAndContextAndPFS(
                FileUploadComponent listener,
                BridgeFacesContext context,
                PersistentFacesState state) {
            this.listener = listener;
            this.context = context;
            this.state = state;
            // Always setCurrent() right away, in case we never get progress,
            //  notifications, and are immediately done, in case
            //  InputFile.upload(-) needs things setup
            setCurrent();
            potentiallyNotify();
        }

        public void reset() {
            PersistentFacesState st = state;
            BridgeFacesContext ctx = context;
            FileUploadComponent component = listener;
            state = null;
            context = null;
            listener = null;
            if (ctx != null && component != null) {
                ctx.setCurrentInstance();
                st.setCurrentInstance();
                component.setProgress(0);
            }
        }

        protected void potentiallyNotify() {
            if (listener != null &&
                context != null &&
                state != null &&
                lastGranularlyNotifiablePercent >= 0)
            {
                setCurrent();
                listener.setProgress(lastGranularlyNotifiablePercent);
                
                // If we can do server push
                if (!state.isSynchronousMode() && listener.renderOnProgress()) {
                    try{
                        // Seam throws spurious exceptions with PFS.renderLater
                        //  so we'll work-around that for now. Fix later.
                        if (SeamUtilities.isSeamEnvironment()) {
                            state.setupAndExecuteAndRender();
                        }
                        else {
                            state.renderLater();
                        }
                    }
                    catch(Exception e) {
                        Log.warn("Problem rendering view during file upload", e);
                    }
                }
            }
        }
        
        private void setCurrent() {
            if(context != null && state != null) {
                context.setCurrentInstance();
                state.setCurrentInstance();
            }
        }
    }
}
