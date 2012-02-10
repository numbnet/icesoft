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
    
//    // Select photos from gallery over using camera
//    private boolean mUsePhotoGallery; 
//    
//    // select videos from gallery over using camera 
//    private boolean mUseVideoGallery;
    
    // 
    private String mEmailNotification= "";
    
    // invisible flag indicating whether the eula has been viewed. 
    private Boolean mEulaViewed; 
    

    // Current mode of choosing mainApplication URL, see below 
    private int mMode; 

    // TextField or dropdown list 
    public static final int TEXT_MODE = 1; 
    public static final int DROP_MODE = 2; 

    private final String UNUSED = "- unused -";

    // type a string and right click -> Convert String to Long 
    // When this value is changed, reference to previous persisted 
    // values is lost 
    private static final long PERSISTENCE_ID = 0x34332c1a19646ecfL;

    //Persistent object wrapping the effective properties instance
    private static PersistentObject store;

    //Ensure that an effective properties set exists on startup.
    static {
        store = PersistentStore.getPersistentObject(PERSISTENCE_ID);
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
        mApplicationURLs[1] = "http://labs.icesoft.com:8080/mobileshowcase";

        mSelectedIndex = 0;
        mMode = TEXT_MODE;
        mEulaViewed = Boolean.FALSE;
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
    
//    public boolean isUsePhotoGallery () { 
//        return mUsePhotoGallery; 
//    }
//    
//    public void setUsePhotoGallery(boolean usePhotoGallery) { 
//        mUsePhotoGallery = usePhotoGallery;
//    }
//    
//    public boolean isUseVideoGallery() { 
//        return mUseVideoGallery;
//    }
//    
//    public void setUseVideoGallery(boolean useVideoGallery) { 
//        mUseVideoGallery = useVideoGallery; 
//    }
    
    public String getEmailNotification() { 
        return mEmailNotification; 
    }
    
    public void setEmailNotification( String emailNotification ) { 
        mEmailNotification = emailNotification;
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
        mMode = other.mMode;
//        mUsePhotoGallery = other.mUsePhotoGallery; 
//        mUseVideoGallery = other.mUseVideoGallery; 
        if (other.mEmailNotification != null ) { 
            mEmailNotification = new String( other.mEmailNotification );
        }
    }
}
