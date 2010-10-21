/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.gwt.client.screens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import org.icepush.samples.icechat.gwt.client.UserSession;

/**
 *
 * @author icesoft
 */
public class CredentialsPanel extends Composite{

    private static final CredentialsPanelLayoutBinder binder = GWT.create(CredentialsPanelLayoutBinder.class);

    public CredentialsPanel(){
       MainPanelRegistry.getInstance().registerCredentialsPanel(this);
       initWidget(binder.createAndBindUi(this));
    }

    public void setUsername(String username){
        this.setVisible(true);
       this.usernameLabel.setText(username);
    }
    
    @UiField
    Label usernameLabel;

    @UiHandler(value={"logoutButton"})
    public void logout(ClickEvent ev){
        UserSession.logout();
        Window.Location.assign("/icechat-gwt");
        
    }
    @UiTemplate(value="CredentialsPanelLayout.ui.xml")
    public interface CredentialsPanelLayoutBinder extends UiBinder<Panel, CredentialsPanel>{};


	
}
