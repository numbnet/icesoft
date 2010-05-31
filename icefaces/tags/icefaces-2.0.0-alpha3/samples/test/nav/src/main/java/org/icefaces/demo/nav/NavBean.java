
package org.icefaces.demo.nav;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "nav")
@ViewScoped
public class NavBean implements Serializable {

    private String navData1a;
    private String navData1b;
    private String navData2a;
    private String navData2b;
    
    public String getResponseWriter(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ResponseWriter rw = fc.getResponseWriter();
        return rw.getClass().getName();
    }

    public String getNavData1a(){
        return navData1a;
    }

    public String getNavData1b(){
        return navData1b;
    }

    public String getNavData2a(){
        return navData2a;
    }

    public String getNavData2b(){
        return navData2b;
    }

    public void setNavData1a(String data){
        navData1a = data;
    }

    public void setNavData1b(String data){
        navData1b = data;
    }

    public void setNavData2a(String data){
        navData2a = data;
    }

    public void setNavData2b(String data){
        navData2b = data;
    }

    public String gotoPage01(){
        return "page01";
    }

    public String gotoPage02(){
        return "page02";
    }

}