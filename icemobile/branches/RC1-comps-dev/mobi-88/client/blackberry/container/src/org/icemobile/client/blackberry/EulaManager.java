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
    private ICEmobileContainer mContainer;
   

    public EulaManager(final ICEmobileContainer container) {
        
        mContainer = container;
        mEula = container.readLocalFileSystem(getClass(), "/eula.html");
        
        // This may be somewhat problematic, but if the customer wishes no eula, 
        // don't try to show one, accept, and move on. 
        if (mEula == null || mEula.trim().length() == 0) { 
            return; 
        }

        BrowserFieldConfig bfc = new BrowserFieldConfig(); 
        bfc.setProperty(BrowserFieldConfig.JAVASCRIPT_ENABLED, Boolean.TRUE);
        
        Background black = BackgroundFactory.createSolidBackground( Color.BLACK );

        VerticalFieldManager vfm = new VerticalFieldManager();
        vfm.setBackground( black );
        mPopup = new MainScreen();         
        mPopup.add(vfm);
        
        mBrowserField = new BrowserField( bfc );
        mBrowserField.displayContent(mEula, "");

        ButtonField okButton = new ButtonField("OK", Field.FIELD_HCENTER); 
        okButton.setChangeListener( new FieldChangeListener() { 

            public void fieldChanged( Field field, int context) {
                UiApplication ui = UiApplication.getUiApplication(); 
                ui.popScreen( ui.getActiveScreen() ); 
                mPopup.deleteAll();
                mPopup = null;
                container.accept();
                container.init();
                
            }        	
        }); 

        vfm.add(mBrowserField);
        vfm.add(okButton);
    }

    public void show() {

        if (mEula == null || mEula.trim().length()==0 ) { 
            ICEmobileContainer.DEBUG("Skipping eula check");
            mContainer.accept(); 
            mContainer.init();
            return;
        }
        
        UiApplication.getUiApplication().invokeLater( new Runnable() { 
            public void run() { 
                UiApplication.getUiApplication().pushModalScreen( mPopup );
            }
        }); 
    }	
}
