package com.icesoft.faces.component.inputrichtext;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.JarResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceLinker;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.util.CoreUtils;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InputRichText extends UIInput {
    public static final String COMPONENET_TYPE = "com.icesoft.faces.InputRichText";
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.InputRichTextRenderer";
    private static final Resource ICE_FCK_EDITOR_JS = new JarResource("com/icesoft/faces/component/inputrichtext/fckeditor_ext.js");
    private static final Resource FCK_EDITOR_JS = new JarResource("com/icesoft/faces/component/inputrichtext/fckeditor.js");
    private static final String FCK_EDITOR_ZIP = "com/icesoft/faces/component/inputrichtext/fckeditor.zip";
    private static final Date lastModified = new Date();
    private static final Map ZipEntryCache = new HashMap();

    static {
            try {
            InputStream in = InputRichText.class.getClassLoader().getResourceAsStream(FCK_EDITOR_ZIP);
                ZipInputStream zip = new ZipInputStream(in);
                ZipEntry entry;
                while ((entry = zip.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                    ZipEntryCache.put(entry.getName(), toByteArray(zip));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ResourceLinker.Handler FCK_LINKED_BASE = new ResourceLinker.Handler() {
        public void linkWith(ResourceLinker linker) {
            Iterator i = ZipEntryCache.keySet().iterator();
            while (i.hasNext()) {
                final String entryName = (String) i.next();
                        linker.registerRelativeResource(entryName, new Resource() {
                            public String calculateDigest() {
                        return String.valueOf(FCK_EDITOR_ZIP + entryName);
                            }

                            public Date lastModified() {
                                return lastModified;
                            }

                            public InputStream open() throws IOException {
                        return new ByteArrayInputStream((byte[]) ZipEntryCache.get(entryName));
                            }

                            public void withOptions(Resource.Options options) {
                                options.setFileName(entryName);
                                options.setLastModified(lastModified);
                            }
                        });
                    }
                }
    };

    public static void loadFCKJSIfRequired() {
        if (FacesContext.getCurrentInstance() != null && baseURI == null && exist.booleanValue()) {
            ResourceRegistry registry =
                    (ResourceRegistry) FacesContext.getCurrentInstance();
            if (registry != null) {
                baseURI = registry.loadJavascriptCode(FCK_EDITOR_JS, FCK_LINKED_BASE);
                registry.loadJavascriptCode(ICE_FCK_EDITOR_JS);
            } else {
                //LOG fckeditor's library has not loaded, component will not work as desired
            }
        }
    }

    private String language;
    private String _for;
    private String style;
    private String styleClass;
    private String width;
    private String height;
    private static URI baseURI = null;
    private static Boolean exist = Boolean.FALSE;
    private String toolbar;
    private String customConfigPath;
    private Boolean disabled = null;
    private String skin = null;
    private Boolean saveOnSubmit = null;

    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }

    public String getComponentType() {
        return COMPONENET_TYPE;
    }

    public InputRichText() {
        //the following static variables are used, so the library can be load 
        //for each separate views 
        baseURI = null;
        exist = Boolean.TRUE;
    }

    public void decode(FacesContext facesContext) {
        Map map = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(facesContext);
        if (map.containsKey(clientId)) {
            String newValue = map.get(clientId).toString().replace('\n', ' ');
            setSubmittedValue(newValue);
        }
        super.decode(facesContext);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }

    /**
     * <p>Set the value of the <code>language</code> property.</p>
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * <p>Return the value of the <code>language</code> property.</p>
     */
    public String getLanguage() {
        if (language != null) {
            return language;
        }
        ValueBinding vb = getValueBinding("language");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "en";
    }

    /**
     * <p>Set the value of the <code>for</code> property.</p>
     */
    public void setFor(String _for) {
        this._for = _for;
    }

    /**
     * <p>Return the value of the <code>language</code> property.</p>
     */
    public String getFor() {
        if (_for != null) {
            return _for;
        }
        ValueBinding vb = getValueBinding("for");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "";
    }

    boolean isToolbarOnly() {
        return false;
    }

    public URI getBaseURI() {
        if (baseURI == null)
            loadFCKJSIfRequired();
        return baseURI;
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }


    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this,
                styleClass,
                CSS_DEFAULT.INPUT_RICH_TEXT,
                "styleClass");

    }

    /**
     * <p>Set the value of the <code>width</code> property.</p>
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * <p>Return the value of the <code>width</code> property.</p>
     */
    public String getWidth() {
        if (width != null) {
            return width;
        }
        ValueBinding vb = getValueBinding("width");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "100%";
    }

    /**
     * <p>Set the value of the <code>height</code> property.</p>
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * <p>Return the value of the <code>height</code> property.</p>
     */
    public String getHeight() {
        if (height != null) {
            return height;
        }
        ValueBinding vb = getValueBinding("height");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "200";
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
        return output.toByteArray();
    }

    /**
     * <p>Set the value of the <code>toolbar</code> property.</p>
     */
    public void setToolbar(String toolbar) {
        this.toolbar = toolbar;
    }

    /**
     * <p>Return the value of the <code>toolbar</code> property.</p>
     */
    public String getToolbar() {
        if (toolbar != null) {
            return toolbar;
        }
        ValueBinding vb = getValueBinding("toolbar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "Default";
    }

    /**
     * <p>Set the value of the <code>customConfigPath</code> property.</p>
     */
    public void setCustomConfigPath(String customConfigPath) {
        this.customConfigPath = customConfigPath;
    }

    /**
     * <p>Return the value of the <code>customConfigPath</code> property.</p>
     */
    public String getCustomConfigPath() {
        if (customConfigPath != null) {
            return CoreUtils.resolveResourceURL(getFacesContext(), customConfigPath);
        }
        ValueBinding vb = getValueBinding("customConfigPath");
        return vb != null ? CoreUtils.resolveResourceURL(getFacesContext(), (String) vb.getValue(getFacesContext())) : null;
    }

    /**
     * <p>Set the value of the <code>disabled</code> property.</p>
     */
    public void setDisabled(boolean disabled) {
        this.disabled = new Boolean(disabled);
    }

    /**
     * <p>Return the value of the <code>disabled</code> property.</p>
     */
    public boolean isDisabled() {
        if (disabled != null) {
            return disabled.booleanValue();
        }
        ValueBinding vb = getValueBinding("disabled");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext()))
                .booleanValue() : false;
    }

    /**
     * <p>Set the value of the <code>skin</code> property.</p>
     */
    public void setSkin(String skin) {
        this.skin = skin;
    }

    /**
     * <p>Return the value of the <code>skin</code> property.</p>
     */
    public String getSkin() {
        if (skin != null) {
            return skin;
        }
        ValueBinding vb = getValueBinding("skin");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "default";
    }

    /**
     * <p>Set the value of the <code>saveOnSubmit</code> property.</p>
     */
    public void setSaveOnSubmit(boolean saveOnSubmit) {
        this.saveOnSubmit = new Boolean(saveOnSubmit);
    }

    /**
     * <p>Return the value of the <code>saveOnSubmit</code> property.</p>
     */
    public boolean isSaveOnSubmit() {
        if (saveOnSubmit != null) {
            return saveOnSubmit.booleanValue();
        }
        ValueBinding vb = getValueBinding("saveOnSubmit");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext()))
                .booleanValue() : false;
    }
    
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        if (event instanceof ValueChangeEvent) {
            FacesContext facesContext = getFacesContext();
            JavascriptContext.addJavascriptCall(facesContext, "Ice.FCKeditorUtility.updateValue ('" + getClientId(facesContext) + "');");            
        }
        super.broadcast(event);
    }    
}