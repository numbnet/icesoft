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
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import java.util.logging.Logger;

@ManagedBean(name="menu")
@ViewScoped
public class MenuBean implements Serializable {

    private static final Logger logger =
    Logger.getLogger(ListBean.class.toString());
    private List<String> simpleList = new ArrayList<String>() ;
    private String outputString = "none";

 //   private List<MenuAction> itemList = new ArrayList<MenuAction>();
    private List<ModelData> data = new ArrayList<ModelData>();
    private String height="12px";
    private String style="display:inline-block;position:relative;top:-25px;left:0;color:white;";

    public MenuBean(){
        this.simpleList.add("Edit");
        this.simpleList.add("File");
        this.simpleList.add("Delete");
        this.simpleList.add("Cancel");
        fillModelData();
    }

    private void fillModelData(){
  //      this.data.add(new ModelData("options", "select one","none", "none"));
        this.data.add(new ModelData("File", "file item","pc1" , "none" ));
        this.data.add(new ModelData("Add", "add item", "pcAdd", "sn1" ));
        this.data.add(new ModelData("Delete", "delete item", "pcDel", "n" ));
        this.data.add(new ModelData("Print", "print item", "pc1", "none"));
        this.data.add(new ModelData("Cancel", "cancel item", "none", "n" ));
    }

    public List<ModelData> getData() {
        return data;
    }

    public void setData(List<ModelData> data) {
        this.data = data;
    }


    public List<String> getSimpleList() {
        return simpleList;
    }

    public void setSimpleList(List<String> simpleList) {
        this.simpleList = simpleList;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getStyle() {
        return style;
    }


    public void setStyle(String style) {
        this.style = style;
    }
    public static String EVENT_TRIGGERED="NONE";
    public String getOutputString() {
        return EVENT_TRIGGERED;
    }


    public class ModelData implements Serializable{
        private String eventTriggered = "none";
        private String value;
        private String label;
        private String panelConfId;
        private String submitNotif;
        private String disabled= "false";
        private String singleSubmit = "false";

        public ModelData (String val, String label, String pcId, String snId){
            this.value = val;
            this.label = label;
            this.submitNotif = snId;
            this.panelConfId = pcId;
        }

        public String getDisabled() {
            return disabled;
        }

        public void setDisabled(String disabled) {
            this.disabled = disabled;
        }

        public String getValue() {
             return value;
         }

         public void setValue(String value) {
             this.value = value;
         }

         public String getLabel() {
             return label;
         }

         public void setLabel(String label) {
             this.label = label;
         }

         public String getPanelConfId() {
             return panelConfId;
         }

         public void actionMethod(ActionEvent ae){
             MenuBean.EVENT_TRIGGERED="item "+this.value+" was selected";
             if (this.value.equals("Add")){
                try{
                   Thread.sleep(5000);
                   this.label="Added";
                }  catch (Exception e){

                }
             }
         }
         public void setPanelConfId(String panelConfId) {
             this.panelConfId = panelConfId;
         }

         public String getSubmitNotif() {
             return submitNotif;
         }

         public void setSubmitNotif(String submitNotif) {
             this.submitNotif = submitNotif;
         }

        public String getSingleSubmit() {
            return singleSubmit;
        }

        public void setSingleSubmit(String singleSubmit) {
            this.singleSubmit = singleSubmit;
        }
    }
}
