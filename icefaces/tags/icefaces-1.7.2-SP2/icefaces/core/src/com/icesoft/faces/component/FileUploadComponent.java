package com.icesoft.faces.component;

import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import org.apache.commons.fileupload.FileItemStream;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;

public interface FileUploadComponent {

    void upload(FileItemStream fileItemStream, String uploadDirectory, boolean uploadDirectoryAbsolute, long maxSize, ServletContext servletContext, String sessionId, PersistentFacesState state) throws IOException;

    void updateProgress(PersistentFacesState state, int percentage);

    void renderIFrame(Writer writer, BridgeFacesContext context) throws IOException;
}
