package com.icesoft.faces.component.ext;


import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class OutputBody extends javax.faces.component.UIComponentBase{
    private String alink;
    private String background;
    private String bgcolor;
    private String link;
    private String style;
    private String styleClass;
    private String text;
    private String vlink;

    public OutputBody() {
        super();
        setRendererType("com.icesoft.faces.OutputBody");
    }
    /**
     * <p>Return the family for this component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.OutputBody";
    }

    public String getAlink() {
        return (String) getAttribute("alink", alink, null);
    }

    public void setAlink(String alink) {
        this.alink = alink;
    }

    public String getBackground() {
        return (String) getAttribute("background", background, null);
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBgcolor() {
        return (String) getAttribute("bgcolor", bgcolor, null);
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getLink() {
        return (String) getAttribute("link", link, null);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStyle() {
        return (String) getAttribute("style", style, null);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return (String) getAttribute("styleClass", styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getText() {
        return (String) getAttribute("text", text, null);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVlink() {
        return (String) getAttribute("vlink", vlink, null);
    }

    public void setVlink(String vlink) {
        this.vlink = vlink;
    }

    private Object getAttribute(String name, Object localValue, Object defaultValue) {
        if (localValue != null) return localValue;
        ValueBinding vb = getValueBinding(name);
        if (vb == null) return defaultValue;
        Object value = vb.getValue(getFacesContext());
        if (value == null) return defaultValue;
        return value;
    }
    
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                alink,
                background,
                bgcolor,
                link,
                style,
                styleClass,
                text,
                vlink,
        };
    }

    public void restoreState(FacesContext context, Object state) {
        String[] attrNames = {
                "alink",
                "background",
                "bgcolor",
                "link",
                "style",
                "styleClass",
                "text",
                "vlink",
        };
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        for (int i = 0; i < attrNames.length; i++) {
            getAttributes().put(attrNames[i], values[i + 1]);
        }
    }
}




