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

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class EulaManager {

    private String mEula; 
    
    private BrowserField mBrowserField; 
    private Screen mPopup; 
    private ContainerController mController;

    public EulaManager(final ContainerController controller) {
        
        mController = controller;
        mEula = controller.readLocalFileSystem(getClass(), "/eula.html");
        
        // This may be somewhat problematic, but if the customer wishes no eula, 
        // don't try to show one, accept, and move on. 
        if (mEula == null || mEula.trim().length() == 0) { 
            return; 
        }

        BrowserFieldConfig bfc = new BrowserFieldConfig(); 
        bfc.setProperty(BrowserFieldConfig.JAVASCRIPT_ENABLED, Boolean.TRUE);
        
        Background black = BackgroundFactory.createSolidBackground( Color.BLACK );

        final VerticalFieldManager vfm = new VerticalFieldManager();
        vfm.setBackground( black );
        mPopup = new MainScreen();         
        mPopup.add(vfm);

        
        mBrowserField = new BrowserField( bfc );
        mBrowserField.displayContent(mEula, "");

        final ButtonField okButton = new ButtonField("OK", Field.FIELD_HCENTER); 
        okButton.setChangeListener( new FieldChangeListener() { 

            public void fieldChanged( Field field, int context) {
                UiApplication ui = UiApplication.getUiApplication(); 
                ui.popScreen( ui.getActiveScreen() ); 
                mPopup.deleteAll();
                mPopup = null;
                controller.acceptEula();
                controller.init();
                
            }        	
        }); 

        vfm.add(mBrowserField);
        UiApplication.getUiApplication().invokeLater( new Runnable() {
            public void run() { 
                                
                synchronized (this) { 
                    try { 
                        this.wait(2500);
                    
                    } catch (InterruptedException ie) {}
                    vfm.add(okButton);
                } 
            }
        } );        
    }

    public void show() {

        if (mEula == null || mEula.trim().length()==0 ) { 
        	Logger.DEBUG("Skipping eula check");
            mController.acceptEula(); 
            mController.init();
            return;
        }
        
        UiApplication.getUiApplication().invokeLater( new Runnable() { 
            public void run() { 
                UiApplication.getUiApplication().pushModalScreen( mPopup );
            }
        }); 
    }	
}
