package com.icesoft.faces.component.paneltooltip;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.w3c.dom.Element;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.DisplayEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.panelpopup.PanelPopup;
import com.icesoft.faces.context.effects.CurrentStyle;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class PanelTooltip extends PanelPopup{

    /**
     * The component type.
     */
    public static final String COMPONENT_TYPE = "com.icesoft.faces.PanelTooltip";
    /**
     * The default renderer type.
     */
    public static final String DEFAULT_RENDERER_TYPE =
            "com.icesoft.faces.PanelTooltipRenderer";

    public static String ICE_TOOLTIP_INFO = "iceTooltipInfo";
    
    private String hoverDelay;
    
    private String hideOn;
    
    private Boolean dynamic; 
    
    private UIComponent tooltipSrcComponent;
    
    private String styleClass = null;
    
    private MethodBinding displayListener;
    
    public PanelTooltip() {
        setRendererType(DEFAULT_RENDERER_TYPE);
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS,
                                     FacesContext.getCurrentInstance());
    }

    
    public void encodeBegin(FacesContext context) throws IOException {
        if ("none".equals(getHideOn())) {
            removeTooltipFromVisibleList(context);
        }
        if (isDynamic() && !isVisible()) {
            return;
        }
        super.encodeBegin(context);
        setValueChangeFired(false);
        
    }
    
    public String getHoverDelay() {
        if (hoverDelay != null) {
            return hoverDelay;
        }
        ValueBinding vb = getValueBinding("hoverDelay");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "500";
    }

    public void setHoverDelay(String hoverDelay) {
        this.hoverDelay = hoverDelay;
    }

    public String getHideOn() {
        if (hideOn != null) {
            return hideOn;
        }
        ValueBinding vb = getValueBinding("hideOn");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "mouseout";
    }

    public void setHideOn(String hideOn) {
        this.hideOn = hideOn;
    }

    /**
     * @return true if the tooltip is dynamic.
     */
    public boolean isDynamic() {
        //if the tooltip is draggable its mean its dynamic as well 
        if (isDraggable() || 
                displayListener != null ||
                getValueBinding("visible") != null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param make tooltip dynamic
     */
    public void setDynamic(boolean dynamic) {
        this.dynamic = Boolean.valueOf(dynamic);
    }    

    public UIComponent getTooltipSrcComponent() {
        if (tooltipSrcComponent != null) {
            return tooltipSrcComponent;
        }
        ValueBinding vb = getValueBinding("tooltipSrcComponent");
        return vb != null ? (UIComponent) vb.getValue(getFacesContext()) : null;
    }

    public void setTooltipSrcComponent(UIComponent tooltipSrcComponent) {
        this.tooltipSrcComponent = tooltipSrcComponent;
    }
    
    /* (non-Javadoc)
     * @see javax.faces.component.html.HtmlPanelGroup#getStyleClass()
     */
     public String getStyleClass() {
         return Util.getQualifiedStyleClass(this, 
                     styleClass,
                     CSS_DEFAULT.TOOLTIP_BASE,
                     "styleClass");
     }
     
     /* (non-Javadoc)
      * @see javax.faces.component.html.HtmlPanelGroup#setStyleClass(java.lang.String)
      */
     public void setStyleClass(String styleClass) {
         this.styleClass = styleClass;
     }     
     
    public void updateModal(FacesContext context) {
        ValueBinding vb = getValueBinding("tooltipSrcComponent");
        if (vb != null) {
            vb.setValue(context, tooltipSrcComponent);                
        }        
    }
    
 
    
    public void processUpdates(FacesContext context) {
            ValueBinding vb = getValueBinding("visible");
            if (vb != null) {
                if (isValueChangeFired()) {
                    vb.setValue(context, new Boolean("show".equals(getTooltipInfo().getState())));
                }
//                else {
//                    vb.setValue(context, new Boolean("show".equals(getState())));
//                }
            } 
            super.processUpdates(context);
    }
    
    public void applyStyle(FacesContext facesContext, Element root) {
        super.applyStyle(facesContext, root);
        String updatedStyle = root.getAttribute(HTML.STYLE_ATTR);
        if (isDynamic() && cssUpdateReceived(facesContext)) {
            String y = getCssPropertyValue (updatedStyle, "top");
            String x = getCssPropertyValue(updatedStyle, "left");
            if (y != null)setTooltipY(y);
            if (x != null)setTooltipX(x);
        }
        if (!isDynamic() && !isInitialized()) {
            updatedStyle = setPropertyValue(updatedStyle, "visibility", "hidden", true);
            updatedStyle = setPropertyValue(updatedStyle, "display", "none", true);            
            setInitialized(true);
        }
        if (isDynamic()) {
            updatedStyle = setPropertyValue(updatedStyle, "position", "absolute", true);
        }
        
        //value change fired must be a dynamic panelPopup
        if (isValueChangeFired() || (isDynamic() 
                && !cssUpdateReceived(facesContext))) {
            setValueChangeFired(false);
            if (getState().equals("show")) {
                updatedStyle = setPropertyValue(updatedStyle, "top", getTooltipY(), true);
                updatedStyle = setPropertyValue(updatedStyle, "left", getTooltipX(), true);            
            }
        }
        
        root.setAttribute(HTML.STYLE_ATTR, updatedStyle);
    }
    
    String setPropertyValue(String css, String propName, String value, boolean add) {
        String[] properties = css.split(";");
        boolean found = false;
        StringBuffer newCss = new StringBuffer();
        for (int i=0; i < properties.length; i++) {
            String[] property = properties[i].split(":");
            if (property.length == 2) {
                if (property[0].equalsIgnoreCase(propName)) {
                    if (add) {
                        found = true;
                        newCss.append(propName + ":"+ value + ";");
                    }
                } else {
                    newCss.append(property[0] + ":" + property[1] + ";");
                }
            }
        }
        if (add && !found) {
            newCss.append(propName + ":" + value + ";");
        }
        return newCss.toString();
    }
    
    String getCssPropertyValue(String css, String propName) {
        String[] properties = css.split(";");
        for (int i=0; i < properties.length; i++) {
            String[] property = properties[i].split(":");
            if (property.length == 2) {
                if (property[0].equalsIgnoreCase(propName)) {
                    return property[1];
                }
            }
        }
        return null;
    }
    void setInitialized(boolean initialized) {
        this.getAttributes().put("comp-initialized", String.valueOf(initialized));
    }
    
    boolean isInitialized() {
        return Boolean.getBoolean(String.valueOf(this.getAttributes().get("comp-initialized")));
    }
    
    private boolean cssUpdateReceived(FacesContext facesContext) {
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestMap == null || 
                    !requestMap.containsKey(CurrentStyle.CSS_UPDATE_FIELD)) {
            return false;
        }
        String CSS_UPDATE = String.valueOf(requestMap.get(CurrentStyle.CSS_UPDATE_FIELD));
        String clientId = getClientId(facesContext);
        if (CSS_UPDATE.startsWith(clientId)){
            return true;
        }
        
        return false;
    }
    
    public void decode(FacesContext context) {
        super.decode(context);

    }
    
    public static void decodeTooltip(FacesContext facesContext, 
                                                UIComponent target) {
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestMap.containsKey(ICE_TOOLTIP_INFO)) {
            populateTooltipInfo(facesContext,
                                target,                   
                                String.valueOf(requestMap.get(ICE_TOOLTIP_INFO)));
        }
    }
    
    public static void populateTooltipInfo(FacesContext facesContext,
                                            UIComponent target,
                                            String tooltipinfo
            ) {

        String tooltipId = ((HtmlPanelGroup)target).getPanelTooltip();
        UIComponent tooltipComponent = D2DViewHandler.findComponent(tooltipId, target);
        String tooltipClientId = tooltipComponent.getClientId(facesContext);
        String[] entries = tooltipinfo.split(";");
        if (entries.length == 5){
            if (!entries[0].split("=")[1].equals(tooltipClientId)) return;
                TooltipInfo tooltipInfo = getTooltipInfo(tooltipComponent,
                        tooltipClientId);
                tooltipInfo.populateValues(entries);
                if (entries[1].split("=")[1].equals(target.getClientId(facesContext))) {
                    target.queueEvent(new DisplayEvent(tooltipComponent,
                            target,
                            "This is value",
                            "show".equalsIgnoreCase(tooltipInfo.getState())
                            ));
                }
                
        }
    }
    
    String getState() {
        ValueBinding vb = getValueBinding("state");
        return vb != null ? (String) vb.getValue(getFacesContext()):
                            getTooltipInfo().getState();
    }
    
    void setState(String state) {
        getTooltipInfo().setState(state);
    }
    
    String getTooltipSrcComp() {
        return getTooltipInfo().getSrc();
    }
    
    String getTooltipX() {
        return getTooltipInfo().getX();
    }
    
    String getTooltipY() {
        return getTooltipInfo().getY();
    }
    
    void setTooltipX(String x) {
        getTooltipInfo().setX(x);
    }
    
    void setTooltipY(String y) {
        getTooltipInfo().setY(y);
    }
    
    void setValueChangeFired (boolean eventFired) {
        getTooltipInfo().setEventFired(eventFired);
    }
    
    boolean isValueChangeFired () {
        return getTooltipInfo().isEventFired();
    }
    
   boolean isDraggable() {
       return "true".equalsIgnoreCase(getDraggable());
   }

   void removeTooltipFromVisibleList(FacesContext facesContext) {
       boolean oldValue = "show".equals(getTooltipInfo().getState());
       boolean show = isVisible();
       if (!show && oldValue) {
           //app is trying to hide the tooltip, synch the client
           setState(isVisible()?"show":"hide");
           JavascriptContext.addJavascriptCall(facesContext, "ToolTipPanelPopupUtil.removeFromVisibleList('"+ getClientId(facesContext)+"');");
       }
   }
   
   public static TooltipInfo getTooltipInfo(UIComponent tooltipComponent, String tooltipClientId) {
       if (!tooltipComponent.getAttributes().containsKey("tooltip"+ tooltipClientId)) {
           tooltipComponent.getAttributes().put("tooltip"+ tooltipClientId, new TooltipInfo());
       }
       return ((TooltipInfo)tooltipComponent.getAttributes().get("tooltip"+ tooltipClientId));
   }
   
   TooltipInfo getTooltipInfo() {
       if (!this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
           this.getAttributes().put("tooltip"+ getClientId(getFacesContext()), new TooltipInfo());
       }
       return ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext())));
   }
    
    /**
     * <p>Return the value of the <code>displayListener</code> property.</p>
     */
    public MethodBinding getDisplayListener() {
        return displayListener;
    }

    /**
     * <p>Set the value of the <code>displayListener</code> property.</p>
     */
    public void setDisplayListener(MethodBinding displayListener) {
        this.displayListener = displayListener;
    }
    
   
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        
        if (displayListener != null) {
            Object[] displayEvent = {(DisplayEvent) event};
            displayListener.invoke(getFacesContext(), displayEvent);
        }
    }
    
    /**
     * <p>Return the value of the <code>visible</code> property.</p>
     */
    public boolean isVisible() {
        if (visible != null) {
            return visible.booleanValue();
        }
        ValueBinding vb = getValueBinding("visible");
        Boolean boolVal =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return boolVal != null ? boolVal.booleanValue() : "show".equals(getTooltipInfo().getState());
    }
}
