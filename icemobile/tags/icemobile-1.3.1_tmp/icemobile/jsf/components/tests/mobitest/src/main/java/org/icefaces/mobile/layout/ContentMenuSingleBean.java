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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Map;

@ManagedBean(name="contentMenuBean")
@SessionScoped
public class ContentMenuSingleBean extends LayoutBean {
    private static final Logger logger =Logger.getLogger(ContentMenuSingleBean.class.toString());
    private static final String MENUPANE="mnuPanel";
    private static final String FIRSTPANE="panel1";
    private static final String SECONDPANE="panel2";
    private static final String THIRDPANE="panel3";
    private static final String FIRSTTABPANE="panelt1";
    private static final String SECONDTABPANE="panelt2";
    private static final String THIRDTABPANE="panelt3";
    private List menuCList = new ArrayList<MenuValue>();
    private List dataList = new ArrayList<DataValue>();
    private List eachContentPane = new ArrayList<EachContentPane>();
    private String selectedPane = MENUPANE;
    private String selectedPane2 = FIRSTPANE;
    private String selectedTabPane = FIRSTTABPANE;
    private String twoColSelectedPane=FIRSTPANE;
    private String thirdPane = THIRDPANE;
    private String stackId="stack1";
    private boolean accordion = false;
    private boolean disabled = false;
    private String styleClass;
    private String style;

    public ContentMenuSingleBean(){
       this.menuCList.add(new MenuValue("Menu Example", null, true));
       this.menuCList.add(new MenuValue("splash", FIRSTPANE, false));
       this.menuCList.add(new MenuValue("date/time", SECONDPANE, false));
       this.menuCList.add(new MenuValue("tabSet", THIRDPANE, false));
       this.dataList.add(new DataValue(FIRSTPANE));
       this.eachContentPane.add(new EachContentPane("panel1", true, false, "panel1",
           "Panel1", "header for panel 1", "content for panel 1 - king of clubs",
           "../images/kingOfClubs.jpg", "king of clubs", "King of Clubs"));
       this.eachContentPane.add(new EachContentPane("panel2", false, false, "panel2",
            "Panel2", "header for panel 2", "content for panel 2 - queen of clubs",
            "../images/queen.jpg", "queen of clubs", "Queen of Clubs"));
       this.eachContentPane.add(new EachContentPane("panel3", false, true, "panel3",
            "Panel3", "header for panel 3", "content for panel 3 - some other card",
            "../images/Joker.jpg", "joker", "Joker"));
    }
    public String getSelectedPane() {
        return selectedPane;
    }

    public void setSelectedPane(String selectedPane) {
        this.selectedPane = selectedPane;
    }

    public String setSelectedPaneAction(String selectedPane) {
        this.selectedPane = selectedPane;
        return null;
    }

    public String getSelectedTabPane() {
        return selectedTabPane;
    }

    public void setSelectedTabPane(String selectedTabPane) {
        this.selectedTabPane = selectedTabPane;
    }

    public List getMenuCList() {
        return menuCList;
    }

    public void setMenuCList(List menuList) {
        this.menuCList = menuList;
    }

    public boolean isAccordion() {
        return accordion;
    }

    public void setAccordion(boolean accordion) {
        this.accordion = accordion;
    }

    public String getTwoColSelectedPane() {
        return twoColSelectedPane;
    }
    public String getSelectedPane2() {
        return selectedPane2;
    }

    public String setTwoColSelectedPaneAction(String twoColSelPane){
        this.twoColSelectedPane = twoColSelPane;
        System.out.println(" action method change to "+twoColSelPane);
        return "";
    }

    public void actionParamTest(){
        FacesContext fc = FacesContext.getCurrentInstance();
        this.twoColSelectedPane = setPaneFromParams(fc);
        logger.info("set twoColSelPane="+twoColSelectedPane);
    }
    private String setPaneFromParams(FacesContext fc){
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        return params.get("paneSel");
    }
    public void setTwoColSelectedPane(String twoColSelectedPane) {
        this.twoColSelectedPane = twoColSelectedPane;
    }
    public void setSelectedPane2(String selectedPane2) {
        this.selectedPane2 = selectedPane2;
    }

    public void goToPane3(ActionEvent ae){
        this.twoColSelectedPane = THIRDPANE;
        logger.info("twoColSelected with ae ="+this.twoColSelectedPane);
    }
    public void goToPane3(){
        this.twoColSelectedPane= "panel3";
        logger.info("twoColSelected withOUT ae ="+this.twoColSelectedPane);
    }
    public void goToPane4(){
        this.twoColSelectedPane= "panel4";
    }

    public String getThirdPane() {
        return thirdPane;
    }

    public void setThirdPane(String thirdPane) {
        this.thirdPane = thirdPane;
    }
    public void testP(Long test){
        logger.info("test int = "+test.intValue());
    }
    public void testString(String abc){
        logger.info("test abc = "+abc);
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
}
