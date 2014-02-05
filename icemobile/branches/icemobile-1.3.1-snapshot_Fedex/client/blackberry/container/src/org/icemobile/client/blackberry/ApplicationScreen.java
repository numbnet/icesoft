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
package org.icemobile.client.blackberry;

import org.icemobile.client.blackberry.menu.BackMenuItem;
import org.icemobile.client.blackberry.menu.ExitMenuItem;
import org.icemobile.client.blackberry.menu.HistorySubMenuItem;
import org.icemobile.client.blackberry.menu.LogMemoryMenuItem;
import org.icemobile.client.blackberry.menu.ReloadCurrentMenuItem;
import org.icemobile.client.blackberry.menu.ReloadHomeMenuItem;
import org.icemobile.client.blackberry.menu.ResetAuthorizationMenuItem;
import org.icemobile.client.blackberry.menu.ResetPushMenuItem;
import org.icemobile.client.blackberry.menu.TestJavascriptFullItem;
//import org.icemobile.client.blackberry.menu.ResetPushMenuItem;
//import org.icemobile.client.blackberry.menu.TestJavascriptMenuItem;
import org.icemobile.client.blackberry.menu.URLEntryMenu;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.menu.SubMenu;

 /**
  * A class extending the MainScreen class, which provides default standard
  * behavior for BlackBerry GUI applications.
  */
 public final class ApplicationScreen extends MainScreen {
     
     private ContainerController mController; 
     // Blackberry menu subclasses for the primary screen. Each subscreen will define 
     // context specific options within the Screen subclasses themselves. 
     private MenuItem mReloadMenu;        // reload Home page
     private MenuItem mReloadCurrentMenu; // reload current page 
     private MenuItem mURLMenu;           // allow user to manually define HOME_URL locally
                                          // without going into options system.
     private MenuItem mResetAuthCacheMenu; // Reset the authorization cache
     private MenuItem mLogMemMenu;
     
     
     private MenuItem mResetPushMenu;
     private MenuItem mBackMenu; 
     private MenuItem mTestJavascriptMenu;
     private MenuItem mExitContainerMenu;
     
     
     private HistorySubMenuItem[] mHistorySubs;
     
     /**
      * Creates a new MyScreen object
      */
     public ApplicationScreen( ContainerController controller ) {
         super(HORIZONTAL_SCROLL | VERTICAL_SCROLL); 
//         super();
         mController = controller; 
        
         mHistorySubs = new HistorySubMenuItem[ ICEmobileContainer.HISTORY_SIZE ];
         loadMenus();
     }

     // 

     public boolean onClose() { 
         UiApplication.getUiApplication().requestBackground(); 
         return false; 
     }

     public void close() { 

     }
     
     /**
      * Define the options subclasses for the MainScreen
      */
     private void loadMenus() {

         mReloadMenu = new ReloadHomeMenuItem(mController);         
         mReloadCurrentMenu = new ReloadCurrentMenuItem(mController);
//         mResetAudioMenu = new ResetAudioMenuItem( mContainer); 
         mResetPushMenu = new ResetPushMenuItem(mController);         
         mBackMenu = new BackMenuItem(mController); 
         mURLMenu = new URLEntryMenu(mController);
         mTestJavascriptMenu = new TestJavascriptFullItem(mController);
         mExitContainerMenu = new ExitMenuItem(mController);
         mResetAuthCacheMenu = new ResetAuthorizationMenuItem(mController); 
         mLogMemMenu = new LogMemoryMenuItem(mController);
         
        
         
     }
     
     protected void makeMenu ( Menu menu, int instance ) {     
    
    	 menu.add ( mReloadMenu ); 
    	 menu.add ( mReloadCurrentMenu);
    	 menu.add( mBackMenu );
//         menu.add (mResetAudioMenu);
//    	 menu.add (mRerunScriptMenu);
    	 menu.add(mURLMenu);
    	 menu.add(mResetAuthCacheMenu);
    	 menu.add(mLogMemMenu);
    	 menu.add(mTestJavascriptMenu);
    	 menu.add(mExitContainerMenu);
    	 menu.add(mResetPushMenu);  
    	 
    	 
    	 SubMenu urlSubmenu = new SubMenu(null, "URL History", 5, 3);
    	 if (mHistorySubs[0] == null) { 
    		 initSubmenuStructure();
    	 }
    	 for (int idx = 0; idx < mHistorySubs.length; idx ++ ) { 
    		 urlSubmenu.add( mHistorySubs[idx] );
    	 }

         menu.add( urlSubmenu );
     }

	private void initSubmenuStructure() {
		for (int idx = 0; idx < mHistorySubs.length; idx ++ ) { 
    		 mHistorySubs[idx] = new HistorySubMenuItem( mController, "" );  
    	 }
	} 
    	 
    	 
     /**
      * Notification to update the history menus
      */
     public void updateHistoryMenus() { 
    	 
    	 if (mHistorySubs[0] == null) { 
    		 initSubmenuStructure();
    	 }
    	 String[] urlHistory = mController.getHistoryManager().getHistoryLocations();
    	 for (int idx = 0; idx < mHistorySubs.length; idx ++ ) { 
    		 if (idx >= urlHistory.length || urlHistory[idx] == null) { 
    			 break;
    		 }
    		 // These can be null on initial page load when history is added 
    		 // before the menu has been 
    		 mHistorySubs[idx].setLabel( urlHistory[idx] );  
    	 } 
     }
 }
