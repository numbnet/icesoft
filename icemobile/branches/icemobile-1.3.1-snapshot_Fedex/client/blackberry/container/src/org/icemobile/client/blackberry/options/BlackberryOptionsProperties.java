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
import org.icemobile.client.blackberry.ICEmobileContainer;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;


/**
 * The values in this class are Persisted between starts.  
 * They are flagged as such with the Persistable interface
 *
 */
public class BlackberryOptionsProperties implements Persistable {

    private String mHomeUrl;
    private String[] mApplicationURLs;
    private int mSelectedIndex;
    
    private Boolean mUsingEmailNotification; 
        
    // Email address to use for email notification
    private String mEmailNotification= "";
    
    // invisible flag indicating whether the eula has been viewed. 
    private Boolean mEulaViewed; 
    
    private String mQuickURL; 
    

    // Current mode of choosing mainApplication URL, see below 
    private int mMode; 

    // TextField or dropdown list 
    public static final int TEXT_MODE = 1; 
    public static final int DROP_MODE = 2; 

    private final String UNUSED = "- unused -";

    // type a string and right click -> Convert String to Long 
    // When this value is changed, reference to previous persisted 
    // values is lost 

    //Persistent object wrapping the effective properties instance
    private static PersistentObject store;

    //Ensure that an effective properties set exists on startup.
    static {
        store = PersistentStore.getPersistentObject( ContainerController.GUID );
        synchronized (store) {
            if (store.getContents() == null) {
                store.setContents(new BlackberryOptionsProperties());
                store.commit();
            }
        }
    }

    /**
     * Construct a new version of this class when there is no 
     * version persisted. 
     */
    private BlackberryOptionsProperties() {

        mHomeUrl = ICEmobileContainer.HOME_URL;

        mApplicationURLs = new String[5];
        for (int idx = 1; idx < mApplicationURLs.length; idx ++ ) { 
            mApplicationURLs[idx] = UNUSED;
        }
        // 
        mApplicationURLs[0] = mHomeUrl;
        mApplicationURLs[1] = "http://mediacast.icemobile.org";
        mApplicationURLs[2] = "http://mobileshowcase.icemobile.org";

        mSelectedIndex = 0;
        mMode = TEXT_MODE;
        mEulaViewed = Boolean.FALSE;
//       A  mUsingEmailNotification = Boolean.FALSE; 
    }        

    //Retrieves a copy of the effective properties set from storage.
    public static BlackberryOptionsProperties fetch() { 
        synchronized (store) { 
            BlackberryOptionsProperties savedProps =
                (BlackberryOptionsProperties) store.getContents();

            return new BlackberryOptionsProperties(savedProps);
        }
    }

    //Causes the values within this instance to become the effective
    //properties for the application by saving this instance to the store.
    public void save() { 

        synchronized (store) {
            store.setContents(this);
            store.commit();
        }
    }

    /**
     * Fetch the 'Home' URL for the application. 
     * @return
     */
    public String getHomeURL() {
        return mHomeUrl;
    }

    /**
     * A call to this reflects a typed entry that should be used, 
     * and added to the list. 
     * 
     * @param url URL to act as new application HOME url. 
     */
    public void setHomeUrl(String url) { 
        mHomeUrl = url;	

        for (int idx = 0; idx < mApplicationURLs.length; idx++) {

            if (mApplicationURLs[idx].equals(url) ) { 
                break; 
            }		
        
            if (mApplicationURLs[idx].equals(UNUSED)) {
                mApplicationURLs[idx] = url; 
                mSelectedIndex = idx;
                break;
            }
        }	
    }

    /**
     * A call to this method reflects use of the drop down, and 
     * we should grab that URL
     * @param 
     */
    public void setHomeURLIndex(int idx) { 
        mHomeUrl = mApplicationURLs[idx];
        mSelectedIndex = idx;			
    }

    public int getHomeURLIndex() { 
        return mSelectedIndex; 
    }

    public String[] getApplicationURLs() { 
        return mApplicationURLs;
    }

    public void setMode(int mode) { 
        mMode = mode;
    }

    public int getMode() { 
        return mMode;
    }
    
    public boolean isEulaViewed() { 
    	return mEulaViewed.booleanValue(); 
    }
    
    public void setEulaViewed(boolean eulaViewed) { 
    	mEulaViewed  = new Boolean (eulaViewed);
    }
    
    public boolean isUsingEmailNotification() { 
        return mUsingEmailNotification.booleanValue(); 
    }
    
    public void setUsingEmailNotification(boolean usingEmail) { 
        mUsingEmailNotification = new Boolean( usingEmail );
    }
    
    public String getEmailNotification() { 
        return mEmailNotification; 
    }
    
    public void setEmailNotification( String emailNotification ) { 
        mEmailNotification = emailNotification;
    }
    
    

    public String getQuickURL() {
        return mQuickURL;
    }

    public void setQuickURL(String quickURL) {
        this.mQuickURL = quickURL;
    }

    //Canonical copy constructor.
    private BlackberryOptionsProperties(BlackberryOptionsProperties other) { 
        mHomeUrl = other.mHomeUrl;
        mApplicationURLs = new String[ other.mApplicationURLs.length];
        for (int idx = 0; idx < mApplicationURLs.length; idx ++ ) { 
            mApplicationURLs[idx] = new String ( other.mApplicationURLs[idx] ); 
        }
        mSelectedIndex = other.mSelectedIndex;
        if (other.mEulaViewed != null) { 
            mEulaViewed = new Boolean (other.mEulaViewed.booleanValue() );
        } else {  
            mEulaViewed = Boolean.FALSE; 
        }
        if (other.mUsingEmailNotification != null) { 
            mUsingEmailNotification = new Boolean (other.mUsingEmailNotification.booleanValue()); 
        } else { 
            mUsingEmailNotification = Boolean.FALSE; 
        }
        
        mMode = other.mMode;        
        if (other.mQuickURL != null) { 
            this.mQuickURL = new String( other.mQuickURL );
        }
        
        if (other.mEmailNotification != null ) { 
            mEmailNotification = new String( other.mEmailNotification );
        } 
    }
}
