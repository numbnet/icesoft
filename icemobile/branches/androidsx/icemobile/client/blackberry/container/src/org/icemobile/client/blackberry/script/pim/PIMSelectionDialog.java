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

package org.icemobile.client.blackberry.script.pim;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;

import org.icemobile.client.blackberry.Logger;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.table.SimpleList;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

/**
 * A very simple model dialog to capture user's name and password
 */
public class PIMSelectionDialog extends Dialog 
{
    
    private PasswordEditField passwordField;
    
    private static final String DEFAULT_CHOICES[] = { "Ok", "Cancel" };
    private static final int DEFAULT_VALUES[] = { Dialog.OK, Dialog.CANCEL };
    
    private int mChoice; 

    public PIMSelectionDialog(String title, String contactInfo[], boolean selectMultiple) {
        this(title, DEFAULT_CHOICES, DEFAULT_VALUES, contactInfo, selectMultiple );
    }
    
    public PIMSelectionDialog(String title, String choices[], int values[], String contactInfo[], boolean selectMultiple)
    {
        super(title, choices, values, Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.INFORMATION), Dialog.GLOBAL_STATUS);
        
        Manager delegate = getDelegate();
        ChoiceGroup group; 
        if (selectMultiple) { 
        	group = new ChoiceGroup("Select Contacts from list", Choice.MULTIPLE);
        } else { 
        	group = new ChoiceGroup("Select Contact from list", Choice.EXCLUSIVE); 
        }
        SimpleList listField = new SimpleList(delegate);   
        if ( delegate instanceof DialogFieldManager ) { 

        	DialogFieldManager dfm = (DialogFieldManager)delegate;
            Manager manager = dfm.getCustomManager();
            
            for (int idx = 0; idx < contactInfo.length; idx ++) { 

            	HorizontalFieldManager hfm = new HorizontalFieldManager(); 
            	LabelField label = new LabelField(contactInfo[idx]); 
            	hfm.add(label); 
            	CheckboxField select = new CheckboxField("", false, HorizontalFieldManager.RIGHTMOST); 
            	hfm.add(select); 

            	if( manager != null ) { 
            		manager.insert( hfm, idx);
            	} 
            }
        }       
    }
    
    /**
     * Strategy specific authorization in this case 
     * prompts for username/password via a popup. 
     */
    public void doSelection() { 
    	
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
    	String username= null; //  = getUsername();
    	String password= null; //  = getPassword();
    	
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
    
//    private String getUsername() {
//        return userNameField.getText();
//    }
//    
//    private String getPassword() {
//        return passwordField.getText();
//    }
    
	public int fetchResult() {
		return mChoice;
	}

}