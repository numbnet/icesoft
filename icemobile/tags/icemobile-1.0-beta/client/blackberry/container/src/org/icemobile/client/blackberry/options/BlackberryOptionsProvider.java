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
package org.icemobile.client.blackberry.options;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.blackberry.api.options.OptionsProvider;
import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.EmailAddressEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class BlackberryOptionsProvider implements OptionsProvider {

    private ICEmobileContainer mContainer; 
    private BlackberryOptionsProperties mOptionsProperties;
    private EditField mUrlField;
    private ObjectChoiceField mRecentURLs;
    private CheckboxField mUseEmailNotification; 
    private LabelField mEmailEntryLabel;
    
    
    private EmailAddressEditField mEmailNotification; 
//    private CheckboxField mBrowsePhotos; 
//    private CheckboxField mBrowseVideos;


    public String getTitle() {
        // TODO Auto-generated method stub
        return "ICEmobile Container Preferences";
    }

    public BlackberryOptionsProvider (ICEmobileContainer container) {
        this.mContainer = container; 		
        this.mOptionsProperties = BlackberryOptionsProperties.fetch();	

    }

    public void populateMainScreen(MainScreen screen) {

        VerticalFieldManager vfm = new VerticalFieldManager();
        screen.add (vfm);
        
        
        // ---------- Home URL pair 
        
        LabelField homeLabel = new LabelField("Home URL:", Field.FIELD_LEFT);
        screen.add(  homeLabel ); 
        
        mUrlField = new EditField( "", 
                mOptionsProperties.getHomeURL(), 
                100, 
                EditField.FILTER_URL | Field.FIELD_HCENTER);
        
        Background whiteBackground = BackgroundFactory.createSolidBackground(Color.WHITE);
        mUrlField.setBackground(whiteBackground);
        

        // A little bit smaller font for sizing
        Font F = mUrlField.getFont(); 
        Font newF = F.derive( Font.ANTIALIAS_DEFAULT, 20);
        
        mUrlField.setFont(newF);
        mUrlField.setChangeListener(new FieldChangeListener()  { 
            public void fieldChanged(Field field, int context ) {
                mOptionsProperties.setMode( BlackberryOptionsProperties.TEXT_MODE );
            }			
        }); 

        screen.add(mUrlField);
        
        // -----------  recently defined HOME urls 
        

        LabelField urlList = new LabelField ("Recent HOME URLs: ", Field.FIELD_LEFT);
        screen.add (urlList); 
        
        mRecentURLs  = new ObjectChoiceField("", 
                mOptionsProperties.getApplicationURLs(), 
                mOptionsProperties.getHomeURLIndex(), 
                Field.FIELD_HCENTER);

        mRecentURLs.setFont(newF);
        mRecentURLs.setBackground( whiteBackground );
        screen.add( mRecentURLs );

        mRecentURLs.setChangeListener( new FieldChangeListener () {
            public void fieldChanged(Field field, int context) {
                mOptionsProperties.setMode(BlackberryOptionsProperties.DROP_MODE);
            } 			
        }); 		
    
        
        // ----------------- Email Notification section 
        
        mUseEmailNotification = new CheckboxField( "Use Email Notification", 
                mOptionsProperties.isUsingEmailNotification(), 
                Field.FIELD_LEFT);
        mUseEmailNotification.setFont( newF ); 
        
        mUseEmailNotification.setChangeListener( new FieldChangeListener () {
            public void fieldChanged(Field field, int context) {
                
                final boolean enabled = mUseEmailNotification.getChecked(); 
                UiApplication.getUiApplication().invokeLater(new Runnable() { 
                    public void run() { 
                        setFieldState( enabled );
                    }
                }); 
            }   
            
        });         
        
        screen.add( mUseEmailNotification );
                
        
        mEmailEntryLabel = new LabelField ("Email notification address: ", Field.FIELD_LEFT);
        screen.add ( mEmailEntryLabel );
        
        mEmailNotification = new EmailAddressEditField( "", 
                mOptionsProperties.getEmailNotification(), 
                50, 
                Field.FIELD_HCENTER); 

        mEmailNotification.setBackground(whiteBackground);
        mEmailNotification.setFont(newF);
        screen.add(mEmailNotification);        
        
        
        
          // ---- Reload Home page on exit button 
        
        
        
        ButtonField reloadField = new ButtonField("Load Home URL on Re-entry", 
                Field.FIELD_BOTTOM );
        reloadField.setCommandContext( new Object() { 
            public String toString() { 
                return "Options Menu"; 
            }       
        });

        reloadField.setCommand(new Command (new ReloadPageHandler()));      
        screen.add( reloadField );
        
        setFieldState ( mOptionsProperties.isUsingEmailNotification());
    }
    

    public void setFieldState(boolean enabled) { 
        // blackberry labels  don't support enabled/disabled
        if (enabled) { 
            mEmailEntryLabel.setVisualState(Field.VISUAL_STATE_NORMAL);
        } else { 
            mEmailEntryLabel.setVisualState ( Field.VISUAL_STATE_DISABLED);
        }
        mEmailNotification.setEnabled(enabled);                
    }
    
    class ReloadPageHandler extends CommandHandler { 

        public void execute(ReadOnlyCommandMetadata metadata, Object context) { 
            mContainer.reloadApplication();
        }
    } 

    public void save() {

        if (mOptionsProperties.getMode() == BlackberryOptionsProperties.TEXT_MODE) {			
            mOptionsProperties.setHomeUrl( mUrlField.getText() );
        } else { 			
            mOptionsProperties.setHomeURLIndex( mRecentURLs.getSelectedIndex() );
        }
              
        boolean isChecked = mUseEmailNotification.getChecked(); 
        
        mOptionsProperties.setUsingEmailNotification( isChecked );
        if (isChecked) { 
            mOptionsProperties.setEmailNotification( mEmailNotification.getText() );   
        }
        mOptionsProperties.save(); 				        
        mContainer.optionsChanged(); 
    }
}
