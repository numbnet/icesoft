package com.icesoft.faces.component.inputrichtext;

import org.icefaces.impl.event.UIOutputWriter;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;


public class InputRichTextSetup implements Serializable, SystemEventListener {
    private static final String INPUTRICHTEXT_CKEDITOR_DIR = "inputrichtext/ckeditor/";

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (EnvUtils.isICEfacesView(context)) {
            try {
                UIViewRoot root = context.getViewRoot();
                String code = loadResources(context);
                MappingsWriter mappingsWriter = new MappingsWriter(code);
                mappingsWriter.setTransient(true);
                mappingsWriter.setId("inpRichTxt");

                List componentResources = root.getComponentResources(context, "head");
                //make sure the script is always loaded first
                if (componentResources.isEmpty()) {
                    root.addComponentResource(context, mappingsWriter, "head");
                } else {
                    componentResources.add(0, mappingsWriter);
                }

            } catch (IOException e) {
                throw new AbortProcessingException(e);
            }
        }
    }

    private String loadResources(FacesContext context) throws IOException {
        Map applicationMap = context.getExternalContext().getApplicationMap();

        Object value = applicationMap.get(InputRichTextSetup.class.getName());
        if (value == null) {
            List allResources = new ArrayList();
            List cssResources = new ArrayList();
            List imageResources = new ArrayList();

            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            Resource resource = resourceHandler.createResource(INPUTRICHTEXT_CKEDITOR_DIR + "ckeditor.js");

            //collecting resource relative paths
            URL url = resource.getURL();
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
                JarFile jar = new JarFile(jarPath);
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName();
                    //create resources only for the files (excluding directories) contained in INPUTRICHTEXT_CKEDITOR_DIR
                    if (name.contains(INPUTRICHTEXT_CKEDITOR_DIR) && !name.endsWith("/")) {
                        String localPath = name.substring(name.indexOf(INPUTRICHTEXT_CKEDITOR_DIR));

                        if (localPath.endsWith(".css")) {
                            InputStream in = jar.getInputStream(jarEntry);
                            cssResources.add(new CSSResourceEntry(localPath, readIntoByteArray(in)));
                        } else if (localPath.endsWith(".png") || localPath.endsWith(".gif") || localPath.endsWith(".jpg")) {
                            createResourceMapping(localPath, imageResources, resourceHandler);
                        } else {
                            createResourceMapping(localPath, allResources, resourceHandler);
                        }
                    }
                }
            } else {
                throw new IOException("Cannot access InputRichText resources icefaces-compat.jar when exploded into the classpath.");
            }

            rewriteRelativeImagePaths(cssResources, imageResources);
            File file = createCSSResourcesJar(context, cssResources);
            addJarToClasspath(file);

            //add CSS mappings that point to the rewritten CSS resources
            Iterator<CSSResourceEntry> i = cssResources.iterator();
            while (i.hasNext()) {
                CSSResourceEntry css = i.next();
                String resourcePath = css.getLocalPath().replace("css", "rewritten.css");
                Resource r = resourceHandler.createResource(resourcePath);
                allResources.add(new ResourceMapping(css.getRelativeLocalPath(), r.getRequestPath()));
            }
            //add image resources
            allResources.addAll(imageResources);

            StringBuffer code = new StringBuffer();
            code.append("window.CKEDITOR_GETURL = function(r) { var mappings = [");
            Iterator<ResourceMapping> entries = allResources.iterator();
            while (entries.hasNext()) {
                ResourceMapping next = entries.next();
                code.append("{inbound: '");
                code.append(next.getRelativeLocalPath());
                code.append("', outbound: '");
                code.append(next.getRequestPath());
                code.append("'}");
                if (entries.hasNext()) {
                    code.append(",");
                }
            }
            code.append("]; if (r.indexOf('http://') == 0) { var i = document.location.href.lastIndexOf('/'); r = r.substring(i + 1); }; for (var i = 0, l = mappings.length; i < l; i++) { var m = mappings[i]; if (m.inbound == r) { return m.outbound;} } return false; };");
            applicationMap.put(InputRichTextSetup.class.getName(), code.toString());

            return code.toString();
        } else {
            return (String) value;
        }
    }

    private void addJarToClasspath(File file) throws IOException {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(this.getClass().getClassLoader(), new Object[]{file.toURI().toURL()});
        } catch (Throwable t) {
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    private File createCSSResourcesJar(FacesContext context, List cssList) throws IOException {
        //using temporary directory as defined in spec: http://java.sun.com/developer/technicalArticles/Servlets/servletapi/
        File tmpDir = (File) context.getExternalContext().getApplicationMap().get("javax.servlet.context.tempdir");
        File file = File.createTempFile("remappedCSSResources", ".tmp.jar", tmpDir);
        file.deleteOnExit();
        JarOutputStream jar = new JarOutputStream(new FileOutputStream(file));
        Iterator<CSSResourceEntry> i = cssList.iterator();
        while (i.hasNext()) {
            CSSResourceEntry next = i.next();
            String path = next.getLocalPath();
            byte[] content = next.getContent();
            String resourcePath = path.replace("css", "rewritten.css");
            jar.putNextEntry(new JarEntry("META-INF/resources/" + resourcePath));
            jar.write(content);
            jar.closeEntry();
        }
        jar.close();

        return file;
    }

    private void rewriteRelativeImagePaths(List csss, List images) throws UnsupportedEncodingException {
        Iterator<CSSResourceEntry> i = csss.iterator();
        while (i.hasNext()) {
            CSSResourceEntry css = i.next();
            String content = css.getContentAsString("UTF-8");
            String dir = css.getRelativeLocalDir();

            Iterator<ResourceMapping> ri = images.iterator();
            while (ri.hasNext()) {
                ResourceMapping resourceMapping = ri.next();
                String path = resourceMapping.getRelativeLocalPath();
                if (path.startsWith(dir)) {
                    String relativePath = path.substring(dir.length() + 1);
                    String requestPath = resourceMapping.getRequestPath();
                    content = content.replaceAll(relativePath, requestPath);
                }
            }

            css.setContentAsString(content, "UTF-8");
        }
    }

    private void createResourceMapping(String path, List mappings, ResourceHandler resourceHandler) {
        Resource resource = resourceHandler.createResource(path);
        mappings.add(new ResourceMapping(path.substring(INPUTRICHTEXT_CKEDITOR_DIR.length()), resource.getRequestPath()));
    }

    private static class MappingsWriter extends UIOutputWriter {
        private String code;

        public MappingsWriter(String code) {
            super();
            this.code = code;
            this.setTransient(true);
        }

        public void encode(ResponseWriter writer, FacesContext context) throws IOException {
            writer.startElement("script", this);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeText("try {" + code + " } catch (ex) { alert(ex) }", null);
            writer.endElement("script");
        }

        //Convince PortletFaces Bridge that this is a valid script for
        //inserting into the Portal head
        public String getRendererType() {
            return "javax.faces.resource.Script";
        }
    }

    private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1)
            out.write(buffer, 0, bytesRead); // write
        out.flush();

        return out.toByteArray();
    }

    private static class ResourceMapping {
        private String relativeLocalPath;
        private String requestPath;

        private ResourceMapping(String relativeLocalPath, String requestPath) {
            this.relativeLocalPath = relativeLocalPath;
            this.requestPath = requestPath;
        }

        public String getRelativeLocalPath() {
            return relativeLocalPath;
        }

        public String getRequestPath() {
            return requestPath;
        }
    }

    private static class CSSResourceEntry {
        private String localPath;
        private byte[] content;

        private CSSResourceEntry(String localPath, byte[] content) {
            this.localPath = localPath;
            this.content = content;
        }

        public String getLocalPath() {
            return localPath;
        }

        public String getRelativeLocalPath() {
            return localPath.substring(INPUTRICHTEXT_CKEDITOR_DIR.length());
        }

        public String getRelativeLocalDir() {
            int position = localPath.lastIndexOf("/");
            return INPUTRICHTEXT_CKEDITOR_DIR.length() > position ? "/" : localPath.substring(INPUTRICHTEXT_CKEDITOR_DIR.length(), position);
        }

        public byte[] getContent() {
            return content;
        }

        public String getContentAsString(String encoding) throws UnsupportedEncodingException {
            return new String(content, encoding);
        }

        public void setContentAsString(String newContent, String encoding) throws UnsupportedEncodingException {
            content = newContent.getBytes(encoding);
        }
    }
}