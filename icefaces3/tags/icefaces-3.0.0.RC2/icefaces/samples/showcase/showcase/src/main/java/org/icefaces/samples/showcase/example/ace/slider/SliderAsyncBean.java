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

package org.icefaces.samples.showcase.example.ace.slider;

import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;  
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = SliderBean.BEAN_NAME,
        title = "example.ace.slider.async.title",
        description = "example.ace.slider.async.description",
        example = "/resources/examples/ace/slider/slider-async.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "slider-async.xhtml",
                        resource = "/resources/examples/ace/slider/slider-async.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "SliderAsyncBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/slider/SliderAsyncBean.java")
        }
)
@ManagedBean(name = SliderAsyncBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SliderAsyncBean extends ComponentExampleImpl<SliderAsyncBean> implements Serializable {
    public static final String BEAN_NAME = "sliderAsync";

    private boolean clickableRail = true;
    private double maxValue = 150.0;
    private double minValue = -30.0;
    private boolean xAxis = true;
    private String length = "200px";
    private double sliderValue = 0.0;

    public boolean isClickableRail() {
        return clickableRail;
    }

    public void setClickableRail(boolean clickableRail) {
        this.clickableRail = clickableRail;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public boolean isxAxis() {
        return xAxis;
    }

    public void setxAxis(boolean xAxis) {
        this.xAxis = xAxis;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Double getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(Double sliderValue) {
        this.sliderValue = sliderValue;
    }

    public SliderAsyncBean() {
        super(SliderAsyncBean.class);
    }	
}