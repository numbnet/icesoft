package org.icefaces.demo.basic;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 */
@ManagedBean(name = "ajaxecho")
@ViewScoped
public class AjaxEcho {

    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void reset() {
        this.str = "";
    } 
}
