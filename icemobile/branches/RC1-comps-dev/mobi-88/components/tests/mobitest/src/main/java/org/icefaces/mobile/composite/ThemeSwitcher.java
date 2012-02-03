package org.icefaces.mobile.composite;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Simple bean for storing the select theme. Default theme is iOS.
 */
@ManagedBean
@SessionScoped
public class ThemeSwitcher implements Serializable{

    private String theme = "";

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
