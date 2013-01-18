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
package org.icemobile.client.blackberry.menu;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.util.StringProvider;
import net.rim.device.api.ui.component.DialogClosedListener; 

import org.icemobile.client.blackberry.ContainerController;

/**
 * This MenuItem allows a user to type in a value for HOME_URL without going to
 * the options system
 * 
 */
public class URLEntryMenu extends MenuItem {

    private ContainerController mController;
    private EditField mURLField;
    

    public URLEntryMenu(ContainerController controller ) {
        super(new StringProvider("Open URL..."), 5, 0);
        mController = controller;
        super.setCommand(new Command(new URLHandler()));
    }

    class URLHandler extends CommandHandler {
        
        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            
            String url; 
            url = mController.getQuickURL();
            
            final Dialog newURLDialog = new 
                   Dialog(Dialog.D_OK_CANCEL,
                          "Enter URL:",
                          Dialog.OK, 
                          Bitmap.getPredefinedBitmap(Bitmap.QUESTION), 
                          Field.FIELD_HCENTER); 
                 
            newURLDialog.setDialogClosedListener( new DialogClosedListener() {  
                
                public void dialogClosed( Dialog dialog, int choice) { 
                    if (choice == Dialog.D_OK) { 
                        String url = mURLField.getText();                    
                        mController.setQuickURL( url );
                    }
                }            
            }); 
            
            mURLField = new  EditField( "", 
                    url, 
                    120, 
                    EditField.FILTER_URL | Field.FIELD_HCENTER);
            
            XYEdges edges = new XYEdges( 2, 2, 2, 2);
            Border whiteBorder = BorderFactory.createBevelBorder( edges ); 
            mURLField.setBorder(whiteBorder);
            
            newURLDialog.add (mURLField);
            // pop up a dialog 
            // fetch result
            // set the current HOME URL on the container 
            //   (which must persist the URL as home url and persist
            UiApplication.getUiApplication().invokeLater( new Runnable () { 
                public void run() { 
                    newURLDialog.show();  
                }
            }); 
           
        }
    }
}
