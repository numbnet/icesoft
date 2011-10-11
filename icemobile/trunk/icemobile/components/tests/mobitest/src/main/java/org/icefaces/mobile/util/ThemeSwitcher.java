package org.icefaces.mobile.util;

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
    private String prefix = "tcal";

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPrefix(){
        if (theme.equals("iphone.css")){
            return "iphone";
        }
       else return "tcal";
    }
}
