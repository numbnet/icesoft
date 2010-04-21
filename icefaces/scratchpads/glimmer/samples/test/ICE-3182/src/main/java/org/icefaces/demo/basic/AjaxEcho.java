package org.icefaces.demo.basic;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ManagedBean(name = "ajaxecho")
@ViewScoped
public class AjaxEcho {

    private String str;

    private static int count;

    private String filepath;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void reset() {
        this.str = "";
    }

     public void addChild() {

        FacesContext fc = FacesContext.getCurrentInstance();
         Map m = fc.getExternalContext().getRequestParameterMap();
         m.put("javax.faces.partial.render", "@all");
    }

    public void toggleInclude() {

        if (filepath == null) {
            filepath = "snippet.xhtml";
        } else {
            filepath = null;
        } 
    } 

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
