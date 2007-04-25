package com.icesoft.faces.component.accordion;

import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.component.ext.taglib.Util;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.component.UIComponentBase;

public class PanelAccordion extends UIComponentBase {
    public static final String COMPONENET_TYPE = "com.icesoft.faces.Accordion";
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.AccordionRenderer";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.AccordionFamily";

    private String label;
    private Boolean expanded;
    private MethodBinding actionListener;
    private String styleClass;
    private Boolean toogleOnClick;
    private Boolean disabled;
     /**
     * The current enabledOnUserRole state.
     */
    private String enabledOnUserRole = null;
    /**
     * The current renderedOnUserRole state.
     */
    private String renderedOnUserRole = null;


    public PanelAccordion() {
        super();
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS, FacesContext.getCurrentInstance());

    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }


    public String getStyleClass() {
        ValueBinding vb = getValueBinding("styleClass");
       if (vb != null) {
           return (String) vb.getValue(getFacesContext());
       }
       if (styleClass != null) {
           return styleClass;
       }
       return null;

    }

    public void setStyleClass(String styleClass) {
        ValueBinding vb = getValueBinding("styleClass");
        if (vb != null) {
            vb.setValue(getFacesContext(), styleClass);
        } else {
            this.styleClass = styleClass;
        }

    }

    public String getLabel() {
         ValueBinding vb = getValueBinding("label");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext());
        }
        if (label != null) {
            return label;
        }
        return null;
    }

    public void setLabel(String label) {
        ValueBinding vb = getValueBinding("label");
        if (vb != null) {
            vb.setValue(getFacesContext(), label);
        } else {
            this.label = label;
        }
    }


    public Boolean getExpanded() {
         ValueBinding vb = getValueBinding("expanded");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        if (expanded!= null) {
            return expanded;
        }
        return Boolean.FALSE;
    }

    public void setExpanded(Boolean expanded) {
        ValueBinding vb = getValueBinding("expanded");
        if (vb != null) {
            vb.setValue(getFacesContext(), expanded);
        } else {
            this.expanded = expanded;
        }
    }

    public Boolean getToogleOnClick() {
         ValueBinding vb = getValueBinding("toogleOnClick");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        if (toogleOnClick!= null) {
            return toogleOnClick;
        }
        return Boolean.TRUE;
    }

    public void setToogleOnClick(Boolean toogleOnClick) {
        ValueBinding vb = getValueBinding("toogleOnClick");
        if (vb != null) {
            vb.setValue(getFacesContext(), toogleOnClick);
        } else {
            this.toogleOnClick= toogleOnClick;
        }
    }

    public MethodBinding getActionListener() {
        return actionListener;
    }

    public void setActionListener(MethodBinding actionListener) {
        this.actionListener = actionListener;
    }

    public void broadcast(FacesEvent event) {
        super.broadcast(event);
        if (event instanceof ActionEvent && actionListener != null) {

            actionListener.invoke(getFacesContext(),
                                     new Object[]{(ActionEvent) event});

        }
    }

      /**
     * @param disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = new Boolean(disabled);
        ValueBinding vb = getValueBinding("disabled");
        if (vb != null) {
            vb.setValue(getFacesContext(), this.disabled);
            this.disabled = null;
        }
    }

    /**
     * @return the value of disabled
     */
    public boolean isDisabled() {
        if (!Util.isEnabledOnUserRole(this)) {
            return true;
        }

        if (disabled != null) {
            return disabled.booleanValue();
        }
        ValueBinding vb = getValueBinding("disabled");
        Boolean v =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

      /**
     * @param enabledOnUserRole
     */
    public void setEnabledOnUserRole(String enabledOnUserRole) {
        this.enabledOnUserRole = enabledOnUserRole;
    }

    /**
     * @return the value of enabledOnUserRole
     */
    public String getEnabledOnUserRole() {
        if (enabledOnUserRole != null) {
            return enabledOnUserRole;
        }
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * @param renderedOnUserRole
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * @return the value of renderedOnUserRole
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }



    public Object saveState(FacesContext context) {
        Object[] state = new Object[12];
        state[0] = super.saveState(context);
        state[1] = label;
        state[2] = expanded;
        state[3] = actionListener;
        state[4] = styleClass;
        state[5] = disabled;
        state[6] = enabledOnUserRole;
        state[7] = renderedOnUserRole;
        return state;
    }

    public void restoreState(FacesContext context, Object stateIn) {

        Object[] state = (Object[]) stateIn;
        super.restoreState(context, state[0]);
        label = (String)state[1];
        expanded= (Boolean)state[2];
        actionListener = (MethodBinding)state[3];
        styleClass = (String)state[4];
        disabled = (Boolean)state[5];
        enabledOnUserRole = (String)state[6];
        renderedOnUserRole = (String)state[7];

    }


    


    public Object processSaveState(FacesContext facesContext) {
        return super.processSaveState(facesContext);
    }

    public void processRestoreState(FacesContext facesContext, Object object) {
        super.processRestoreState(facesContext, object);
    }
}