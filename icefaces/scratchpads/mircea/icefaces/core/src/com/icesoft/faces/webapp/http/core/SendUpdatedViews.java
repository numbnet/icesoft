package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SendUpdatedViews implements Server {
    private Collection synchronouslyUpdatedViews;
    private BlockingQueue allUpdatedViews;

    public SendUpdatedViews(Collection synchronouslyUpdatedViews, final BlockingQueue allUpdatedViews) {
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
        this.allUpdatedViews = allUpdatedViews;
    }

    public void service(final Request request) throws Exception {
        //todo: refactor this!
        while (true) {
            try {
                Set viewIdentifiers = new HashSet();
                viewIdentifiers.add(allUpdatedViews.take());
                allUpdatedViews.drainTo(viewIdentifiers);
                viewIdentifiers.removeAll(synchronouslyUpdatedViews);
                synchronouslyUpdatedViews.clear();
                if (!viewIdentifiers.isEmpty()) {
                    request.respondWith(new UpdatedViewsHandler((String[]) viewIdentifiers.toArray(new String[viewIdentifiers.size()])));
                    return;
                }
            } catch (InterruptedException e) {
                continue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void shutdown() {
    }

    private class UpdatedViewsHandler extends FixedXMLContentHandler {
        private String[] viewIdentifiers;

        public UpdatedViewsHandler(String[] viewIdentifiers) {
            this.viewIdentifiers = viewIdentifiers;
        }

        public void writeTo(Writer writer) throws IOException {
            writer.write("<updated-views>");
            for (int i = 0; i < viewIdentifiers.length; i++) {
                writer.write(viewIdentifiers[i]);
                writer.write(' ');
            }
            writer.write("</updated-views>");
        }
    }
}
