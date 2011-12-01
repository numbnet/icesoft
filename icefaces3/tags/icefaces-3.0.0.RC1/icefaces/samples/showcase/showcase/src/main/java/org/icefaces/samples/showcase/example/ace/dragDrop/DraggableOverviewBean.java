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

package org.icefaces.samples.showcase.example.ace.dragDrop;


import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = DragDropOverviewBean.BEAN_NAME,
        title = "example.ace.draggable.title",
        description = "example.ace.draggable.description",
        example = "/resources/examples/ace/dragDrop/draggableOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="draggableOverview.xhtml",
                    resource = "/resources/examples/ace/dragDrop/draggableOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DraggableOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dragDrop/DraggableOverviewBean.java")
        }
)
@ManagedBean(name= DraggableOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DraggableOverviewBean extends ComponentExampleImpl<DraggableOverviewBean> implements Serializable
{
    public static final String BEAN_NAME = "draggableOverviewBean";
    private String imageLocation;
    
    private String axisMovementConstraint;
    private String helperMode;
    private String gridMode;
    private String containmentConstraint;
    private boolean revert;
    private double opacity;
    
    public DraggableOverviewBean()
    {
        super(DraggableOverviewBean.class);
        imageLocation = "/resources/css/images/rainbowCalgary.png";
        axisMovementConstraint = "x or y";
        helperMode = "original";
        containmentConstraint = "";
        gridMode ="1,1";
        revert = false;
        opacity = 1d;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getAxisMovementConstraint() {
        return axisMovementConstraint;
    }

    public void setAxisMovementConstraint(String axisMovementConstraint) {
        this.axisMovementConstraint = axisMovementConstraint;
    }

    public String getGridMode() {
        return gridMode;
    }

    public void setGridMode(String gridMode) {
        this.gridMode = gridMode;
    }

    public String getHelperMode() {
        return helperMode;
    }

    public void setHelperMode(String helperMode) {
        this.helperMode = helperMode;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public boolean isRevert() {
        return revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }

    public String getContainmentConstraint() {
        return containmentConstraint;
    }

    public void setContainmentConstraint(String containmentConstraint) {
        this.containmentConstraint = containmentConstraint;
    }
    
}
