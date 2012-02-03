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

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionChangeReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive( Context context, Intent intent )
  {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
    intent.setComponent(new ComponentName(context, "org.icemobile.client.android.ConnectionChangeService"));

    if ( activeNetInfo != null )
    {
	intent.putExtra("netconn", activeNetInfo.getTypeName());
    } else {
	intent.putExtra("netconn", "");
    }
    context.startService(intent);
  }
}
