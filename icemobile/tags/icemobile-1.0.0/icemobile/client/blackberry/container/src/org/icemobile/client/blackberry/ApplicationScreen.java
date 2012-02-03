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
package org.icemobile.client.blackberry;

import org.icemobile.client.blackberry.menu.BackMenuItem;
import org.icemobile.client.blackberry.menu.HistorySubMenuItem;
import org.icemobile.client.blackberry.menu.ReloadCurrentMenuItem;
import org.icemobile.client.blackberry.menu.ReloadHomeMenuItem;
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
     
     
//     private MenuItem mResetPushMenu;
     private MenuItem mBackMenu; 
//     private MenuItem mTestJavascriptMenu;
     
     
     private HistorySubMenuItem[] mHistorySubs;
     
     /**
      * Creates a new MyScreen object
      */
     public ApplicationScreen( ContainerController controller ) {
         super(); 
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
//         mResetPushMenu = new ResetPushMenuItem(mController);         
         mBackMenu = new BackMenuItem(mController); 
         mURLMenu = new URLEntryMenu(mController);
//         mTestJavascriptMenu = new TestJavascriptMenuItem(mContainer);
         
        
         
     }
     
     protected void makeMenu ( Menu menu, int instance ) {     
    
    	 menu.add ( mReloadMenu ); 
    	 menu.add ( mReloadCurrentMenu);
    	 menu.add( mBackMenu );
//         menu.add (mResetAudioMenu);
//    	 menu.add (mRerunScriptMenu);
    	 menu.add(mURLMenu);
//    	 menu.add(mTestJavascriptMenu);
//    	 menu.add(mResetPushMenu);  
    	 
    	 
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
