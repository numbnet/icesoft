package org.icemobile.layout;


import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class CarouselBean {
    private boolean disabled = false;
    private String style;
    private String styleClass;
    private int selectedIndex  = 1;
    private String nextLabel;
    private String previousLabel;
    private List<String> textList = new ArrayList<String>();

    public CarouselBean(){
        textList.add("A robot may not injure a human being or, through inaction, allow a human being to come to harm.");
        textList.add("A robot must obey any orders given to it by human beings, except where such orders would conflict with the First Law.");
        textList.add("A robot must protect its own existence as long as such protection does not conflict with the First or Second Law.");
        textList.add("A crazy robot may not injure a human being or, through inaction, allow a human being to come to harm.");
        textList.add("A crazy robot must obey any orders given to it by human beings, except where such orders would conflict with the First Law.");
        textList.add("A crazy robot must protect its own existence as long as such protection does not conflict with the First or Second Law.");
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public String getNextLabel() {
        return this.nextLabel;
    }

    public void setNextLabel(String nextLabel) {
        this.nextLabel = nextLabel;
    }

    public String getPreviousLabel() {
        return this.previousLabel;
    }

    public void setPreviousLabel(String previousLabel) {
        this.previousLabel = previousLabel;
    }

    public List<String> getTextList() {
        return this.textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }
}
