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
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class BlackberryOptionsProvider implements OptionsProvider {

    private ICEmobileContainer mContainer; 
    private BlackberryOptionsProperties mOptionsProperties;
    private EditField mUrlField;
    private ObjectChoiceField mRecentURLs;
    
//    private TextField mEmailNotification; 
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
        
        Border aBorder;
        XYEdges borderEdges = new XYEdges( 2, 5, 5, 5);

        aBorder = BorderFactory.createRoundedBorder( borderEdges, Color.DARKGRAY, Border.STYLE_SOLID );
        
        mUrlField = new EditField( "Home URL:  ", 
                mOptionsProperties.getHomeURL(), 
                100, 
                EditField.FILTER_URL);
        
        Background whiteBackground = BackgroundFactory.createSolidBackground(Color.WHITE);
        mUrlField.setBackground(whiteBackground);
        
        mUrlField.setBorder( aBorder );

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

        mRecentURLs  = new ObjectChoiceField("Alternative HOME URLs:", 
                mOptionsProperties.getApplicationURLs(), 
                mOptionsProperties.getHomeURLIndex(), 
                Field.FIELD_LEADING);

        mRecentURLs.setFont(newF);
        mRecentURLs.setBorder( aBorder ); 
        mRecentURLs.setBackground( whiteBackground );
        screen.add( mRecentURLs );

        mRecentURLs.setChangeListener( new FieldChangeListener () {
            public void fieldChanged(Field field, int context) {
                mOptionsProperties.setMode(BlackberryOptionsProperties.DROP_MODE);
            } 			
        }); 		

        ButtonField reloadField = new ButtonField("Load Home URL on Re-entry");
        reloadField.setBorder( aBorder );
        reloadField.setCommandContext( new Object() { 
            public String toString() { 
                return "Options Menu"; 
            }		
        });

        reloadField.setCommand(new Command (new ReloadPageHandler()));		
        screen.add( reloadField );
        
        /*
        mBrowsePhotos = new CheckboxField("Browse photo gallery mode", mOptionsProperties.isUsePhotoGallery());
        mBrowsePhotos.setBorder( aBorder );
        mBrowsePhotos.setBackground( whiteBackground );
        mBrowsePhotos.setFont(newF);
        screen.add(mBrowsePhotos);
        
        mBrowseVideos = new CheckboxField("Browse video gallery mode", mOptionsProperties.isUseVideoGallery());
        mBrowseVideos.setFont ( newF );
        mBrowseVideos.setBorder( aBorder );
        mBrowseVideos.setBackground( whiteBackground );
        screen.add (mBrowseVideos);
         
        
        mEmailNotification = new BasicEditField( "Email notification address:   ", 
                mOptionsProperties.getEmailNotification(), 
                50, 
                BasicEditField.FILTER_EMAIL); 

        mEmailNotification.setBackground(whiteBackground);
        mEmailNotification.setFont(newF);
        mEmailNotification.setBorder ( aBorder );
        screen.add(mEmailNotification);
        
        */
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
        
//        mOptionsProperties.setUsePhotoGallery( mBrowsePhotos.getChecked() ); 
//        mOptionsProperties.setUseVideoGallery( mBrowseVideos.getChecked() );
//        mOptionsProperties.setEmailNotification( mEmailNotification.getText() );         
        mOptionsProperties.save(); 				
        
        mContainer.optionsChanged(); 
    }
}
