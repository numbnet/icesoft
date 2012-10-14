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

package org.icemobile.samples.mobileshowcase.view.theme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Simple Model for keeping track of the current them.  The class also handles
 * actionListener used to change the theme in the UI.
 */
@ManagedBean
@SessionScoped
public class ThemeBean implements Serializable {

    // empty theme name will default to auto on the select menu
    private ThemeData currentTheme;
    private List<ThemeData> largeThemes;
    private List<ThemeData> smallThemes;
    
    public ThemeBean(){
        largeThemes = new ArrayList<ThemeData>();
        largeThemes.add(new ThemeData("","Auto"));
        largeThemes.add(new ThemeData("ipad","iPad"));
        largeThemes.add(new ThemeData("honeycomb","Android"));
        
        smallThemes = new ArrayList<ThemeData>();
        smallThemes.add(new ThemeData("","Auto"));
        smallThemes.add(new ThemeData("iphone","iPhone"));
        smallThemes.add(new ThemeData("android","Android"));
        smallThemes.add(new ThemeData("bberry","Blackberry"));
    }

    public ThemeData getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(ThemeData currentTheme) {
        this.currentTheme = currentTheme;
    }
    
    public class ThemeData implements Serializable{
        private String name;
        private String label;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getLabel() {
            return label;
        }
        public void setLabel(String label) {
            this.label = label;
        }
        public ThemeData(String name, String label) {
            super();
            this.name = name;
            this.label = label;
        }
        
        public String setTheme(){
            currentTheme = this;
            return "showcase?faces-redirect=true";
        }
        
    }

    public List<ThemeData> getLargeThemes() {
        return largeThemes;
    }

    public List<ThemeData> getSmallThemes() {
        return smallThemes;
    }
}
