/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icemobile.samples.mobileshowcase.view.examples.layout.panelPopup;

import org.icemobile.samples.mobileshowcase.util.FacesUtils;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@Destination(
        title = "example.layout.panelpopup.destination.title.short",
        titleExt = "example.layout.panelpopup.destination.title.long",
        titleBack = "example.layout.panelpopup.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/panelpopup-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/panelpopup-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "panelpopup-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/panelpopup-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "PanelPopupBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/panelPopup/PanelPopupBean.java")
        }
)

@ManagedBean(name = PanelPopupBean.BEAN_NAME)
@SessionScoped
public class PanelPopupBean extends ExampleImpl<PanelPopupBean> implements
        Serializable {

    public static final String BEAN_NAME = "panelPopupBean";

    private boolean visible;
    private String selectedListItem;
    private boolean clientSide;
    private boolean autoCenter = true;
    private String openButtonLabel = "Open";
    private int width = 260;
    private int height = 300;

    public PanelPopupBean() {
        super(PanelPopupBean.class);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String toggleVisible() {
        visible = !visible;
        return null;
    }

    public void selectListItemAction(ActionEvent actionEvent) {
        selectedListItem = FacesUtils.getRequestParameter("listItem");
    }

    public String getSelectedListItem() {
        return selectedListItem;
    }

    public boolean isClientSide() {
        return clientSide;
    }

    public void setClientSide(boolean clientSide) {
        this.clientSide = clientSide;
    }

    public boolean isAutoCenter() {
        return autoCenter;
    }

    public void setAutoCenter(boolean autoCenter) {
        this.autoCenter = autoCenter;
    }

    public String getOpenButtonLabel() {
        return openButtonLabel;
    }

    public void setOpenButtonLabel(String openButtonLabel) {
        this.openButtonLabel = openButtonLabel;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSelectedListItem(String selectedListItem) {
        this.selectedListItem = selectedListItem;
    }

}