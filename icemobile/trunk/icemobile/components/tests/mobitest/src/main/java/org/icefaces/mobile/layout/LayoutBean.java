/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.awt.event.ActionEvent;
import java.io.Serializable;
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

    private List menuList = new ArrayList<MenuValue>();
    private String selectedPane ;
    private int accordionIndex = 0;
    private int tabIndex = 1;

	public LayoutBean(){
       this.selectedPane = FIRSTPANE;
       this.menuList.add(new MenuValue("Menu Example", "none", true));
       this.menuList.add(new MenuValue("splash", FIRSTPANE, false));
       this.menuList.add(new MenuValue("date/time", SECONDPANE, false));
       this.menuList.add(new MenuValue("tabSet", THIRDPANE, false));
	}

    public void tabChangeListener(ValueChangeEvent vce){
        logger.info("in tabChangeListener old val="+vce.getOldValue()+" new index="+vce.getNewValue());
    }
    public void changeToPane3(){
        this.selectedPane = THIRDPANE;
    }

    public void changeToPane3(ActionEvent ae){
        this.selectedPane = THIRDPANE;
    }

    public String getSelectedPane() {
        return selectedPane;
    }

    public void setSelectedPane(String selectedPane) {
        this.selectedPane = selectedPane;
    }

    public int getAccordionIndex() {
        return accordionIndex;
    }

    public void setAccordionIndex(int accordionIndex) {
        this.accordionIndex = accordionIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List getMenuList() {
        return menuList;
    }

    public void setMenuList(List menuList) {
        this.menuList = menuList;
    }

    public class MenuValue implements Serializable{
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
}
