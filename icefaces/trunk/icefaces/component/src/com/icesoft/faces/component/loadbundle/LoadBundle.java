package com.icesoft.faces.component.loadbundle;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.utils.MessageUtils;

public class LoadBundle extends UIOutput{
    public static final String COMPONENET_TYPE = "com.icesoft.faces.LoadBundle";
    private String basename;
    transient private String oldBasename = new String();
    private String var;
    transient private ResourceBundle bundle;

    
    public LoadBundle() {
        setRendererType(null);
    }
    
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        String newBasename = getBasename();
        if (!oldBasename.equals(newBasename)) {
            bundle = ResourceBundle.getBundle(newBasename.trim(),
                    context.getViewRoot().getLocale(),
                    MessageUtils.getClassLoader(this));   
            context.getExternalContext().getRequestMap().put(getVar(), bundle); 
        }
        oldBasename = newBasename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getBasename() {
        if (basename != null) {
            return basename;
        }
        ValueBinding vb = getValueBinding("basename");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
