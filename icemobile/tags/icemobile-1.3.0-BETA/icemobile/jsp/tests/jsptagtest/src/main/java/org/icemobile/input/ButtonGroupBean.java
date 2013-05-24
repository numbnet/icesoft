package org.icemobile.input;


import java.lang.String;

public class ButtonGroupBean {
    private String someText;
    private String someText2;
    private boolean disabled = false;
    private boolean singleSubmit = false;
    private String style;
    private String type;
    private String buttonType="default";
    private String styleClass;
    private String orientation = "horizontal";
    private String selectedId;

    public ButtonGroupBean(){

    }

    public String getSomeText() {
        return someText;
    }

    public void setSomeText(String someText) {
        this.someText = someText;
    }

    public String getSomeText2() {
        return someText2;
    }

    public void setSomeText2(String someText2) {
        this.someText2 = someText2;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSingleSubmit() {
        return singleSubmit;
    }

    public void setSingleSubmit(boolean singleSubmit) {
        this.singleSubmit = singleSubmit;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        //can only have submit for now
        this.type = "submit";
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getSelectedId() {
      /*  if (null == this.selectedId){
            System.out.println(" null value so setting to default");
            return "button2";
        }*/
        return selectedId;
    }

    public void setSelectedId(String selectedid) {
      //  System.out.println("group bean:- setting selectedId to" + selectedid);
        this.selectedId = selectedid;
    }

}
