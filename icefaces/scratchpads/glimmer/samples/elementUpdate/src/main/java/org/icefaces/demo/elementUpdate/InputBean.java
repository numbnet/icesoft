package org.icefaces.demo.elementUpdate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "InputBean")
@SessionScoped
public class InputBean {
    private String className = "B";
    private String title = "A";
    private String lang = "en";
    private String name = "A";
    private String value = "A";
    private String size = "15";
    private String src = "A.gif";
    private String alt = "A";
    private String style = "";
    private boolean checked, disabled, readOnly;

    public void toggleClassName() {
        className = "A".equals(className) ? "B" : "A";
    }

    public void toggleTitle() {
        title = "A".equals(title) ? "B" : "A";
    }

    public void toggleLang() {
        lang = "en".equals(lang) ? "fr" : "en";
    }

    public void toggleName() {
        name = "A".equals(name) ? "B" : "A";
    }

    public void toggleValue() {
        value = "A".equals(value) ? "B" : "A";
    }

    public void toggleAlt() {
        alt = "A".equals(alt) ? "B" : "A";
    }

    public void toggleSrc() {
        src = "A.gif".equals(src) ? "B.gif" : "A.gif";
    }

    public void toggleChecked() {
        checked = !checked;
    }

    public void toggleDisabled() {
        disabled = !disabled;
    }

    public void toggleReadOnly() {
        readOnly = !readOnly;
    }

    public void toggleSize() {
        size = "15".equals(size) ? "40" : "15";
    }

    public void toggleStyle() {
        style = "".equals(style) ? "background-color: yellow; font-weight: bold;" : "";
    }

    //------------------

    public String getClassName() {
        return className;
    }

    public String getTitle() {
        return title;
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSize() {
        return size;
    }

    public String getSrc() {
        return src;
    }

    public String getAlt() {
        return alt;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public String getStyle() {
        return style;
    }
}
