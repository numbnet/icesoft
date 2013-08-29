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
package org.icemobile.client.blackberry.menu;


import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.DialogClosedListener;
import net.rim.device.api.util.StringProvider;

import org.icemobile.client.blackberry.ContainerController;

/**
 * This MenuItem is intended to execute from the Blackberry 
 * menu system and allows a traditional browser 'back' function
 *
 */
public class ExitMenuItem extends MenuItem {

    private ContainerController mController; 

    public ExitMenuItem(ContainerController controller) { 
        super(new StringProvider("Exit Container..."), 20, 0);
        mController = controller;
        super.setCommand( new Command( new ExitHandler() ));
    }
    

    class ExitHandler extends CommandHandler { 

        public void execute(ReadOnlyCommandMetadata metadata, Object context) { 
        	
        	final Dialog newDialog = new 
            Dialog(Dialog.D_OK_CANCEL,
                   "Are You Sure You Wish To Exit",
                   Dialog.CANCEL, 
                   Bitmap.getPredefinedBitmap(Bitmap.QUESTION), 
                   Field.FIELD_HCENTER); 
          
        	newDialog.setDialogClosedListener( new DialogClosedListener() {  

        		public void dialogClosed( Dialog dialog, int choice) { 
        			if (choice == Dialog.D_OK) { 
        				mController.shutdownContainer();

        			}
        		}            
        	}); 

        	// pop up a dialog 
            // fetch result
            // set the current HOME URL on the container 
            //   (which must persist the URL as home url and persist
            UiApplication.getUiApplication().invokeLater( new Runnable () { 
                public void run() { 
                    newDialog.show();  
                }
            }); 
        	
        }
    } 
}
