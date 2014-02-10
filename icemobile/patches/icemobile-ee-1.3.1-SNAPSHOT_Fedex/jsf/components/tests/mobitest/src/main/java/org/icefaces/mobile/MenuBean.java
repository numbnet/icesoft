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
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.model.SelectItem;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import java.util.logging.Logger;
import org.icefaces.mobi.component.menubutton.MenuButtonItem;

@ManagedBean(name="menu")
@ViewScoped
public class MenuBean implements Serializable {

    private static final Logger logger =
    Logger.getLogger(ListBean.class.toString());
    private List<String> simpleList = new ArrayList<String>() ;
    private String outputString = "none";
    private String selTitle="Select";
    private String buttonLabel = "Buttonlabel";
    private String itemChosen="none";
    private boolean disabled = false;
    private String mbstyle;

 //   private List<MenuAction> itemList = new ArrayList<MenuAction>();
    private List<ModelData> data = new ArrayList<ModelData>();
    private String height="12px";
    private String style="display:inline-block;position:relative;top:-25px;left:0;color:white;";
    private String styleClass;


    public MenuBean(){
        this.simpleList.add("Edit");
        this.simpleList.add("File");
        this.simpleList.add("Delete");
        this.simpleList.add("Cancel");
        fillModelData();
    }

    private void fillModelData(){
        this.data.add(new ModelData("File", "file item","pc1" , null ));
        this.data.add(new ModelData("Add", "add item", "pcAdd", "sn1" ));
        this.data.add(new ModelData("Delete", "delete item", "pcDel", null ));
        this.data.add(new ModelData("Print", "print item", null, "sn1"));
        this.data.add(new ModelData("Cancel", "cancel item", null, null ));
    }

    public List<ModelData> getData() {
        return data;
    }

    public String getSelTitle() {
        return this.selTitle;
    }

    public void setSelTitle(String selTitle) {
        this.selTitle = selTitle;
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

    public void actionMethod(ActionEvent ae){
        UIComponent uic = ae.getComponent();
        if (uic instanceof MenuButtonItem) {
            MenuButtonItem mbi = (MenuButtonItem)uic;
         //   logger.info("Item selected="+mbi.getValue()+" label="+mbi.getLabel());
            this.itemChosen = mbi.getValue().toString();
        }
    }

    public void actionMethodSleep(ActionEvent ae){
        UIComponent uic = ae.getComponent();
        if (uic instanceof MenuButtonItem) {
            MenuButtonItem mbi = (MenuButtonItem)uic;
         //   logger.info("Item selected="+mbi.getValue()+" label="+mbi.getLabel());
            this.itemChosen = mbi.getValue().toString();
        }
            try{
                Thread.sleep(5000);
                logger.info("slept");
            }   catch (Exception e){
       }
    }

    public String getItemChosen() {
        return itemChosen;
    }

    public static String EVENT_TRIGGERED="NONE";
    public String getOutputString() {
        return EVENT_TRIGGERED;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getMbstyle() {
        return mbstyle;
    }

    public void setMbstyle(String mbstyle) {
        this.mbstyle = mbstyle;
    }

    public class ModelData implements Serializable{
        private String eventTriggered = "none";
        private String value;
        private String label;
        private String panelConfId= null;
        private String submitNotif=null;
        private String disabled= "false";
        private String singleSubmit = "false";
        private boolean panelConfOnly;
        private boolean submitNotifOnly;
        private boolean both;
        private boolean none;

        public ModelData (String val, String label, String pcId, String snId){
            this.value = val;
            this.label = label;
            if (null != snId && snId.length() > 0){
               this.submitNotif = snId;
            }
            if (null != pcId && pcId.length() > 0){
                this.panelConfId = pcId;
            }
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
     //        logger.info(" actionMethod for value="+this.value);
             MenuBean.EVENT_TRIGGERED="item "+this.value+" was selected";
             if (this.value.equals("Add") || this.value.equals("Print")){
                try{
                   Thread.sleep(5000);
                    logger.info(" after sleeping value="+this.value);
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
        public boolean isBoth(){
            return this.submitNotif !=null && this.panelConfId !=null;
        }
        public boolean isSubmitNotifOnly(){
            return this.submitNotif !=null && this.panelConfId == null;
        }
        public boolean isPanelConfOnly(){
            return this.panelConfId !=null && this.submitNotif ==null;
        }
        public boolean isNone(){
            return this.panelConfId==null && this.submitNotif ==null;
        }
    }
}
