package com.icesoft.faces.component.inputrichtext;

import com.icesoft.faces.context.*;
import org.icefaces.impl.event.UIOutputWriter;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.*;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class InputRichTextSetup implements Serializable, SystemEventListener {
    public static final Resource ICE_CK_EDITOR_JS = new FCKJarResource("com/icesoft/faces/component/inputrichtext/ckeditor_ext.js");
    private static final String CK_EDITOR_ZIP = "com/icesoft/faces/component/inputrichtext/ckeditor.zip";
    private static final Date lastModified = new Date();
    private static final Map ZipEntryCacheCK = new HashMap();
    private static final ResourceLinker.Handler CK_LINKED_BASE = new ResourceLinker.Handler() {
        public void linkWith(ResourceLinker linker) {
            synchronized (ZipEntryCacheCK) {
                if (ZipEntryCacheCK.isEmpty()) {
                    loadZipEntryCache();
                }
            }
            Iterator i = ZipEntryCacheCK.keySet().iterator();
            while (i.hasNext()) {
                String entryName = (String) i.next();
                linker.registerRelativeResource(entryName, new RelativeResource(entryName));
            }
        }
    };

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (EnvUtils.isICEfacesView(context)) {
            UIViewRoot root = context.getViewRoot();

            UIOutput inputTextJS = new UIOutputWriter() {
                public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                    String clientID = getClientId(context);
                    writer.startElement("script", this);
                    writer.writeAttribute("id", clientID + "JS", null);
                    writer.writeAttribute("src", getURI(context) + "/ckeditor.js", "src");
                    writer.endElement("script");
                    writer.startElement("script", this);
                    writer.writeAttribute("id", clientID + "JSWrpr", null);
                    writer.writeAttribute("src", getURI(context), null);
                    writer.endElement("script");
                }
            };
            inputTextJS.setTransient(true);
            inputTextJS.setId("inpRichTxt");

            root.addComponentResource(context, inputTextJS, "head");
        }
    }

    private String getURI(FacesContext context) {
        Map session = context.getExternalContext().getSessionMap();

        NonPersistentValue value = (NonPersistentValue) session.get(InputRichTextSetup.class.getName());
        if (value == null || value.getObject() == null) {
            ResourceRegistry registry = ResourceRegistryLocator.locate(FacesContext.getCurrentInstance());
            URI ckBaseURI = registry.loadJavascriptCode(ICE_CK_EDITOR_JS, CK_LINKED_BASE);
            registry.loadJavascriptCode(ICE_CK_EDITOR_JS);

            session.put(InputRichTextSetup.class.getName(), new NonPersistentValue(ckBaseURI));
            return ckBaseURI.toString();
        } else {
            NonPersistentValue o = (NonPersistentValue) session.get(InputRichTextSetup.class.getName());
            return o.getObject().toString();
        }
    }

    private static class RelativeResource implements Resource, Serializable {
        private final String entryName;

        private RelativeResource(final String entryName) {
            this.entryName = entryName;
        }

        public String calculateDigest() {
            return String.valueOf(CK_EDITOR_ZIP + entryName);
        }

        public Date lastModified() {
            return lastModified;
        }

        public InputStream open() throws IOException {
            return new ByteArrayInputStream((byte[]) ZipEntryCacheCK.get(entryName));
        }

        public void withOptions(final Resource.Options options) {
            options.setFileName(entryName);
            options.setLastModified(lastModified);
        }
    }

    private static void loadZipEntryCache() {
        try {
            InputStream inCK = InputRichText.class.getClassLoader().getResourceAsStream(CK_EDITOR_ZIP);
            ZipInputStream zipCK = new ZipInputStream(inCK);
            ZipEntry entryCK;
            while ((entryCK = zipCK.getNextEntry()) != null) {
                if (!entryCK.isDirectory()) {
                    ZipEntryCacheCK.put(entryCK.getName(), toByteArray(zipCK));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
        return output.toByteArray();
    }

    private static class NonPersistentValue implements Serializable {
        private transient Object object;

        private NonPersistentValue(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }
    }
}

class FCKJarResource extends JarResource {

    public FCKJarResource(String path) {
        super(path);
    }

    public void withOptions(Options options) throws IOException {

    }
}