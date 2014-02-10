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
package org.icemobile.client.blackberry.options;


import org.icemobile.client.blackberry.ContainerController;

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

    private ContainerController mController;
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
        return ContainerController.PRODUCT_ID;
    }

    public BlackberryOptionsProvider(ContainerController controller) {
        this.mController = controller;
        this.mOptionsProperties = BlackberryOptionsProperties.fetch();

    }

    public void populateMainScreen(MainScreen screen) {

        VerticalFieldManager vfm = new VerticalFieldManager();
        screen.add(vfm);


        // ---------- Home URL pair 

        LabelField homeLabel = new LabelField("Home URL:", Field.FIELD_LEFT);
        screen.add(homeLabel);

        mUrlField = new EditField("",
                                         mOptionsProperties.getHomeURL(),
                                         100,
                                         EditField.FILTER_URL | Field.FIELD_HCENTER);

        Background whiteBackground = BackgroundFactory.createSolidBackground(Color.WHITE);
        mUrlField.setBackground(whiteBackground);


        // A little bit smaller font for sizing
        Font F = mUrlField.getFont();
        Font newF = F.derive(Font.ANTIALIAS_DEFAULT, 20);

        mUrlField.setFont(newF);
        mUrlField.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                mOptionsProperties.setMode(BlackberryOptionsProperties.TEXT_MODE);
            }
        });

        screen.add(mUrlField);

        // -----------  recently defined HOME urls 


        LabelField urlList = new LabelField("Recent HOME URLs: ", Field.FIELD_LEFT);
        screen.add(urlList);

        mRecentURLs = new ObjectChoiceField("",
                                                   mOptionsProperties.getApplicationURLs(),
                                                   mOptionsProperties.getHomeURLIndex(),
                                                   Field.FIELD_HCENTER);

        mRecentURLs.setFont(newF);
        mRecentURLs.setBackground(whiteBackground);
        screen.add(mRecentURLs);

        mRecentURLs.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                mOptionsProperties.setMode(BlackberryOptionsProperties.DROP_MODE);
            }
        });


        // ----------------- Email Notification section 

        mUseEmailNotification = new CheckboxField("Use Email Notification",
                                                         mOptionsProperties.isUsingEmailNotification(),
                                                         Field.FIELD_LEFT);
        mUseEmailNotification.setFont(newF);

        mUseEmailNotification.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {

                final boolean enabled = mUseEmailNotification.getChecked();
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        setFieldState(enabled);
                    }
                });
            }

        });

        screen.add(mUseEmailNotification);


        mEmailEntryLabel = new LabelField("Email notification address: ", Field.FIELD_LEFT);
        screen.add(mEmailEntryLabel);

        mEmailNotification = new EmailAddressEditField("",
                                                              mOptionsProperties.getEmailNotification(),
                                                              50,
                                                              Field.FIELD_HCENTER);

        mEmailNotification.setBackground(whiteBackground);
        mEmailNotification.setFont(newF);
        screen.add(mEmailNotification);


        // ---- Reload Home page on exit button


        ButtonField reloadField = new ButtonField("Load Home URL on Re-entry",
                                                         Field.FIELD_BOTTOM);
        reloadField.setCommandContext(new Object() {
            public String toString() {
                return "Options Menu";
            }
        });

        reloadField.setCommand(new Command(new ReloadPageHandler()));
        screen.add(reloadField);

        setFieldState(mOptionsProperties.isUsingEmailNotification());
    }


    public void setFieldState(boolean enabled) {
        // blackberry labels  don't support enabled/disabled
        if (enabled) {
            mEmailEntryLabel.setVisualState(Field.VISUAL_STATE_NORMAL);
        } else {
            mEmailEntryLabel.setVisualState(Field.VISUAL_STATE_DISABLED);
        }
        mEmailNotification.setEnabled(enabled);
    }

    class ReloadPageHandler extends CommandHandler {

        public void execute(ReadOnlyCommandMetadata metadata, Object context) {
            mController.reloadApplicationOnReentry();
        }
    }

    public void save() {

        if (mOptionsProperties.getMode() == BlackberryOptionsProperties.TEXT_MODE) {
            mOptionsProperties.setHomeUrl(mUrlField.getText());
        } else {
            mOptionsProperties.setHomeURLIndex(mRecentURLs.getSelectedIndex());
        }

        boolean isChecked = mUseEmailNotification.getChecked();

        mOptionsProperties.setUsingEmailNotification(isChecked);
        if (isChecked) {
            mOptionsProperties.setEmailNotification(mEmailNotification.getText());
        }
        mOptionsProperties.save();
        mController.optionsChanged();
    }
}
