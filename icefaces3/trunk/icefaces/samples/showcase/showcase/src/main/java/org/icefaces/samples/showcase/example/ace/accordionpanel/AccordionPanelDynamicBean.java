/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.accordionpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.event.ActionEvent;
import org.icefaces.ace.event.AccordionPaneChangeEvent;

@ComponentExample(
        parent = AccordionPanelBean.BEAN_NAME,
        title = "example.ace.accordionpanel.dynamic.title",
        description = "example.ace.accordionpanel.dynamic.description",
        example = "/resources/examples/ace/accordionpanel/accordionPanelDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="accordionPanelDynamic.xhtml",
                    resource = "/resources/examples/ace/accordionpanel/accordionPanelDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AccordionPanelDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/accordionpanel/AccordionPanelDynamicBean.java")
        }
)
@ManagedBean(name= AccordionPanelDynamicBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelDynamicBean extends ComponentExampleImpl<AccordionPanelDynamicBean> implements Serializable 
{
    public static final String BEAN_NAME = "accordionPanelDynamicBean";
    
    private ArrayList<ImageSet.ImageInfo> imagesOfCars;
    private ArrayList<ImageSet.ImageInfo> imagesOfElectronicDevices;
    private ArrayList<ImageSet.ImageInfo> imagesOfFood;
    private String tabChangeDescriptor;
    
    public AccordionPanelDynamicBean() 
    {
        super(AccordionPanelDynamicBean.class);
        initializeInstanceVariables();
    }
    
    ////////////////////////////////////////////ON TAB CHANGE EVENT BEGIN/////////////////////////////////////////////////
    public void onPaneChange(AccordionPaneChangeEvent event)
    {  
        tabChangeDescriptor = event.getTab().getTitle();
    }
    
    /////////////////////////////////////////////////PRIVATE METHODS BEGIN//////////////////////////////////////////////////
    private void initializeInstanceVariables() 
    {
        this.imagesOfElectronicDevices = ImageSet.getImages(ImageSet.ImagesSelect.GADGETS);
        this.imagesOfCars = ImageSet.getImages(ImageSet.ImagesSelect.CARS);
        this.imagesOfFood = ImageSet.getImages(ImageSet.ImagesSelect.FOOD);
    }
    
    //////////////////////////////////////////////////GETTERS&SETTERS BEGIN////////////////////////////////////////////////
    public ArrayList<ImageSet.ImageInfo> getImagesOfCars()
    {
        return imagesOfCars;
    }

    public int getImagesOfCarsSize(){
        return imagesOfCars != null ? imagesOfCars.size():0;
    }

    public ArrayList<ImageSet.ImageInfo> getImagesOfElectronicDevices()
    {
        return imagesOfElectronicDevices;
    }

    public int getImagesOfElectronicDevicesSize(){
        return imagesOfElectronicDevices != null? imagesOfElectronicDevices.size():0;
    }

    public ArrayList<ImageSet.ImageInfo> getImagesOfFood() {
        return imagesOfFood;
    }

    public int getImagesOfFoodSize(){
        return imagesOfFood != null ? imagesOfFood.size(): 0;
    }

    public String getTabChangeDescriptor() {
        if(tabChangeDescriptor == null)
            return "Click on any pane to fire an event";
        else
            return tabChangeDescriptor;
    }

    public void setTabChangeDescriptor(String tabChangeDescriptor) {
        this.tabChangeDescriptor = tabChangeDescriptor;
    }
}
