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

package org.icefaces.samples.showcase.example.ace.progressbar;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.event.ActionEvent;
import org.icefaces.samples.showcase.example.ace.accordionpanel.ImageSet;

@ComponentExample(
        title = "example.ace.progressbar.title",
        description = "example.ace.progressbar.description",
        example = "/resources/examples/ace/progressbar/progressBar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressBar.xhtml",
                    resource = "/resources/examples/ace/progressbar/progressBar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressBar.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/progressbar/ProgressBarBean.java")
        }
)
@Menu(
	title = "menu.ace.progressbar.subMenu.title",
	menuLinks = {
	         @MenuLink(title = "menu.ace.progressbar.subMenu.main", isDefault = true, exampleBeanName = ProgressBarBean.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.polling", exampleBeanName = ProgressBarPolling.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.push", exampleBeanName = ProgressBarPush.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.client", exampleBeanName = ProgressBarClient.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.clientAndServer", exampleBeanName = ProgressBarClientAndServer.BEAN_NAME)
    }
)
@ManagedBean(name= ProgressBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarBean extends ComponentExampleImpl<ProgressBarBean> implements Serializable {
    public static final String BEAN_NAME = "progressBarBean";
    
    private ArrayList<ImageSet.ImageInfo> imagesOfCars;
    private Integer progressValue;
    private ImageSet.ImageInfo currentImage;
    private int currentIndex;
    private String message;

    public ProgressBarBean() {
        super(ProgressBarBean.class);
        
        this.imagesOfCars = ImageSet.getImages(ImageSet.ImagesSelect.CARS);
        currentIndex = 0;
        setBeanVariables(currentIndex);
    }
    
    public void returnToPreviousImage(ActionEvent event)
    {
        if(currentIndex > 0 && currentIndex<imagesOfCars.size())
        {
            currentIndex--;
            setBeanVariables(currentIndex);
        }
    }
    
    public void proceedToNextImage(ActionEvent event)
    {
        if(currentIndex!=imagesOfCars.size()-1)
        {
            currentIndex++;
            setBeanVariables(currentIndex);
        }
    }
	
    
    private void setBeanVariables(int currentIndex) 
    {
        this.progressValue = findProgressValue(currentIndex);
        this.currentImage = imagesOfCars.get(currentIndex);
        this.message = "Image " +(currentIndex+1)+" out of "+imagesOfCars.size();
    }
    
    private String findImageDescription(String imagePath) 
    {
        String description;
        char searchCriteria = '/';
        int substringStartIndex = imagePath.lastIndexOf(searchCriteria);
        description = imagePath.substring(substringStartIndex+1);
        return description;
    }
    
    private Integer findProgressValue(int indexValue)
    {
        return ++indexValue*(100/imagesOfCars.size());
    }

    public ArrayList<ImageSet.ImageInfo> getImagesOfCars() {
        return imagesOfCars;
    }

    public Integer getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(Integer progressValue) {
        this.progressValue = progressValue;
    }

    public ImageSet.ImageInfo getCurrentImage() {
        return currentImage;
    }
    
    public String getMessage() {
            return message;
    }
}