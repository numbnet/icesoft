package org.icemobile.input;


import java.lang.String;

public class MenuButtonBean {
    private String selectedValue;
    private boolean disabled = false;
    private String buttonLabel="ButtonLabel";
    private String selectTitle=null;
    private String style;
    private String styleClass;
    private String panelConfirmation;
    private String submitNotification;

    public MenuButtonBean(){

    }

    public String getSelectedValue() {
        return this.selectedValue;
    }

    public void setSelectedValue(String selected) {
        if (null != submitNotification){
            try{
                System.out.println("bean is sleeping 5000ms");
                Thread.sleep(3000);
                System.out.println("bean is awake!");
            }  catch (Exception e){
                System.out.println("problem with sleeping thread test");
            }
        }
        this.selectedValue = selected;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getButtonLabel() {
        return this.buttonLabel;
    }

    public void setButtonLabel(String menuLabel) {
        this.buttonLabel = menuLabel;
    }

    public String getSelectTitle() {
        return this.selectTitle;
    }

    public void setSelectTitle(String selectTitle) {
        this.selectTitle = selectTitle;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return this.styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getPanelConfirmation() {
        if (this.panelConfirmationSet){
            return "pc1";
        }
        return panelConfirmation;
    }

    public void setPanelConfirmation(String panelConfirmation) {
        this.panelConfirmation = panelConfirmation;
    }

    public String getSubmitNotification() {
        if (this.snSet){
            return "sn1";
        }
        return submitNotification;
    }

    public void setSubmitNotification(String submitNotification) {
        this.submitNotification = submitNotification;
    }
    private boolean snSet;
    private boolean panelConfirmationSet;
    public boolean isSnSet(){
        return this.snSet;
    }
    public void setSnSet(boolean set){
        this.snSet = true;
    }

    public boolean isPanelConfirmationSet() {
        return this.panelConfirmationSet;
    }

    public void setPanelConfirmationSet(boolean panelConfirmationSet) {
        this.panelConfirmationSet = panelConfirmationSet;
    }
}
