package com.icesoft.faces.component.loadbundle;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.utils.MessageUtils;

public class LoadBundle extends UIOutput{
    public static final String COMPONENET_TYPE = "com.icesoft.faces.LoadBundle";
    private String basename;
    private String var;
    transient private Locale oldLocale;
    transient private String oldBasename = new String();
    transient private ResourceBundle bundle;
    
    
    public LoadBundle() {
        setRendererType(null);
    }
    
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        String newBasename = getBasename();
        Locale currentLocale = context.getViewRoot().getLocale();
        boolean reloadRequired = !((oldLocale != null) && 
                oldLocale.getLanguage().equals(currentLocale.getLanguage()))
            || !oldBasename.equals(newBasename);
        if (reloadRequired) {
            bundle = ResourceBundle.getBundle(newBasename.trim(),
                    currentLocale,
                    MessageUtils.getClassLoader(this));   
            context.getExternalContext().getRequestMap().put(getVar(), bundle); 
        }
        oldBasename = newBasename;
        oldLocale = currentLocale;
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
    
    private transient Object values[];
    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        if(values == null){
            values = new Object[3];
        }
        values[0] = super.saveState(context);
        values[1] = basename;
        values[2] = var;
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        basename = (String) values[1];
        var = (String) values[2];        
    }    
}
