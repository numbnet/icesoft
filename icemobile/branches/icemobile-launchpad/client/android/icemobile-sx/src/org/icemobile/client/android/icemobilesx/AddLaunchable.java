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

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

public class AddLaunchable extends Activity {

    private String name;
    private String url;
    private String sxSuffix;
    private String splash;
    private String icon;
    private static final String LT = "ICEmobile-SX";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_launchable);
    }

    public void addLaunchable(View view) {
	Log.e(LT,"Adding Launchable ");
	Intent result = new Intent();
	result.putExtra("name", ((EditText)findViewById(R.id.app_name)).getText().toString());
	result.putExtra("url", ((EditText)findViewById(R.id.app_url)).getText().toString());
	result.putExtra("suffix", ((EditText)findViewById(R.id.sx_suffix)).getText().toString());
	result.putExtra("splash", ((EditText)findViewById(R.id.app_splash)).getText().toString());
	result.putExtra("icon", ((EditText)findViewById(R.id.app_icon)).getText().toString());
	this.setResult(RESULT_OK, result);
	finish();
    }
    public void cancelLaunchable(View view) {
	Log.e(LT,"Canceling Adding");
	this.setResult(RESULT_CANCELED, null);
	finish();
    }
}