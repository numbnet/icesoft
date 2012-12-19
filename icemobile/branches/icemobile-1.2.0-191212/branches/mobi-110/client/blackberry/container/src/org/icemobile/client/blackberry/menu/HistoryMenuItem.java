/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */ 
package org.icemobile.client.blackberry.menu;


import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.util.StringProvider;

import org.icemobile.client.blackberry.ICEmobileContainer;

/**
 * This MenuItem is intended to execute from the Blackberry 
 * menu system and manifests as a submenu of the history
 *
 */
public class HistoryMenuItem extends MenuItem {

    private ICEmobileContainer mContainer; 

    public HistoryMenuItem(ICEmobileContainer container) { 
        super(new StringProvider("Reload page"), 4, 0);
        mContainer = container;
        super.setCommand( new Command( new ReloadPageImmediateHandler() ));
    }

    public Object run(Object arg0) {

        mContainer.reloadApplicationImmediately();
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        return "Reload Main";
    }


    class ReloadPageImmediateHandler extends CommandHandler { 

        public ReloadPageImmediateHandler () {
            super();
        }
        // TODO Auto-generated constructor stub}

        public void execute(ReadOnlyCommandMetadata metadata, Object context) { 
            mContainer.reloadApplicationImmediately();
        }
    } 

}
