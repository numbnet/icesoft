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

import android.content.Intent;
import android.content.Context;
import android.app.Service;
import android.os.Binder;
import android.os.IBinder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionChangeService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private String status;
    private boolean timerRunning;
    private boolean networkUp;
    private Thread timer;
    private int delay;
    private ConnectionChangeListener listener;

    @Override
	public IBinder onBind(Intent arg0) {
	return mBinder;
    }

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
	if (listener != null) {
	    String currentStatus = intent.getStringExtra("netconn");
	    if (!currentStatus.equals(status)) {
		if (currentStatus.equals("")) {
		    if (networkUp && !timerRunning) {
			startTimer();
		    }
		} else {
		    if (timerRunning) {
			stopTimer();
		    }
		    if (!networkUp) {
			networkUp();
		    }
		}
		status = currentStatus;
	    }
	}
        return START_STICKY;
    }

    public boolean setListener(ConnectionChangeListener listener, int delay, Context context) {
	this.listener = listener;
	this.delay = delay;
	ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	if (activeNetInfo == null) {
	    status = new String("");
	    networkUp = false;
	} else {
	    status = activeNetInfo.getTypeName();
	    networkUp = true;
	}
	timerRunning = false;
	return networkUp;
    }

    private void startTimer() {
	timer = new Thread (new Runnable() {
		public void run() {
		    timerRunning = true;
		    try {
			Thread.currentThread().sleep(delay);
			timerExpired();
		    } catch (InterruptedException e) {
			timerRunning = false;
		    }
		}
	    });
	timer.start();
    }

    private void stopTimer() {
	timer.interrupt();
    }

    private void timerExpired() {
	timerRunning = false;
	if (networkUp) {
	    networkUp = false;
	    listener.networkIsDown();
	}
    }

    private void networkUp() {
	networkUp = true;
	listener.networkIsUp();
    }

    public class LocalBinder extends Binder {
	ConnectionChangeService getService() {
	    return ConnectionChangeService.this;
	}
    }
}

