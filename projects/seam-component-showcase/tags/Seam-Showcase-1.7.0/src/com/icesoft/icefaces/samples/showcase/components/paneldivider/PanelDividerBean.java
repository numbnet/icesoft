package com.icesoft.icefaces.samples.showcase.components.paneldivider;

import java.io.Serializable;
import static org.jboss.seam.ScopeType.PAGE;

import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Scope(PAGE)
@Name("panelDividerBean")
public class PanelDividerBean implements Serializable{
    private int position1 = 50;
    private int position2 = 50;
    
    public int getPosition1() {
        return position1;
    }
    public void setPosition1(int position1) {
        this.position1 = position1;
    }
    public int getPosition2() {
        return position2;
    }
    public void setPosition2(int position2) {
        this.position2 = position2;
    }
    
    public void incPosition(ActionEvent event) {
        if ((position1+5) <100) position1+=5;
    }
    
    public void decPosition(ActionEvent event) {
        if ((position1-5) >0) position1-=5;        
    }
}
