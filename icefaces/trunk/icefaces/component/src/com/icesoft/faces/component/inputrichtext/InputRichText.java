package com.icesoft.faces.component.inputrichtext;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.PORTLET_CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.JarResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.effects.JavascriptContext;

public class InputRichText extends UIInput{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.InputRichText";
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.InputRichTextRenderer";
    private String language;
    private String _for;
    private Boolean html; 
    private Boolean toolbarOnly;   
    private String style;
    private String styleClass;
    private String width;
    private String height;
    
	private final Resource FCK_EDITOR_BASE = new 
	 JarResource("com/icesoft/faces/component/inputrichtext/fckeditor.zip");
	private final Resource ICE_FCK_EDITOR_JS = new 
	 JarResource("com/icesoft/faces/component/inputrichtext/fckeditor_ext.js");	
	private URI baseURI;
	 
    public InputRichText() {
    	ResourceRegistry registry = (ResourceRegistry) 
    	FacesContext.getCurrentInstance();
		try {
	    	if (registry != null) {			
				baseURI = registry.registerZippedResources(FCK_EDITOR_BASE);
				String path = baseURI + "/fckeditor.js";
				path = path.substring(path.indexOf("block"), path.length());
				JavascriptContext.includeLib(path, FacesContext.getCurrentInstance());
				
				URI jsURI = registry.registerResource("application/x-javascript",ICE_FCK_EDITOR_JS);
				path = jsURI.getPath();
				path = path.substring(path.indexOf("block"), path.length());
				JavascriptContext.includeLib(path, FacesContext.getCurrentInstance());
	    	} else {
	    		//LOG fckeditor's library has not loaded, component will not work as desired
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }
    
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public void decode(FacesContext facesContext) {
		 Map map = facesContext.getExternalContext().getRequestParameterMap();
		 String clientId = getClientId(facesContext);
		 if (map.containsKey(clientId)) {
			 String newValue = map.get(clientId).toString().replace('\n', ' ');
			 System.out.println(newValue);
			 setSubmittedValue(newValue);			 
		 }
		 super.decode(facesContext);
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
        return vb != null ? (String) vb.getValue(getFacesContext()) : "da";
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
	
	public boolean isToolbarOnly() {
        if (toolbarOnly != null) {
            return toolbarOnly.booleanValue();
        }
        ValueBinding vb = getValueBinding("toolbarOnly");
        return vb != null ?
               ((Boolean) vb.getValue(getFacesContext())).booleanValue() :
               false;
	}

	public void setToolbarOnly(boolean toolbarOnly) {
		this.toolbarOnly = new Boolean(toolbarOnly);
	}
	public URI getBaseURI() {
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
}
