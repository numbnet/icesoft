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

package org.icemobile.samples.mediacast.navigation;


import org.icefaces.application.PushRenderer;
import org.icemobile.samples.mediacast.MediaController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Stack;

/**
 * Model for a mobile navigation.  Composed of a stack of the navigation
 * hierarchy of the app.  Navigation in mobile application is generally linear
 * with no branching.  There are branch exception around application errors
 * which might corrupt the navigation state.
 */
@ManagedBean(name = NavigationModel.BEAN_NAME)
@SessionScoped
public class NavigationModel implements Serializable {

    public static final String BEAN_NAME = "navigationModel";
    // selected panel in stack, default id is 'home'.
    private String selectedPanelId = "home";
    private Stack<String> history = new Stack<String>();

    public NavigationModel() {
        // add the current view the session group
        PushRenderer.addCurrentSession(MediaController.RENDER_GROUP);
    }

    public void goForward(String panelId) {
        history.push(selectedPanelId);
        selectedPanelId = panelId;
    }

    public void goBack() {
        if (!history.isEmpty()) {
            selectedPanelId = history.pop();
        }
    }

    public boolean isCanGoBack() {
        return !history.isEmpty();
    }

    public String getSelectedPanelId() {
        return selectedPanelId;
    }
}
