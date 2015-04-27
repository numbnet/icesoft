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

import org.icefaces.mobi.component.geolocation.Geolocation;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ManagedBean(name="menuAcc")
@SessionScoped
public class MenuAccordionBean implements Serializable {
	private static final Logger logger =Logger.getLogger(MenuAccordionBean.class.toString());

    private static final String SPLASH = "splash";
    private static final String ACC1 = "accpane1";
    private static final String ACC2 = "accpane2";
    private static final String ACC3 = "accpane3";

    private static final String NAV1="navpane1";
    private static final String NAV2="navpane2";
    private static final String NAV3="navpane3";
    private static final String NAV4="navpane4";

    private static final String INP1="inppane1";
    private static final String INP2="inppane2";
    private static final String INP3="inppane3";
    private static final String INP4="inppane4";
    private static final String INP5="inppane5";

    private static final String MED1="medpane1";
    private static final String MED2="medpane2";
    private static final String MED3="medpane3";


    private String layoutTitle = "Layout and Navigation" ;
    private List<MenuEntry> layoutNavigation = new ArrayList<MenuEntry>();
    private String inputTitle = "Input and Selection";
    private List<MenuEntry> inputSelection = new ArrayList<MenuEntry>();
    private String mediaTitle = "Media Components";
    private List<MenuEntry> mediaComponents = new ArrayList<MenuEntry>() ;
    private String selLayoutNavPane = NAV1;
    private String selInputPane = INP1;
    private String selMediaPane = MED1;
    private String selAccPane =  ACC1;
    private String selPane = SPLASH;
    private List<MenuEntry> allPanes = new ArrayList<MenuEntry>();
    private String singleSelPane="mnuPanel";

	public MenuAccordionBean(){
     //   this.menuList.add(new MenuEntry("Menu Example", "none", true));
        this.layoutNavigation.add(new MenuEntry("Carousel", NAV1, false));
        this.layoutNavigation.add(new MenuEntry("ContentStack", NAV2, false));
        this.layoutNavigation.add(new MenuEntry("Tabset", NAV3, false));
        this.layoutNavigation.add(new MenuEntry("Accordion", NAV4, false));
        this.inputSelection.add(new MenuEntry("Buttons", INP1, false));
        this.inputSelection.add(new MenuEntry("Flip Switch", INP2, false));
        this.inputSelection.add(new MenuEntry("Geolocation", INP3, false));
        this.inputSelection.add(new MenuEntry("Date and Time", INP4, false));
        this.mediaComponents.add(new MenuEntry("Audio", MED1, false));
        this.mediaComponents.add(new MenuEntry("Image", MED2, false));
        this.mediaComponents.add(new MenuEntry("Video", MED3, false));
        allPanes.add(new MenuEntry("Layout and Navigation", null, true));
        allPanes.addAll(layoutNavigation);
        allPanes.add(new MenuEntry("Input and Selection", null, true));
        allPanes.addAll(inputSelection);
        allPanes.add(new MenuEntry("Media Components", null, false));
        allPanes.addAll(mediaComponents);
    }

    public List<MenuEntry> getAllPanes() {
        return allPanes;
    }

    public void setAllPanes(List<MenuEntry> allPanes) {
        this.allPanes = allPanes;
    }

    public String getSingleSelPane() {
        return singleSelPane;
    }

    public void setSingleSelPane(String singleSelPane) {
        this.singleSelPane = singleSelPane;
    }

    public String getSelPane() {
        return selPane;
    }

    public void setSelPane(String selPane) {
        this.selPane = selPane;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }

    public void setLayoutTitle(String layoutTitle) {
        this.layoutTitle = layoutTitle;
    }

    public List<MenuEntry> getLayoutNavigation() {
        return layoutNavigation;
    }

    public void setLayoutNavigation(List<MenuEntry> layoutNavigation) {
        this.layoutNavigation = layoutNavigation;
    }

    public String getInputTitle() {
        return inputTitle;
    }

    public void setInputTitle(String inputTitle) {
        this.inputTitle = inputTitle;
    }

    public List<MenuEntry> getInputSelection() {
        return inputSelection;
    }

    public void setInputSelection(List<MenuEntry> inputSelection) {
        this.inputSelection = inputSelection;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public List<MenuEntry> getMediaComponents() {
        return mediaComponents;
    }

    public void setMediaComponents(List<MenuEntry> mediaComponents) {
        this.mediaComponents = mediaComponents;
    }

    public String getSelLayoutNavPane() {
        return selLayoutNavPane;
    }

    public void setSelLayoutNavPane(String selLayoutNavPane) {
        this.selLayoutNavPane = selLayoutNavPane;
    }

    public String getSelInputPane() {
        return selInputPane;
    }

    public void setSelInputPane(String selInputPane) {
        this.selInputPane = selInputPane;
    }

    public String getSelMediaPane() {
        return selMediaPane;
    }

    public void setSelMediaPane(String selMediaPane) {
        this.selMediaPane = selMediaPane;
    }

    public String getSelAccPane() {
        return selAccPane;
    }

    public void setSelAccPane(String selAccPane) {
        this.selAccPane = selAccPane;
    }

    public static class MenuEntry implements Serializable{
        private String label;
        private String value;
        private String url;
        private String icon;
        private boolean menuHeader;

        public MenuEntry (String label, String value, boolean menuHeader){
            this.label = label;
            this.value = value;
            this.menuHeader = menuHeader;
        }
        public MenuEntry (String label, String value, boolean menuHeader, String url){
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



}
