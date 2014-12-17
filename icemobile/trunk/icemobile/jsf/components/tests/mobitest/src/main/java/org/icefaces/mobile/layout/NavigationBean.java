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

import java.io.Serializable;
import java.lang.String;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "navigationBean")
@ViewScoped
public class NavigationBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private String defaultView;
    private boolean forceView;
    private String accordionPane="sentPanel2";
	private String singleColSelectedPane = "mnuPanelS";
	private String twoColSelectedPane = "panel1L";
	private String selectedTabPane = "panelt1";
    private String selPane = "panel1";

    public NavigationBean(){
        this.defaultView="large";
    }
	public String getSingleColSelectedPane() {
		return singleColSelectedPane;
	}
	public void setSingleColSelectedPane(String singleColSelectedPane) {
		this.singleColSelectedPane = singleColSelectedPane;
	}
	public String getTwoColSelectedPane() {
		return twoColSelectedPane;
	}
	public void setTwoColSelectedPane(String twoColSelectedPane) {
		this.twoColSelectedPane = twoColSelectedPane;
	}
	public String getSelectedTabPane() {
		return selectedTabPane;
	}
	public void setSelectedTabPane(String selectedTabPane) {
		this.selectedTabPane = selectedTabPane;
	}

    public String getSelPane() {
        return selPane;
    }

    public void setSelPane(String selPane) {
        this.selPane = selPane;
    }

    public String getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }

    public boolean isForceView() {
        return forceView;
    }

    public void setForceView(boolean forceView) {
        this.forceView = forceView;
    }

    public String getAccordionPane() {
        return accordionPane;
    }

    public void setAccordionPane(String accordionPane) {
        this.accordionPane = accordionPane;
    }
}
