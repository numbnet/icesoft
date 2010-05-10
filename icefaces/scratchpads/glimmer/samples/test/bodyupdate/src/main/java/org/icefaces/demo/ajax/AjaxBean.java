
package org.icefaces.demo.ajax;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "ajaxBean")
@ViewScoped
public class AjaxBean implements Serializable {

    int count = 0;

    public void toggle(ActionEvent ae) {
        count++;
    }

    public int getCount(){
        return count;
    }

    public String countAction(){
        System.out.println("AjaxBean.countAction");
        count++;
        return "index";
    }

    public void countActionListener(ActionEvent event){
        System.out.println("AjaxBean.countActionListener");
        count++;
    }

}