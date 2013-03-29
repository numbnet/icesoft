/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.client.blackberry.authentication;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.icemobile.client.blackberry.Logger;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

/**
 * A very simple model dialog to capture user's name and password
 */
public class UserAuthenticationDialogStrategy extends Dialog implements AuthenticationStrategy
{
    private EditField userNameField;
    private PasswordEditField passwordField;
    
    private static final String DEFAULT_CHOICES[] = { "Ok", "Cancel" };
    private static final int DEFAULT_VALUES[] = { Dialog.OK, Dialog.CANCEL };
    
    private int mChoice; 

    public UserAuthenticationDialogStrategy(String title) {
        this(title, DEFAULT_CHOICES, DEFAULT_VALUES );
    }
    
    public UserAuthenticationDialogStrategy(String title, String choices[], int values[])
    {
        super(title, choices, values, Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.INFORMATION), Dialog.GLOBAL_STATUS);
        
        userNameField = new EditField("User: ", "", 10, EditField.EDITABLE);
        XYEdges edges = new XYEdges(2, 2, 2, 2);
        Border whiteBorder = BorderFactory.createBevelBorder(edges);
        userNameField.setBorder(whiteBorder); 
        
        
        passwordField = new PasswordEditField("Password: ", "", 10, EditField.EDITABLE);
        passwordField.setBorder(whiteBorder);
        
        Manager delegate = getDelegate();
        if( delegate instanceof DialogFieldManager )
        {
            DialogFieldManager dfm = (DialogFieldManager)delegate;
            Manager manager = dfm.getCustomManager();
            if( manager != null )
            {
                manager.insert(userNameField, 0);
                manager.insert(passwordField, 1);
            }
        }
    }
    
    /**
     * Strategy specific authorization in this case 
     * prompts for username/password via a popup. 
     */
    public void doAuthorization() { 
    	
    	UiApplication.getUiApplication().invokeAndWait(new Runnable() { 
        	public void run() { 
        		mChoice = doModal();
        	}
        });     	
    }
    
    /**
     * Retrieve the value for the 'Authorize' header encoded in the Strategy 
     * specific way
     */
    public String getAuthorizationValue( ) { 
    	String username = getUsername();
    	String password = getPassword();
    	
		if (username == null || password == null) { 
			Logger.ERROR("Error return from Strategy: (null value) username: " + username + 
					", password: " + password);
		}
		
		StringBuffer response = new StringBuffer();
        response.append( username );
        response.append( ':' );
        response.append( password );

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Base64OutputStream b64os = new Base64OutputStream( baos );
            b64os.write( response.toString().getBytes() );
            b64os.close();
            return "Basic " + baos.toString();
		
		} catch (IOException ioe) { 
			Logger.ERROR("Basic Dialog IOException encoding details?: " + ioe);
		} 
		return ""; 
	} 
    
    private String getUsername() {
        return userNameField.getText();
    }
    
    private String getPassword() {
        return passwordField.getText();
    }
    
	public int fetchResult() {
		return mChoice;
	}

}