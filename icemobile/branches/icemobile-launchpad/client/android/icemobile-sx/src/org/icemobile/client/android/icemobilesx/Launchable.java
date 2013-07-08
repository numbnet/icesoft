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

package org.icemobile.client.android.icemobilesx;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Launchable {

    private String name;
    private URL appUrl;
    private URL regUrl;
    private URL splashUrl;
    private URL iconUrl;

    private Bitmap splash;
    private Bitmap icon;

    public Launchable(String name, String appUrl, String regSuffix, String splashUrl, String iconUrl) throws MalformedURLException {
	this.name = name;
	this.appUrl = new URL(fixUrl(appUrl));
	regUrl = new URL(this.appUrl + "/" + regSuffix);
	this.splashUrl = new URL(fixUrl(splashUrl));
	this.iconUrl = new URL(fixUrl(iconUrl));
	try {
	    splash = BitmapFactory.decodeStream(new URL(splashUrl).openStream());
	} catch (IOException e) {
	}
	try {
	    icon = BitmapFactory.decodeStream(new URL(iconUrl).openStream());
	} catch (IOException e) {
	}
    }

    public Launchable(String launchString) {
	Log.e("Launchable", "launchString " + launchString);
	String[] params = launchString.split(",", -1);
	for (int i=0; i< params.length; i++) {
	    Log.e("Launchable", "param[" + i +"] " + params[i]);
	}
	name = params[0];
	try {
	    appUrl = new URL(params[1]);
	    regUrl = new URL(params[2]);
	    splashUrl = new URL(params[3]);
	    iconUrl = new URL(params[4]);
	    getBitmaps();
	} catch (Exception e) {
	}
    }

    private void getBitmaps() throws IOException, MalformedURLException {
	if (splashUrl.toString().length() > 0) {
	    splash = BitmapFactory.decodeStream(splashUrl.openStream());
	} else {
	    splash = null;
	}
	if (iconUrl.toString().length() > 0) {
	    icon = BitmapFactory.decodeStream(iconUrl.openStream());
	} else {
	    icon = null;
	}
    }

    public String getName() {
	return name;
    }
    public URL getAppUrl() {
	return appUrl;
    }
    public URL getRegUrl() {
	return regUrl;
    }
    public Bitmap getSplash() {
	return splash;
    }
    public Bitmap getIcon() {
	return icon;
    }

    public String toString() {
	return name  + "," + appUrl.toString() + "," + regUrl.toString() + "," + splashUrl.toString() + "," + iconUrl.toString();
    }

    private String fixUrl(String url) {
	if (url.startsWith("http://")) {
	    return url;
	}
	return "http://"+url;
    }
}