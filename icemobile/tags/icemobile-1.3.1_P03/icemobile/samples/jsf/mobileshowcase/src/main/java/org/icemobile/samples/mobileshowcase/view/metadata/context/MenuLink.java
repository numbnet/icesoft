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

package org.icemobile.samples.mobileshowcase.view.metadata.context;

/**
 *
 */
public class MenuLink {

    private String title;
    private boolean isDisabled;
    private boolean isDefault;
    private boolean isNew;
    private String exampleBeanName;
    private String examplePanelId;
    private boolean redirect;

    public MenuLink(String title, boolean aDefault, boolean aNew, boolean isDisabled, String exampleBeanName) {
        this.title = title;
        this.isDisabled = isDisabled;
        isDefault = aDefault;
        isNew = aNew;
        this.exampleBeanName = exampleBeanName;
    }

    public MenuLink(String title, boolean aDefault, boolean aNew, boolean isDisabled, String exampleBeanName, 
            String examplePanelId, boolean redirect) {
        this.title = title;
        isDefault = aDefault;
        isNew = aNew;
        this.exampleBeanName = exampleBeanName;
        this.examplePanelId = examplePanelId;
        this.redirect = redirect;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getExampleBeanName() {
        return exampleBeanName;
    }

    public String getExamplePanelId() {
        return examplePanelId;
    }
    
    public boolean isRedirect(){
        return redirect;
    }
}