package com.icesoft.faces.component.paneldivider;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.effects.JavascriptContext;

public class PanelDivider extends UIPanel{
    private final Log log = LogFactory.getLog(PanelDivider.class);

    public static final String COMPONENT_TYPE = "com.icesoft.faces.PanelDivider";

    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.PanelDividerRenderer";

    private static final String INVALID_POSITION = " is invalid value for the position. " +
    "The valid position is between  1 to 100. The default position is " +
    "being applied [50]";
    
    public static final String FIRST_PANL_STYLE= "FirstPane";
    
    public static final String SECOND_PANL_STYLE= "SecondPane";
    
    public static final String IN_PERCENT= "InPercent";
    
    private String style = null;
    
    private String styleClass =  null;

    private Integer dividerPosition  = null;
    
    private String renderedOnUserRole = null;
   
    private String orientation = null;
    
    private boolean decoded = false;
    
    private String firstPaneStyle;
    private String secondPaneStyle;
    private int DEFAULT_POSITION = 50;
    private int submittedDividerPosition = -1;
    private int previousDividerPosition = -1;
    
    public PanelDivider() {
        setRendererType(DEFAULT_RENDERER_TYPE);
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS,
                                     FacesContext.getCurrentInstance());
    }

    public void decode(FacesContext facesContext) {
        Map map = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(facesContext);
        if (map.containsKey(clientId + FIRST_PANL_STYLE)) {
            firstPaneStyle = String.valueOf(map.get(clientId + FIRST_PANL_STYLE));
            secondPaneStyle = String.valueOf(map.get(clientId + SECOND_PANL_STYLE)); 
        }
        if (map.containsKey(clientId + IN_PERCENT) 
                && map.get(clientId + IN_PERCENT) != null && 
                        !"".equals(map.get(clientId + IN_PERCENT))) {
            submittedDividerPosition =  Integer.valueOf(String.valueOf(map.get(clientId + IN_PERCENT))).intValue();
            DEFAULT_POSITION = submittedDividerPosition;
            decoded = true;
        } else {
            decoded = false;
        }
        previousDividerPosition = getDividerPosition();
        super.decode(facesContext);
    }
    
    public void encodeBegin(FacesContext facesContext) throws IOException {

        super.encodeBegin(facesContext);
    }
    
   
    public void processUpdates(FacesContext context) {
        ValueBinding vb = getValueBinding("dividerPosition");
        if (decoded) {
            if (vb != null) {
                vb.setValue(context, new Integer(submittedDividerPosition));
            } 
            if (dividerPosition != null) {
                dividerPosition = new Integer(submittedDividerPosition);
            }
        }
        int pos = getDividerPosition();
        if (!validatePosition(pos)) {
            FacesMessage message = new FacesMessage("["+ pos + "] "+ INVALID_POSITION);;
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(getClientId(context), message);
            
            log.info("["+ pos + "] "+ INVALID_POSITION);
        }
        super.processUpdates(context);
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
     * <p>Set the value of the <code>dividerPosition</code> property.</p>
     */
    public void setDividerPosition(int dividerPosition) {
        this.dividerPosition = new Integer(dividerPosition);
    }

    /**
     * <p>Return the value of the <code>dividerPosition</code> property.</p>
     */
    public int getDividerPosition() {
        if (dividerPosition != null) {
            return dividerPosition.intValue();
        }
        ValueBinding vb = getValueBinding("dividerPosition");
        return vb != null ? ((Integer) vb.getValue(getFacesContext())).intValue() : DEFAULT_POSITION;
    }
    
    String getPanePosition(boolean first) {
        if (!decoded && getDividerPosition() != previousDividerPosition) {
            int pos = getDividerPosition();
            if (!validatePosition(pos)) pos = 50;
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
        } else {
            if (first) {
                return firstPaneStyle;
            } else {
                return secondPaneStyle;
            }
        }
    }
    
    private boolean validatePosition(int position ) {
        return (position > 0  && position <= 100);
    }
    
}
