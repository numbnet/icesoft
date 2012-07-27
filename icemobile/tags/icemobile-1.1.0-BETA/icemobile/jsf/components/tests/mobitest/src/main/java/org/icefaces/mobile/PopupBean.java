package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean(name = "popup")
@SessionScoped
public class PopupBean implements Serializable {


    public boolean visible ;
    public boolean visibleClient;

    public PopupBean(){
        this.visible=false; //initial value
        this.visibleClient=false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void closeClient(ActionEvent ae){
        this.visibleClient = false;
    }
    public void close(ActionEvent ae){
        this.visible = false;
    }

    public void toggleVisible(ActionEvent actionEvent) {
        visible = !visible;
    }
    public void toggleVisibleClient(ActionEvent actionEvent){
        visibleClient = !visibleClient;
    }

    public boolean isVisibleClient() {
        return visibleClient;
    }

    public void setVisibleClient(boolean visibleClient) {
        this.visibleClient = visibleClient;
    }
}
