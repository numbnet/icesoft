package com.icesoft.faces.component.paneltooltip;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import org.w3c.dom.Element;

import com.icesoft.faces.application.D2DViewHandler;
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
    
    private String autoHide;
    
    private Boolean dynamic; 
    
    private UIComponent tooltipSrcComponent;
    
    public PanelTooltip() {
        setRendererType(DEFAULT_RENDERER_TYPE);
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS,
                                     FacesContext.getCurrentInstance());
    }

    
    public void encodeBegin(FacesContext context) throws IOException {
        if (isDynamic() && !getTooltipState().equals("show")) {
            return;
        }
        super.encodeBegin(context);
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

    public String getAutoHide() {
        if (autoHide != null) {
            return autoHide;
        }
        ValueBinding vb = getValueBinding("autoHide");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "onExit";
    }

    public void setAutoHide(String autoHide) {
        this.autoHide = autoHide;
    }

    /**
     * @return true if the tooltip is dynamic.
     */
    public boolean isDynamic() {
        //if the tooltip is draggable its mean its dynamic as well 
        if (isDraggable()) return true;
        if (dynamic != null) {
            return dynamic.booleanValue();
        }
        ValueBinding vb = getValueBinding("dynamic");
        Boolean boolVal =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return boolVal != null ? boolVal.booleanValue() : false;
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
    
    public void updateModal(FacesContext context) {
        ValueBinding vb = getValueBinding("tooltipSrcComponent");
        if (vb != null) {
            vb.setValue(context, tooltipSrcComponent);                
        }        
    }
    
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
            if (event instanceof ValueChangeEvent) {
                FacesContext context = getFacesContext();
                updateModal(context);
                MethodBinding method = getValueChangeListener();
                if (method != null) {
                    method.invoke(context, new Object[] { event });
                }
        }
    }    
    
    public void addValueChangeListener(ValueChangeListener listener) {
        addFacesListener(listener);
    }


    public ValueChangeListener[] getValueChangeListeners() {
        ValueChangeListener vcl[] = (ValueChangeListener [])
        getFacesListeners(ValueChangeListener.class);
        return (vcl);
    }


    public void removeValueChangeListener(ValueChangeListener listener) {
        removeFacesListener(listener);
    }

    
    private MethodBinding valueChangeMethod = null;


    public MethodBinding getValueChangeListener() {
        return (this.valueChangeMethod);
    }


    public void setValueChangeListener(MethodBinding valueChangeMethod) {
        this.valueChangeMethod = valueChangeMethod;
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
            if (getTooltipState().equals("show")) {
                updatedStyle = setPropertyValue(updatedStyle, "top", getTooltipY(), true);
                updatedStyle = setPropertyValue(updatedStyle, "left", getTooltipX(), true);            
            }
        }
        
        root.setAttribute(HTML.STYLE_ATTR, updatedStyle);
    }
    
    //add true= replace or add, false = remove
    String setPropertyValue(String css, String propName, String value, boolean add) {
        String[] properties = css.split(";");
        boolean found = false;
        StringBuffer newCss = new StringBuffer();
        for (int i=0; i < properties.length; i++) {
            String[] property = properties[i].split(":");
            if (property.length == 2) {
                if (property[0].equalsIgnoreCase(propName)) {
                  //if found and "add" is true then replace the value, otherwise 
                  //don't add it into the new css, must need to be removed
                    if (add) {
                        found = true;
                        newCss.append(propName + ":"+ value + ";");
                    }
                } else {
                    newCss.append(property[0] + ":" + property[1] + ";");
                }
            }
        }
        if (add && !found) {//its a new entry add it
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
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        if (requestMap.containsKey(ICE_TOOLTIP_INFO)) {
            populateTooltipInfo(String.valueOf(requestMap.get(ICE_TOOLTIP_INFO)));
        }
    }
    
    void populateTooltipInfo(String tooltipinfo) {
        String[] entries = tooltipinfo.split(";");
        if (entries.length == 5){
            if (!entries[0].split("=")[1].equals(getClientId(getFacesContext()))) return;
                if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))){
                    ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).populateValues(entries);
                } else {
                    this.getAttributes().put("tooltip"+ getClientId(getFacesContext()), new TooltipInfo(entries));    
                }
        }
    }
    
    String getTooltipState() {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            return ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).getState();
        }
        return "hide";
    }
    
    String getTooltipSrcComp() {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            return ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).getSrc();
        }
        return null;
    }
    
    String getTooltipX() {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            return ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).getX();
        }
        return "100";
    }
    
    String getTooltipY() {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            return ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).getY();
        }
        return "100";
    }
    
    void setTooltipX(String x) {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).setX(x);
        }
    }
    
    void setTooltipY(String y) {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).setY(y);
        }
    }
    
    void setValueChangeFired (boolean eventFired) {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).setEventFired(eventFired);
        }
    }
    
    boolean isValueChangeFired () {
        if (this.getAttributes().containsKey("tooltip"+ getClientId(getFacesContext()))) {
            return ((TooltipInfo)this.getAttributes().get("tooltip"+ getClientId(getFacesContext()))).isEventFired();
        }
        return false;
    }
    
   boolean isDraggable() {
       return "true".equalsIgnoreCase(getDraggable());
   }
    class TooltipInfo {
        private String src = new String();
        private String state = "hide";
        private String x;
        private String y;
        private boolean eventFired;
        
        public TooltipInfo(String info[]) {
            populateValues(info);
        }
    
        public void populateValues(String info[]) {
            String _src = info[1].split("=")[1];
            String _state = info[2].split("=")[1];
            if (!getState().equals(_state) || !getSrc().equals(_src)) {
                //change the x and y only on valueChangeEvent
                x = info[3].split("=")[1]+"px";
                y = info[4].split("=")[1]+"px";
                src = _src;
                state = _state;
                
                System.out.println("Event has queed");
                eventFired = true;
                queueEvent(new ValueChangeEvent(PanelTooltip.this, getState(), _state));
                FacesContext context = getFacesContext();
                UIComponent srcComp = D2DViewHandler.findComponent(getSrc(), context.getViewRoot());
                setTooltipSrcComponent(srcComp);

            }
        }
        public String getSrc() {
            return src;
        }
    
        public void setSrc(String src) {
            this.src = src;
        }
    
        public String getState() {
            return state;
        }
    
        public void setState(String state) {
            this.state = state;
        }
    
        public String getX() {
            return x;
        }
    
        public void setX(String x) {
            this.x = x;
            System.out.println("setting X = "+ x);            
        }
    
        public String getY() {
            return y;
        }
    
        public void setY(String y) {
            this.y = y;
            System.out.println("setting Y = "+ y);
        }

        public boolean isEventFired() {
            return eventFired;
        }

        public void setEventFired(boolean eventFired) {
            this.eventFired = eventFired;
        }
    }    
}
