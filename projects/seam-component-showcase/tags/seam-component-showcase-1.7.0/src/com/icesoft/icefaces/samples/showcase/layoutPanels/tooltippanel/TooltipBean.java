package com.icesoft.icefaces.samples.showcase.layoutPanels.tooltippanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import com.icesoft.faces.component.DisplayEvent;
import com.icesoft.faces.component.paneltabset.PanelTab;
import com.icesoft.faces.component.paneltabset.PanelTabSet;
import com.icesoft.icefaces.samples.showcase.common.Person;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;


@Scope(ScopeType.PAGE)
@Name("tooltip")
public class TooltipBean implements Serializable{
    //source component for which the tooltip will be rendered/unrendered


    Map provinces = new HashMap (); 
    
    List cityList = new ArrayList();
    Person person;
    
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
    
   
    public void provinceDspListener(DisplayEvent event) {
        String contextValue = String.valueOf(event.getContextValue());
        if (contextValue != null) {
            cityList = (List)provinces.get(contextValue);
        }
	//in case user doesn't close the bottom tooltip panel, keep the tooltipperson in the context
	 FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("tooltipPerson", person);
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

    public void hideTooltip(ActionEvent event) {
        this.visible = false;
    }

    public void displayListener(DisplayEvent event) {
        person =  (Person) ((UIData)event.getTarget().getParent().getParent()).getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("tooltipPerson", person);
    }
    
    private boolean visible = false;


    public boolean isVisible() {
        return visible;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }


}