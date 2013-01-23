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

package org.icemobile.client.blackberry.script.audio;

import java.util.Hashtable;

import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.ui.UiApplication;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

public class AudioRecorderLauncher extends ScriptableFunction {

	private ContainerController mController;
    // Id of the audio clip field
	private String mFieldId;
	private int mMaxDuration;
	
	private AudioRecorder mAudioScreen;
    
    
    public AudioRecorderLauncher(ContainerController controller ) {
    	mController = controller;       
    	mAudioScreen = new AudioRecorder(controller);
    }


    public Object invoke( Object thiz, Object[] args) {

    	Hashtable params = new Hashtable();
    	if (args.length == 2) { 
    		mFieldId = (String) args[0];

    		// 'undefined' object indicates arguments not being passed 
    		if (! (args[1] instanceof Object)) { 
    			params = UploadUtilities.getNameValuePairHash((String)args[1], "=", "&");
    		} 

    	} else { 
    		Logger.ERROR("ice.audio - wrong number of arguments");
    		return Boolean.FALSE; 
    	}     

    	NameValuePair temp; 

    	temp = (NameValuePair) params.get("maxtime");
    	if (temp != null) {
    		mMaxDuration = Integer.parseInt( temp.getValue() ); 
    	} else { 
    		Logger.DEBUG("ice.audio indefinite length recording");
    	}
    	
    	try { 
    		mAudioScreen.setAudioParameters( mFieldId, mMaxDuration);  	
    		UiApplication.getUiApplication().pushScreen(mAudioScreen);
    	} catch (Throwable t ) { 
    		Logger.DEBUG("ice.audio - exception launching screen: " + t); 
    	}
    	
    	return Boolean.TRUE;
    }	
}
