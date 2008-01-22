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

    private Integer position = null;
    
    private String renderedOnUserRole = null;
   
    private String orientation = null;
    
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
                isHorizontal()? CSS_DEFAULT.PANEL_DIVIDER_HOR_BASE : 
                    CSS_DEFAULT.PANEL_DIVIDER_BASE, "styleClass");
    }
    
    /**
     * <p>Return the value of the <code>firstPane</code> property.</p>
     */
    public String getFirstPaneClass() {
        return Util.getQualifiedStyleClass(this,
                               CSS_DEFAULT.PANEL_DIVIDER_FIRST_PANE);
    }
    
    /**
     * <p>Return the value of the <code>secondPane</code> property.</p>
     */
    public String getSecondPaneClass() {
        return Util.getQualifiedStyleClass(this,
                               CSS_DEFAULT.PANEL_DIVIDER_SECOND_PANE);
    }
    
    /**
     * <p>Return the value of the <code>southClass</code> property.</p>
     */
    public String getSplitterClass() {
        return Util.getQualifiedStyleClass(this,
                               CSS_DEFAULT.PANEL_DIVIDER_SPLITTER);
    }
    
    /**
     * <p>Return the value of the <code>southClass</code> property.</p>
     */
    public String getContainerClass() {
        return Util.getQualifiedStyleClass(this,
                               CSS_DEFAULT.PANEL_DIVIDER_CONTAINER);
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
    
    /**
     * <p>Set the value of the <code>orientation</code> property.</p>
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * <p>Return the value of the <code>orientation</code> property.</p>
     */
    public String getOrientation() {
        if (orientation != null) {
            return orientation;
        }
        ValueBinding vb = getValueBinding("orientation");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "vertical";
    } 
    
    boolean isHorizontal() {
        return "horizontal".equalsIgnoreCase(getOrientation());
    }
    
    /**
     * <p>Set the value of the <code>position</code> property.</p>
     */
    public void setPosition(int position) {
        this.position = new Integer(position);
    }

    /**
     * <p>Return the value of the <code>position</code> property.</p>
     */
    public int getPosition() {
        if (position != null) {
            return position.intValue();
        }
        ValueBinding vb = getValueBinding("position");
        return vb != null ? ((Integer) vb.getValue(getFacesContext())).intValue() : 50;
    }
    
    String getPanePosition(boolean first) {
        int pos = getPosition();
        int panPos = 0;
        if (first) {
            panPos = pos-1;
        } else {
            panPos = 98 - pos;        
        }
        String unit = "height:100%;width:";
        if(isHorizontal()) {
            unit = "width:100%;height:";
        }
        return unit + panPos + "%;";
    }
}
