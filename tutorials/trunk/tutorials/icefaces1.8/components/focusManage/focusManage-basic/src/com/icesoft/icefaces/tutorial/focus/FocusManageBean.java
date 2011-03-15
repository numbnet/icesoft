package com.icesoft.icefaces.tutorial.focus;

import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.component.ext.HtmlInputText;


/**
 * <p>
 * A basic backing bean for the focus management tutorial.
 * This bean contains component bindings and the value change listener used to manage focus requests.
 * </p>
 */
public class FocusManageBean{

    // currently selected text
    private String selectedText = "";

    // selected text constants
    private String NORTH = "north";
    private String WEST = "west";
    private String CENTER = "center";
    private String EAST = "east";
    private String SOUTH = "south";

    // HtmlInputText component bindings
    private HtmlInputText northText = null;
    private HtmlInputText westText = null;
    private HtmlInputText centerText = null;
    private HtmlInputText eastText = null;
    private HtmlInputText southText = null;


    public HtmlInputText getCenterText() {
        return centerText;
    }


    public void setCenterText(HtmlInputText centerText) {
        this.centerText = centerText;
    }


    public HtmlInputText getEastText() {
        return eastText;
    }


    public void setEastText(HtmlInputText eastText) {
        this.eastText = eastText;
    }


    public HtmlInputText getNorthText() {
        return northText;
    }


    public void setNorthText(HtmlInputText northText) {
        this.northText = northText;
    }


    public String getSelectedText() {
        return selectedText;
    }


    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }


    public HtmlInputText getSouthText() {
        return southText;
    }


    public void setSouthText(HtmlInputText southText) {
        this.southText = southText;
    }


    public HtmlInputText getWestText() {
        return westText;
    }


    public void setWestText(HtmlInputText westText) {
        this.westText = westText;
    }


    /**
     * The focus is controlled by a group of radio buttons and their
     * state changes call this method to register a requestFocus call on the newly selected text.
     * @param event new value of event contains the new selected
     * panel name.
     */
    public void selectedTextChanged(ValueChangeEvent event) {
        selectedText = event.getNewValue().toString();

        if (selectedText.equalsIgnoreCase(NORTH)) {
            this.northText.requestFocus();
        } else if (selectedText.equalsIgnoreCase(WEST)){
            this.westText.requestFocus();
        } else if (selectedText.equalsIgnoreCase(CENTER)) {
            this.centerText.requestFocus();
        } else if (selectedText.equalsIgnoreCase(EAST)){
            this.eastText.requestFocus();
        } else if (selectedText.equalsIgnoreCase(SOUTH)) {
            this.southText.requestFocus();
        }
    }


    public String getCENTER() {
        return CENTER;
    }


    public void setCENTER(String center) {
        CENTER = center;
    }


    public String getEAST() {
        return EAST;
    }


    public void setEAST(String east) {
        EAST = east;
    }


    public String getNORTH() {
        return NORTH;
    }


    public void setNORTH(String north) {
        NORTH = north;
    }


    public String getSOUTH() {
        return SOUTH;
    }


    public void setSOUTH(String south) {
        SOUTH = south;
    }


    public String getWEST() {
        return WEST;
    }


    public void setWEST(String west) {
        WEST = west;
    }




}