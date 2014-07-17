/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.lang.String;

@ManagedBean(name = "popup")
@SessionScoped
public class PopupBean implements Serializable {


    public boolean visible ;
    public boolean visibleClient;
    public boolean clientSide;
    public boolean disabled;
    public boolean autoCenter;
    public int width;
    public int height;
    public boolean centerOnForm;
    public String statusMsg;
    public String styleClass;

    public PopupBean(){
        this.visible=false; //initial value
        this.visibleClient=false;
        this.autoCenter = true;
        this.disabled=false;
        this.width=400;
        this.height = 400;
        this.centerOnForm = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void closeClient(ActionEvent ae){
        this.visibleClient = false;
    }
    public void close(ActionEvent ae){
        this.visible = false;
    }

    public void toggleVisible(ActionEvent actionEvent) {
        visible = !visible;
    }
    public void toggleVisibleClient(ActionEvent actionEvent){
        visibleClient = !visibleClient;
    }

    public boolean isVisibleClient() {
        return visibleClient;
    }

    public void setVisibleClient(boolean visibleClient) {
        this.visibleClient = visibleClient;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isClientSide() {
        return clientSide;
    }

    public void setClientSide(boolean clientSide) {
        this.clientSide = clientSide;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAutoCenter() {
        return autoCenter;
    }

    public void setAutoCenter(boolean autoCenter) {
        this.autoCenter = autoCenter;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isCenterOnForm() {
        return centerOnForm;
    }

    public void setCenterOnForm(boolean centerOnForm) {
        this.centerOnForm = centerOnForm;
    }
    public void popupAction(String statusMsg){
        this.statusMsg = statusMsg;
        this.visible = true;
    }
    public void popupActionClose(String statusMsg){
        this.statusMsg=statusMsg;
        this.visible = false;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
