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
import org.icemobile.client.blackberry.ICEmobileContainer;

/**
 * This MenuItem is intended to execute from the Blackberry 
 * menu system and allows reloading the main application URL 
 * without having switched 'from' the application as in the options 
 * case. 
 */
public class ReloadHomeMenuItem extends MenuItem {

    private ContainerController mController; 

    public ReloadHomeMenuItem(ContainerController controller) { 
        super(new StringProvider("Home"), 1, 0);
        mController = controller;
        super.setCommand( new Command( new ReloadPageImmediateHandler() ));
    }

    class ReloadPageImmediateHandler extends CommandHandler { 

        public ReloadPageImmediateHandler () {
            super();
        }

        public void execute(ReadOnlyCommandMetadata metadata, Object context) { 
            mController.loadPage( ICEmobileContainer.HOME_URL );
        }
    } 

}
