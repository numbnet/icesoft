package com.icesoft.faces.component.paneldivider;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.effects.JavascriptContext;

public class PanelDivider extends UIPanel{
    /**
     * The component type.
     */
    public static final String COMPONENT_TYPE = "com.icesoft.faces.PanelDivider";
    /**
     * The default renderer type.
     */
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.PanelDividerRenderer";

    private String style = null;
    
    private String styleClass =  null;
    
    private String renderedOnUserRole = null;
    
    public PanelDivider() {
        setRendererType(DEFAULT_RENDERER_TYPE);
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS,
                                     FacesContext.getCurrentInstance());
    }
    
    /**
     * @return the "first" facet.
     */
    UIComponent getFirstFacet() {
        return (UIComponent) getFacet("first");
    }

    /**
     * @return the "second" facet.
     */
    UIComponent getSecondFacet() {
        return (UIComponent) getFacet("second");
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
    
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, styleClass,
                CSS_DEFAULT.PANEL_DIVIDER_BASE, "styleClass");
    }
    
    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }    
}
