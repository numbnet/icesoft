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
    private List<SelectItem> selItems = new ArrayList<SelectItem>();
    private List<String> actionListeners = new ArrayList<String>();
    private String outputString = "none";

 //   private List<MenuAction> itemList = new ArrayList<MenuAction>();
    private List<ModelData> data = new ArrayList<ModelData>();
    private String height="12px";
    private String style="display:inline-block;position:relative;top:-25px;left:0;color:white;";

    public MenuBean(){
        fillSelItems();
        this.simpleList.add("Edit");
        this.simpleList.add("File");
        this.simpleList.add("Delete");
        this.simpleList.add("Cancel");
        fillModelData();
    }

    private void fillModelData(){
        this.data.add(new ModelData("File", "file item", "#{\"#{menu.fileListener}\"", "none", "none" ));
        this.data.add(new ModelData("Add", "add item", "#{\"#{menu.addListener}\"", "none", "n" ));
        this.data.add(new ModelData("Delete", "delete item", "#{\"#{menu.deleteListener}\"", "none", "n" ));
        this.data.add(new ModelData("Cancel", "cancel item", "#{\"#{menu.cancelListener}\"", "none", "n" ));
    }
    private void fillSelItems(){
        this.selItems.add(new SelectItem("edit", "edit option"));
        this.selItems.add(new SelectItem("file", "file option"));
        this.selItems.add(new SelectItem("delete", "delete option"));
        this.selItems.add(new SelectItem("cancel", "cancel option"));
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
        private String actionListener;
        private String panelConfId;
        private String submitNotif;

        public ModelData (String val, String label, String actionListener, String pcId, String snId){
            this.value = val;
            this.label = label;
            this.actionListener = actionListener;
            this.submitNotif = snId;
            this.panelConfId = pcId;
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

     }
}
