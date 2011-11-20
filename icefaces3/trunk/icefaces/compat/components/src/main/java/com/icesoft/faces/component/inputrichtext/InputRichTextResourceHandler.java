/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.component.inputrichtext;

import org.icefaces.impl.util.Base64;
import org.icefaces.impl.util.Util;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class InputRichTextResourceHandler extends ResourceHandlerWrapper {
    private static final String INPUTRICHTEXT_CKEDITOR_DIR = "inputrichtext/ckeditor/";
    private static final String META_INF_RESOURCES = "/META-INF/resources/";
    private static final String CKEDITOR_MAPPING_JS = "ckeditor.mapping.js";
    private static final String CKEDITOR_JS = "ckeditor.js";
    private ResourceHandler handler;
    private String extensionMapping;
    private HashMap<String, ResourceEntry> cssResources = new HashMap();
    private ResourceEntry codeResource;

    public InputRichTextResourceHandler(ResourceHandler handler) {
        this.handler = handler;

        final ArrayList imageResources = new ArrayList();
        final ArrayList allResources = new ArrayList();
        try {
            //collecting resource relative paths
            Class thisClass = this.getClass();
            InputStream in = thisClass.getResourceAsStream(META_INF_RESOURCES + "inputrichtext/ckeditor.resources");
            String resourceList = new String(readIntoByteArray(in), "UTF-8");
            String[] paths = resourceList.split(" ");
            for (int i = 0; i < paths.length; i++) {
                String localPath = paths[i];
                byte[] content = readIntoByteArray(thisClass.getResourceAsStream(META_INF_RESOURCES + localPath));
                if (localPath.endsWith(".css")) {
                    cssResources.put(localPath, new ResourceEntry(localPath, content));
                } else if (localPath.endsWith(".jpg") || localPath.endsWith(".gif") || localPath.endsWith(".png")) {
                    imageResources.add(localPath);
                } else {
                    allResources.add(localPath);
                }
            }

            //calculate mappings when the first request comes in (to find out the context path)
            FacesContext.getCurrentInstance().getApplication().subscribeToEvent(PreRenderViewEvent.class, new SystemEventListener() {
                public void processEvent(SystemEvent event) throws AbortProcessingException {
                    FacesContext context = FacesContext.getCurrentInstance();
                    try {
                        calculateExtensionMapping();
                        calculateMappings(context, allResources, cssResources, imageResources);
                    } catch (UnsupportedEncodingException e) {
                        throw new AbortProcessingException(e);
                    }
                }

                public boolean isListenerForSource(Object source) {
                    return EnvUtils.isICEfacesView(FacesContext.getCurrentInstance());
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateExtensionMapping() {
        if (extensionMapping == null) {
            Resource resource = super.createResource(INPUTRICHTEXT_CKEDITOR_DIR + CKEDITOR_JS);
            String path = resource.getRequestPath();
            int position = path.lastIndexOf(".");
            extensionMapping = position < 0 ? "" : path.substring(position);
        }
    }

    private void calculateMappings(FacesContext context, ArrayList allResources, HashMap cssResources, ArrayList imageResources) throws UnsupportedEncodingException {
        Map applicationMap = context.getExternalContext().getApplicationMap();

        String value = (String) applicationMap.get(InputRichTextResourceHandler.class.getName());
        if (value == null) {
            //rewrite relative request paths
            Iterator<ResourceEntry> i = cssResources.values().iterator();
            while (i.hasNext()) {
                ResourceEntry css = i.next();
                String content = css.getContentAsString("UTF-8");
                String dir = toRelativeLocalDir(css.localPath);

                Iterator<String> ri = imageResources.iterator();
                while (ri.hasNext()) {
                    String entry = ri.next();
                    String path = toRelativeLocalPath(entry);
                    if (path.startsWith(dir)) {
                        String relativePath = path.substring(dir.length() + 1);
                        String requestPath = toRequestPath(context, entry);
                        content = content.replaceAll(relativePath, requestPath);
                    }
                }

                css.setContentAsString(content, "UTF-8");
            }

            //add modified css resources
            allResources.addAll(cssResources.keySet());
            //add images
            allResources.addAll(imageResources);

            StringBuffer code = new StringBuffer();
            code.append("window.CKEDITOR_GETURL = function(r) { var mappings = [");
            Iterator<String> entries = allResources.iterator();
            while (entries.hasNext()) {
                String next = entries.next();
                code.append("{i: '");
                code.append(toRelativeLocalPath(next));
                code.append("', o: '");
                code.append(toRequestPath(context, next));
                code.append("'}");
                if (entries.hasNext()) {
                    code.append(",");
                }
            }
            code.append("]; if (r.indexOf('http://') == 0) { var i = document.location.href.lastIndexOf('/'); r = r.substring(i + 1); }; for (var i = 0, l = mappings.length; i < l; i++) { var m = mappings[i]; if (m.i == r) { return m.o;} } return false; };");

            value = code.toString();
            applicationMap.put(InputRichTextResourceHandler.class.getName(), value);
        }

        if (codeResource == null) {
            codeResource = new ResourceEntry(INPUTRICHTEXT_CKEDITOR_DIR + CKEDITOR_MAPPING_JS, value.getBytes("UTF-8"));
        }
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public Resource createResource(String resourceName) {
        return createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        if (codeResource != null && codeResource.localPath != null && codeResource.localPath.equals(resourceName)) {
            //serving up the mapping as a referenced JS resource
            return codeResource;
        } else if (cssResources != null && cssResources.containsKey(resourceName)) {
            //serve the modified CSS resources
            return cssResources.get(resourceName);
        } else {
            //let JSF serve the rest of resources
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead); // write
        }
        out.flush();

        return out.toByteArray();
    }

    private static String toRelativeLocalPath(String localPath) {
        return localPath.substring(INPUTRICHTEXT_CKEDITOR_DIR.length());
    }

    public String toRequestPath(FacesContext context, String localPath) {
        String contextPath = context.getExternalContext().getRequestContextPath();
        return contextPath + ResourceHandler.RESOURCE_IDENTIFIER + "/" + localPath + extensionMapping;
    }

    private String toRelativeLocalDir(String localPath) {
        int position = localPath.lastIndexOf("/");
        return INPUTRICHTEXT_CKEDITOR_DIR.length() > position ? "/" : localPath.substring(INPUTRICHTEXT_CKEDITOR_DIR.length(), position);
    }

    private class ResourceEntry extends Resource {
        Date lastModified = new Date();
        String localPath;
        byte[] content;

        private ResourceEntry(String localPath, byte[] content) {
            this.localPath = localPath;
            this.content = content;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        public Map<String, String> getResponseHeaders() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            HashMap headers = new HashMap();
            headers.put("ETag", eTag());
            headers.put("Cache-Control", "public");
            headers.put("Content-Type", externalContext.getMimeType(localPath));
            headers.put("Date", Util.HTTP_DATE.format(new Date()));
            headers.put("Last-Modified", Util.HTTP_DATE.format(lastModified));

            return headers;
        }

        public String getRequestPath() {
            return toRequestPath(FacesContext.getCurrentInstance(), localPath);
        }

        public URL getURL() {
            try {
                return FacesContext.getCurrentInstance().getExternalContext().getResource(localPath);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            try {
                Date modifiedSince = Util.HTTP_DATE.parse(context.getExternalContext().getRequestHeaderMap().get("If-Modified-Since"));
                return lastModified.getTime() > modifiedSince.getTime() + 1000;
            } catch (Throwable e) {
                return true;
            }
        }

        private String getContentAsString(String encoding) throws UnsupportedEncodingException {
            return new String(content, encoding);
        }

        private void setContentAsString(String newContent, String encoding) throws UnsupportedEncodingException {
            content = newContent.getBytes(encoding);
        }

        private String eTag() {
            return Base64.encode(String.valueOf(localPath.hashCode()));
        }
    }
}
