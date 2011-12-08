/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
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
