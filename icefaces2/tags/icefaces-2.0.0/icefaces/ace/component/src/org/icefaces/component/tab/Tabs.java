package org.icefaces.component.tab;


import javax.faces.component.UIData;
//TODO extending UIData to just to compile, should be extending UISeries,
public class Tabs extends UIData{
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Tabs";
    public Tabs() {
        super();
        setRendererType(null);
    }
}