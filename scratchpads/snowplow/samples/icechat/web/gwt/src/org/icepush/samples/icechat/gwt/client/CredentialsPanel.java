/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.uibinder.client.UiField;

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
       this.usernameLabel.setText(username);
    }
    
    @UiField
    Label usernameLabel;

    @UiTemplate(value="CredentialsPanelLayout.ui.xml")
    public interface CredentialsPanelLayoutBinder extends UiBinder<Panel, CredentialsPanel>{};


	
}
