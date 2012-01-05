/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.slider;

import org.icefaces.samples.showcase.example.ace.slider.SliderBasicBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderAsyncBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderAsyncInputBean;

import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;                                                                       
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.slider.title",
        description = "example.ace.slider.description",
        example = "/resources/examples/ace/slider/slider.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="slider.xhtml",
                    resource = "/resources/examples/ace/slider/slider.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SliderBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/slider/SliderBean.java")
        }
)
@Menu(
	title = "menu.ace.slider.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.ace.slider.subMenu.main",
                    isDefault = true,
                    exampleBeanName = SliderBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.slider.subMenu.basic",
                    exampleBeanName = SliderBasicBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.slider.subMenu.async",
                    exampleBeanName = SliderAsyncBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.slider.subMenu.asyncinput",
                    exampleBeanName = SliderAsyncInputBean.BEAN_NAME)
})
@ManagedBean(name= SliderBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SliderBean extends ComponentExampleImpl<SliderBean>
        implements Serializable {

    public static final String BEAN_NAME = "slider";

    private Double sliderValue = 0.0;

    public Double getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(Double sliderValue) {
        this.sliderValue = sliderValue;
    }

    public SliderBean() {
        super(SliderBean.class);
    }
}
