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

package org.icefaces.samples.showcase.example.ace.tooltip;

import org.icefaces.samples.showcase.example.ace.accordionpanel.ImageSet;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.tooltip.title",
        description = "example.ace.tooltip.description",
        example = "/resources/examples/ace/tooltip/toolTip.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="toolTip.xhtml",
                    resource = "/resources/examples/ace/tooltip/toolTip.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/tooltip/TooltipOverviewBean.java")
        }
)
@Menu(
	title = "menu.ace.progressbar.subMenu.title",
	menuLinks = {
	         @MenuLink(title = "menu.ace.tooltip.subMenu.main", isDefault = true, exampleBeanName = TooltipOverviewBean.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.tooltip.subMenu.globalTooltip", exampleBeanName = GlobalTooltipBean.BEAN_NAME)
    }
)
@ManagedBean(name= TooltipOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipOverviewBean extends ComponentExampleImpl<TooltipOverviewBean> implements Serializable {
    
    public static final String BEAN_NAME = "tooltipOverviewBean";
    private String tooltipEffect = "slide";
    private Integer tooltipShowDelay = 1000;
    private Integer tooltipHideDelay = 2000;
    private ImageSet.ImageInfo image;
    private String tooltipTargetPosition = "topRight";
    private String tooltipPosition = "bottomRight";
    private Integer showEffectLength = 2000;
    private Integer hideEffectLength = 1000;
    
    
    public TooltipOverviewBean() 
    {
        super(TooltipOverviewBean.class);
        image = ImageSet.getImage(ImageSet.ImageSelect.PICTURE);
    }

    public String getTooltipEffect() {
        return tooltipEffect;
    }
    public void setTooltipEffect(String tooltipEffect) {
        this.tooltipEffect = tooltipEffect;
    }
    public ImageSet.ImageInfo getImage() {
        return image;
    }

    public Integer getTooltipHideDelay() {
        return tooltipHideDelay;
    }

    public void setTooltipHideDelay(Integer tooltipHideDelay) {
        this.tooltipHideDelay = tooltipHideDelay;
    }

    public Integer getTooltipShowDelay() {
        return tooltipShowDelay;
    }

    public void setTooltipShowDelay(Integer tooltipShowDelay) {
        this.tooltipShowDelay = tooltipShowDelay;
    }

    public String getTooltipTargetPosition() {
        return tooltipTargetPosition;
    }

    public void setTooltipTargetPosition(String tooltipTargetPosition) {
        this.tooltipTargetPosition = tooltipTargetPosition;
    }

    public String getTooltipPosition() {
        return tooltipPosition;
    }

    public void setTooltipPosition(String tooltipPosition) {
        this.tooltipPosition = tooltipPosition;
    }

    public Integer getHideEffectLength() {
        return hideEffectLength;
    }

    public void setHideEffectLength(Integer hideEffectLength) {
        this.hideEffectLength = hideEffectLength;
    }

    public Integer getShowEffectLength() {
        return showEffectLength;
    }

    public void setShowEffectLength(Integer showEffectLength) {
        this.showEffectLength = showEffectLength;
    }
}