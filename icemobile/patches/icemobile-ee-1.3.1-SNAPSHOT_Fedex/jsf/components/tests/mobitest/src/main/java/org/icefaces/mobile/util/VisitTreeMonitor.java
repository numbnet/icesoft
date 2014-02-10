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

package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.lang.String;
import java.lang.System;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;
import java.util.*;

import org.icefaces.mobi.component.contentstack.ContentStack;

@ManagedBean(name="vmonitor")
@SessionScoped
public class VisitTreeMonitor implements Serializable {
    private int treeSize = -1;
    private List<CompDef> resultCompList;

    public void findCompsIntree(ActionEvent actionEvent){
        resultCompList = new ArrayList<CompDef>();
        String stackId= "stack1";
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        UIComponent stack = root.findComponent(stackId);
        if (null==stack)return;
        stack.visitTree(
                VisitContext.createVisitContext(context),
                        new VisitCallback(){
                            public VisitResult visit(VisitContext visitContext, UIComponent uiComponent){
                                CompDef uicomp = new CompDef();
                                uicomp.setComponentId(uiComponent.getId());
                                uicomp.setClientId(uiComponent.getClientId());
                                uicomp.setComponentType(uiComponent.getClass().toString());
                                uicomp.setChildCount(uiComponent.getChildCount());
                                uicomp.setFacetCount(uiComponent.getFacetCount());
                                uicomp.setNamingContainer(uiComponent instanceof NamingContainer);
                                resultCompList.add(uicomp);
                                return VisitResult.ACCEPT;
                            }
                 });
    }

    public List<org.icefaces.mobile.VisitTreeMonitor.CompDef> getResultCompList() {
        return resultCompList;
    }

    public void setResultCompList(List<org.icefaces.mobile.VisitTreeMonitor.CompDef> resultCompList) {
        this.resultCompList = resultCompList;
    }

    public String getIdsToVisit(){
        return "ALL_IDS";
    }
    public int getTreeSize() {
         if (null==resultCompList){
             return -1;
         }
        else {
             return resultCompList.size();
         }

    }

    public void setTreeSize(int value){
        this.treeSize = value;
    }

    public class CompDef implements Serializable{
        private String componentId;
        private String componentType;
        private String clientId;
        private int facetCount = 0;
        private int childCount = 0;
        private boolean isNamingContainer = false;

        public CompDef(){
            super();
        }
        public String getComponentId() {
            return componentId;
        }

        public void setComponentId(String componentId) {
            this.componentId = componentId;
        }

        public String getComponentType() {
            return componentType;
        }

        public void setComponentType(String componentType) {
            this.componentType = componentType;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public int getFacetCount() {
            return facetCount;
        }

        public void setFacetCount(int facetCount) {
            this.facetCount = facetCount;
        }

        public int getChildCount() {
            return childCount;
        }

        public void setChildCount(int childCount) {
            this.childCount = childCount;
        }

        public boolean isNamingContainer() {
            return isNamingContainer;
        }

        public void setNamingContainer(boolean namingContainer) {
            isNamingContainer = namingContainer;
        }
    }
}
