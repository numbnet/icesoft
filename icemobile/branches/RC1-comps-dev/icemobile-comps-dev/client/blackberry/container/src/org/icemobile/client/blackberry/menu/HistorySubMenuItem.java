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