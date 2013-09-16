package org.icemobile.client.android.location;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.app.Application;
import android.os.Binder;
import android.os.IBinder;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.util.Log;
import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.location.LocationManager;
//import android.location.LocationProvider;
import android.location.LocationListener;
import android.location.Location;
import android.location.Criteria;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class LocationService extends Service implements Runnable {

    private static String LT = "ICEnotify";
    private final IBinder mBinder = new LocalBinder();
    private LocationManager locationManager;
    private String server;
    private String command;
    private String accuracy;
    private String deltaTime;
    private String deltaDistance;
    private LocationListener locationListener;
    private String currentProvider;
    private Location locationToSend;

    @Override
    public void onCreate() {
	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
		    // Called when a new location is found by the network location provider.
		    sendNewLocation(location);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {}

		public void onProviderEnabled(String provider) {}

		public void onProviderDisabled(String provider) {}
	    };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

	// Stop any previous location fetching;
	if (locationListener != null) {
	    locationManager.removeUpdates(locationListener);
	}

	if (intent == null || intent.getExtras() == null) {
	    Log.e(LT,"Location Services started with invalid intent.");
	    return START_STICKY;
	}

	// Get command parameters;
	server = intent.getExtras().getString("s");
	command = intent.getExtras().getString("c");
	accuracy = intent.getExtras().getString("a");
	deltaTime = intent.getExtras().getString("t");
	deltaDistance = intent.getExtras().getString("d");

	// Get appropriate location provider;
	Criteria criteria = new Criteria();

	if (accuracy != null && accuracy.equals("high")) {
	    Log.d(LT,"High accuracy");
	    criteria.setAccuracy( Criteria.ACCURACY_FINE );
	    //currentProvider = LocationManager.GPS_PROVIDER;
	} else {
	    Log.d(LT,"Low accuracy");
	    criteria.setAccuracy( Criteria.ACCURACY_COARSE );
	    //currentProvider = LocationManager.NETWORK_PROVIDER;
	}
	currentProvider = locationManager.getBestProvider( criteria, true );
	if (currentProvider == null) {
	    Log.d(LT,"Could not get required LocationProvider");
	    return START_STICKY;
	} else {
	    Log.d(LT,"LocationProvider=" + currentProvider);
	}
	
	Log.d(LT,"Command=" + command);
	if (command.equals("last")) {
	    Log.d(LT,"Requesting last from " + accuracy);
	    sendNewLocation(locationManager.getLastKnownLocation(currentProvider));
	} else if (command.equals("current")) {
	    Log.d(LT,"Requesting current from " + accuracy);
	    locationManager.requestSingleUpdate(currentProvider, locationListener, null);
	} else if (command.equals("continuous")) {
	    Log.d(LT,"Requesting continuous from " + accuracy + ": dtime=" + deltaTime + ", ddistance=" + deltaDistance);
	    long dTime = Long.valueOf(deltaTime).longValue();
	    float dDistance = Float.valueOf(deltaDistance).floatValue();
	    locationManager.requestLocationUpdates(currentProvider, dTime, dDistance, locationListener);
	} else if (command.equals("stop")) {
	    // Already stopped;
	} else {
	    Log.e(LT, "Invalid location request command: " + command);
	}
        return START_STICKY;
    }

    public IBinder onBind(Intent arg0) {
	return mBinder;
	}

    public class LocalBinder extends Binder {
	LocationService getService() {
	    return LocationService.this;
	}
    } 

    private void sendNewLocation(Location location) {
	locationToSend = location;
	if (locationToSend == null) {
	    Log.e(LT,"Null location returned.");
	    return;
	}
	Thread thread = new Thread(this);
	thread.start();
    }

    public void run() {
	JSONObject locationObject = getGeoJSON(locationToSend);
	if (locationObject != null) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(server);
	    httppost.setHeader("Content-Type",
			       "application/json;charset=UTF-8");
	    try {
		httppost.setEntity(new StringEntity(locationObject.toString(), "UTF-8"));
	    } catch (UnsupportedEncodingException uee) {
		Log.e(LT, "Error encoding geoJSON data: " + uee.toString());
	    }

	    // Execute HTTP Post Request
	    try {
		Log.d(LT, "Sending Location Data:" + locationObject.toString());
		HttpResponse response = httpclient.execute(httppost);
	    } catch (IOException ioe) {
		Log.e(LT, "Error sending geoJSON data: " + ioe.toString());
	    }
	}
    }

    private JSONObject getGeoJSON(Location loc) {

	try {
	JSONObject geometry = new JSONObject();
	geometry.put("type","Point");
	JSONArray coordinates = new JSONArray();
	coordinates.put(loc.getLongitude());
	coordinates.put(loc.getLatitude());
	if (loc.hasAltitude()) {
	    coordinates.put(loc.getAltitude());
	}
	geometry.put("coordinates", coordinates);

	JSONObject properties = new JSONObject();
	properties.put("time", loc.getTime());
	if (loc.hasBearing()) {
	    properties.put("bearing", loc.getBearing());
	}
	if (loc.hasAccuracy()) {
	    properties.put("accuracy", loc.getAccuracy());
	}
	if (loc.hasSpeed()) {
	    properties.put("speed", loc.getSpeed());
	}

	JSONObject returnVal = new JSONObject();
	returnVal.put("type", "Feature");
	returnVal.put("geometry", geometry);
	returnVal.put("properties", properties);
	return returnVal;
	} catch (JSONException e) {
	    Log.e(LT,"Error creating geoJSON data: " + e.toString());
	}
	return null;
    }
}
