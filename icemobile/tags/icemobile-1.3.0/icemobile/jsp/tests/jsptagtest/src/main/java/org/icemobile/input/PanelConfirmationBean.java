package org.icemobile.input;


import java.lang.String;

public class PanelConfirmationBean {
    private String type="both";
    private String message="Are you certain?";
    private String style;
    private String styleClass;
    private String title  = "Confirm Entry";
    private String orientation = "horizontal";
    private String acceptLabel = "You Bet!" ;
    private String cancelLabel = "No Way";
    private String testInput;

    public PanelConfirmationBean(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcceptLabel() {
        return acceptLabel;
    }

    public void setAcceptLabel(String acceptLabel) {
        this.acceptLabel = acceptLabel;
    }

    public String getCancelLabel() {
        return cancelLabel;
    }

    public void setCancelLabel(String cancelLabel) {
        this.cancelLabel = cancelLabel;
    }

    public String getTestInput() {
        return testInput;
    }

    public void setTestInput(String testInput) {
        this.testInput = testInput;
    }
}
