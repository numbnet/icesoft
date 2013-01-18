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
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.util.StringProvider;

import org.icemobile.client.blackberry.ContainerController;

/**
 * This menu item is used as a URL history submenu item 
 * displaying a URL that had been visited previously. 
 *
 */
public class HistorySubMenuItem  extends MenuItem {

    private ContainerController mController; 
    private String mUrl;

    public HistorySubMenuItem(ContainerController controller, String url) {
    	
        super( new StringProvider( url ), 4, 0);
        mUrl = url;
        mController = controller;
        super.setCommand( new Command( new LoadPageHandler() ));
    }

    class LoadPageHandler extends CommandHandler { 

        public LoadPageHandler () {
            super();
        }

        public void execute(ReadOnlyCommandMetadata metadata, Object context) { 
            mController.loadPage( mUrl );
        }
    } 
    
    public void setLabel(String newUrl ) {
    	// keep the original for loading, but show a shorter version 
    	mUrl = newUrl;
    	
    	String label = newUrl; 
    	int spos = newUrl.indexOf("//"); 
    	if (spos > 0) { 
    		spos = newUrl.indexOf("/", spos+2 ); 
    		if (spos > 0) { 
    			label = "..." + newUrl.substring( spos ); 
    		}
    	}    	
    	super.setText(new StringProvider ( label ));
    }
}