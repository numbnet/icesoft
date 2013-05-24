package org.icemobile.input;


public class ButtonBean {
    private String someText;
    private String someText2;
    private boolean disabled = false;
    private boolean singleSubmit = false;
    private String buttonType="default";
    private String type="submit";
    private String value;
    private String style;
    private String styleClass;
    private String selectedButton  = "Yes";
    private String orientation;
    private String buttonGroupStyle ;

    public ButtonBean(){

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

    public String getValue() {
        if (this.type.equals("image")){
            return "";
        }
        return this.buttonType;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(String selectedButton) {
        this.selectedButton = selectedButton;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getButtonGroupStyle() {
        return buttonGroupStyle;
    }

    public void setButtonGroupStyle(String buttonGroupStyle) {
        this.buttonGroupStyle = buttonGroupStyle;
    }
}
