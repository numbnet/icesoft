/*
* Copyright 2004-2011 ICEsoft Technologies Canada Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions an
* limitations under the License.
*/ 

package org.icemobile.client.android;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class FileLoader implements JavascriptInterface {
    private AssetManager assetManager;
    protected static final String FACES_DIR = new String("icefaces/");
    
    public FileLoader(AssetManager assetManager)  {
        this.assetManager = assetManager;
    }

    public String loadAssetFile(String fileName)  {
        try {
	    return readTextFile(assetManager.open(FACES_DIR + fileName));
        } catch (IOException e)  {
            Log.e("ICEcontainer", "exception loading file " + e);
        }
        return "";
    }

    public String readTextFile(InputStream inputStream) throws IOException  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
        } finally {
            outputStream.close();
            inputStream.close();
        }
        return outputStream.toString();
    }
}
