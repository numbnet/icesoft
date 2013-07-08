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
//import java.util.Hashtable;
import java.util.Vector;
import java.util.Arrays;
import java.util.Enumeration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.io.IOException;
import android.graphics.Bitmap;
import android.util.Log;

//import android.graphics.BitmapFactory;

import org.icemobile.client.android.util.UtilInterface;
import org.icemobile.client.android.util.FileLoader;

public class LaunchManager {

    private Vector<Launchable> launchmarks;
    private UtilInterface utilInterface;
    private FileLoader fileLoader;
    private static final String LAUNCHABLE_FILE = "launchable.log";

    public LaunchManager(UtilInterface utilInterface, FileLoader fileLoader) {
	this.utilInterface = utilInterface;
	this.fileLoader = fileLoader;
	launchmarks = new Vector();
	load();
    }

    public void add(Launchable app) {
	launchmarks.add(app);
    }

    /*    public void remove(URL appURL) {
	launchmarks.remove(appURL);
	}*/
    
    public Launchable get(int pos) {
	return (Launchable)(launchmarks.elementAt(pos));
    }
    
    public Launchable find(String url) {
	Enumeration<Launchable> i = launchmarks.elements();
	while (i.hasMoreElements()) {
	    Launchable app = i.nextElement();
	    if (url.startsWith(app.getAppUrl().toString())) return app;
	}
	return null;
    }
    
    public int size() {
	return launchmarks.size();
    }
    
    public void load() {
	File launchableFile = new File(utilInterface.getTempPath(), LAUNCHABLE_FILE);
	String list=null;
	try {
	    if (launchableFile.exists()) {
		list = fileLoader.readTextFile(new FileInputStream(launchableFile));
	    } else {
		list = fileLoader.loadAssetFile(LAUNCHABLE_FILE);
	    }
	} catch (IOException e) {
	}
	if (list != null && list.length() > 0) {
	    Vector<String> launchables = new Vector(Arrays.asList(list.split("\\|")));
	    Enumeration<String> e = launchables.elements();
	    while (e.hasMoreElements()) {
		Launchable launchable = new Launchable(e.nextElement());
		launchmarks.add(launchable);
	    }
	}
    }

    public void save() {
	FileWriter out;
	try {

	    File launchableFile = new File(utilInterface.getTempPath(), "launchable.log");
	    out = new FileWriter(launchableFile);
	    for (Enumeration i = launchmarks.elements(); i.hasMoreElements(); ) {
		out.write(((Launchable)i.nextElement()).toString());
		out.write("|");
	    }
	    out.close();
	} catch (IOException e) {
	}
    }
}