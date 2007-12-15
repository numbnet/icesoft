package com.icesoft.icefaces.samples.showcase.layoutPanels.tooltippanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.event.ValueChangeEvent;

public class TooltipBean {
    //source component for which the tooltip will be rendered/unrendered
    UIComponent tooltipSrc;
    
    //current state of the tooltip
    String state = "hide";
    
    Map provinces = new HashMap (); 
    
    List cityList = new ArrayList();
    
    
    public TooltipBean () {
        List alberta = new ArrayList();
        alberta.add("Calgary");
        alberta.add("Edmonton");
        alberta.add("Red Deer");
        alberta.add("Lethbridge");
        alberta.add("Medicine Hat");
        alberta.add("Airdrie");
        provinces.put("Alberta", alberta);

        List ontario = new ArrayList();
        ontario.add("Toronto");
        ontario.add("Mississauga");
        ontario.add("Hamilton");
        ontario.add("Brampton");
        ontario.add("London");
        ontario.add("Windsor");       
        provinces.put("Ontario", ontario);
        
        List novascotia = new ArrayList();
        novascotia.add("Halifax");
        novascotia.add("Kings County");
        novascotia.add("Colchester County");
        novascotia.add("Lunenburg County");
        provinces.put("Nova Scotia", novascotia);
        
        List saskatchewan = new ArrayList();
        saskatchewan.add("Saskatoon");
        saskatchewan.add("Regina");
        saskatchewan.add("Prince Albert");
        saskatchewan.add("Moose Jaw");
        provinces.put("Saskatchewan", saskatchewan);        

    }
    
    public UIComponent getTooltipSrc() {
        return tooltipSrc;
    }

    public void setTooltipSrc(UIComponent tooltipSrc) {
        this.tooltipSrc = tooltipSrc;
    }
    
    public void stateListener(ValueChangeEvent event) {
        //update the state
        state = String.valueOf(event.getNewValue());
        System.out.println("Event fired "+ state);
        if (tooltipSrc != null) {
            System.out.println("The Src Comp was "+ tooltipSrc);
            UIComponent output = (UIComponent)tooltipSrc.getChildren().get(0);
            cityList = (List)provinces.get(((ValueHolder)output).getValue());
        }
    }

    public Map getProvinces() {
        return provinces;
    }

    public void setProvinces(Map provinces) {
        this.provinces = provinces;
    }

    public List getCityList() {
        return cityList;
    }

    public void setCityList(List cityList) {
        this.cityList = cityList;
    }




    
}
