/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobile.layout;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="layoutBean")
@SessionScoped
public class LayoutBean implements Serializable {
	private static final Logger logger =Logger.getLogger(LayoutBean.class.toString());
    private static final String FIRSTPANE="panel1";
    private static final String SECONDPANE="panel2";
    private static final String THIRDPANE="panel3";
    private static final String FORTH_PANE="panel4";
    private static final String FIFTH_PANE="panel5";
    private static final String SIXTH_PANE="panel6";

    private List<MenuValue> menuList = new ArrayList<MenuValue>();
    private List<DataValue> dataList = new ArrayList<DataValue>();
    private List<DataValue> dataListMultiRow = new ArrayList<DataValue>();
    private List<EachContentPane> eachContentPane = new ArrayList<EachContentPane>();
    private String selectedPane = FIRSTPANE;
    private String selectedPane2 = FORTH_PANE;
 //   private String lmenuPane = MENUPANE;
    private int counter;
    private boolean autoHeight;
    private boolean fixedHeight;
    private String fixedHeightString;
    private String fixedHeightVal;
    private boolean disabled;
    private String styleClass;
    private String style;
    private boolean scrollablePaneContent;
    private boolean pane3Rendered = true;
    private long timestamp;
    private long headTimestamp;
    private String orientation="top";
    private boolean fitToParent=false;
    private String inputString;
    private String inputString2;

	public LayoutBean(){
        this.selectedPane = FIRSTPANE;
        this.menuList.add(new MenuValue("Menu Example", "none", true));
        this.menuList.add(new MenuValue("splash", FIRSTPANE, false));
        this.menuList.add(new MenuValue("date/time", SECONDPANE, false));
        this.menuList.add(new MenuValue("tabSet", THIRDPANE, false));
        this.menuList.add(new MenuValue("icefacesPage", null, false));
        this.dataList.add(new DataValue(FIRSTPANE));
        dataListMultiRow.add(new DataValue(FIRSTPANE));
        dataListMultiRow.add(new DataValue(SECONDPANE));
        dataListMultiRow.add(new DataValue(THIRDPANE));
        eachContentPane.add(new EachContentPane("panel1", true, false, "panel1",
            "Panel1", "header for panel 1", "content for panel 1 - king of clubs",
            "../../images/kingOfClubs.jpg", "king of clubs", "King of Clubs"));
        eachContentPane.add(new EachContentPane("panel2", false, false, "panel2",
            "Panel2", "header for panel 2", "content for panel 2 - queen of clubs",
            "../../images/queen.jpg", "queen of clubs", "Queen of Clubs"));
        eachContentPane.add(new EachContentPane("panel3", false, true, "panel3",
            "Panel3", "header for panel 3", "content for panel 3 - some other card",
            "../../images/Joker.jpg", "joker", "Joker"));
        counter = eachContentPane.size();
        disabled = false;
        this.scrollablePaneContent = true;
    }

    public void tabChangeListener(ValueChangeEvent vce){
        logger.info("in tabChangeListener old val="+vce.getOldValue()+" new index="+vce.getNewValue());
    }

    public void paneChange(ValueChangeEvent pce){
        logger.info("pane Changed!!!"+" oldId="+pce.getOldValue()+" newId="+pce.getNewValue());

    }
    public void addContentPane(ActionEvent event) {
        logger.info("LayoutBean.addContentPane()");
        counter++;
        eachContentPane.add(new EachContentPane("panel"+counter, false, true, "panel"+counter,
            "Panel"+counter, "header for panel "+counter, "content for panel "+counter+" - some other card",
            "../../images/Joker.jpg", "joker", "Joker"));
    }

    public void removeContentPane(ActionEvent event) {
        logger.info("LayoutBean.removeContentPane()");
        if (selectedPane == null || selectedPane.length() == 0) {
            return;
        }
        for (int i = 0; i < eachContentPane.size(); i++) {
            EachContentPane pane = eachContentPane.get(i);
            if (selectedPane.equals(pane.getId())) {
                eachContentPane.remove(i);
                break;
            }
        }
    }

    public String getInputString2() {
        return inputString2;
    }

    public void setInputString2(String inputString2) {
        this.inputString2 = inputString2;
    }

    public void clearFixedHeight(ActionEvent evt){
        this.fixedHeightString = "";
    }
    public void changeToPane3(){
        this.selectedPane = THIRDPANE;
    }

    public boolean isAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
    }
    
    public void setFitToParent(boolean fitToParent){
        this.fitToParent = fitToParent;
    }
    
    public boolean getFitToParent(){
        return fitToParent;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean isFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(boolean fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public String getFixedHeightVal() {
       return fixedHeightVal;
    }

    public void setFixedHeightVal(String fixedHeightVal) {
        this.fixedHeightVal = fixedHeightVal;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSelectedPane() {
        return selectedPane;
    }

    public void setSelectedPane(String selectedPane) {
       // logger.info("LayoutBean.setSelectedPane: " + selectedPane);
        this.selectedPane = selectedPane;
    }

    public String getSelectedPane2() {
        return selectedPane2;
    }

    public void setSelectedPane2(String selectedPane2) {
        this.selectedPane2 = selectedPane2;
    }

    public void render3false(ActionEvent ae){
          this.pane3Rendered=!pane3Rendered;
    }

    public boolean isPane3Rendered() {
        return pane3Rendered;
    }

    public void setPane3Rendered(boolean pane3Rendered) {
        this.pane3Rendered = pane3Rendered;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public List getMenuList() {
        return menuList;
    }

    public void setMenuList(List menuList) {
        this.menuList = menuList;
    }

    public String getFixedHeightString() {
        return fixedHeightString;
    }

    public void setFixedHeightString(String fixedHeightString) {
        this.fixedHeightString = fixedHeightString;
    }

    public List getDataList() { return dataList; }

    public List getDataListMultiRow() { return dataListMultiRow; }

    public List getEachContentPane() { return eachContentPane; }

    public SelectItem[] getAllContentPanes() {
        int sz = eachContentPane.size();
        SelectItem[] items = new SelectItem[sz];
        for (int i = 0; i < sz; i++) {
            items[i] = new SelectItem(eachContentPane.get(i).getId());
        }
        return items;
    }

    public boolean isScrollablePaneContent() {
        return scrollablePaneContent;
    }

    public void setScrollablePaneContent(boolean scrollablePaneContent) {
        this.scrollablePaneContent = scrollablePaneContent;
    }
 
    public long getTimestamp(){ return timestamp; }
    public long getHeadTimestamp(){ return headTimestamp; }
    
    public void updateTimestamp(ActionEvent evt){
        timestamp = System.currentTimeMillis();
    }
    
    public void updateHeadTimestamp(ActionEvent evt){
        headTimestamp = System.currentTimeMillis();
    }

    public static class MenuValue implements Serializable{
        private String label;
        private String value;
        private String url;
        private String icon;
        private boolean menuHeader;

        public MenuValue (String label, String value, boolean menuHeader){
            this.label = label;
            this.value = value;
            this.menuHeader = menuHeader;
        }
        public MenuValue (String label, String value, boolean menuHeader, String url){
            this.label = label;
            this.value = value;
            this.menuHeader = menuHeader;
            this.url = url;
        }
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public boolean isMenuHeader() {
            return menuHeader;
        }

        public void setMenuHeader(boolean menuHeader) {
            this.menuHeader = menuHeader;
        }
        
    }


    public static class DataValue implements Serializable {
        private String selectedPane;

        public DataValue(String selectedPane) {
            this.selectedPane = selectedPane;
        }

        public String getSelectedPane() { return selectedPane; }
        public void setSelectedPane(String selectedPane) { this.selectedPane = selectedPane; }
        public void changeToPane3(){
            this.selectedPane = THIRDPANE;
        }
    }


    public static class EachContentPane implements Serializable {
        private String id;
        private boolean client;
        private boolean facelet;
        private String title;
        private String legend;
        private String header;
        private String content;
        private String imgSrc;
        private String alt;
        private String figcaption;

        public EachContentPane(
            String id,
            boolean client,
            boolean facelet,
            String title,
            String legend,
            String header,
            String content,
            String imgSrc,
            String alt,
            String figcaption) {
            this.id = id;
            this.client = client;
            this.facelet = facelet;
            this.title = title;
            this.legend = legend;
            this.header = header;
            this.content = content;
            this.imgSrc = imgSrc;
            this.alt = alt;
            this.figcaption = figcaption;
        }

        public String getId() { return id; }
        public boolean getClient() { return client; }
        public boolean getFacelet() {return facelet;}
        public String getTitle() { return title; }
        public String getLegend() { return legend; }
        public String getHeader () { return header; }
        public String getContent() { return content; }
        public String getImgSrc() { return imgSrc; }
        public String getAlt() { return alt; }
        public String getFigcaption() { return figcaption; }
        
    }
}
