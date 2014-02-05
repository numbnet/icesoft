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

package org.icemobile.client.android.arview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Matrix;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;

import org.icemobile.client.android.util.AttributeExtractor;

public class ARViewActivity extends Activity implements SensorEventListener, 
    LocationListener, OnTouchListener {

    private Preview mPreview;
    Camera mCamera;
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private Sensor mGravitySensor;
    private Sensor mCompassSensor;
    private LocationManager mLocationManager;
    int numberOfCameras;
    int cameraCurrentlyLocked;
    ARView arView;
    Location currentLocation = null;
    private float[] gravity  = new float[3];
    private float[] compass  = new float[3];    
    private final float[] mRotationMatrix = new float[16];
    private float azimuth = 0.0f;
    HashMap<String,String> places = new HashMap();
    private String urlBase = null;

    // The first rear facing camera
    int defaultCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("ARViewActivity", "created; registering for events ");

        Bundle extras = getIntent().getExtras();
        String attr = "";
        if (null != extras) {
            attr = extras.getString("attributes");
        }
        Log.e("ARViewActivity", "places: " + attr);

        Map<String,String> attributes = 
                new AttributeExtractor(attr).getAttributes();
        for (String name : attributes.keySet())  {
            if ("id".equals(name))  {
                continue;
            }
            if ("ub".equals(name))  {
                urlBase = URLDecoder.decode(attributes.get(name));
                continue;
            }
            if ("v".equals(name))  {
                //Detect AR Markers here
                //not currently implemented
                Log.d("ARViewActivity", "Ignore viewer " + attributes.get(name));
                continue;
            }
            places.put(name, attributes.get(name));
        }
    
        for (String label : places.keySet())  {
            Log.d("ARViewActivity", "Display " + label + " at " + places.get(label));
        }

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);
        mGravitySensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        mCompassSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD);
        mRotationMatrix[ 0] = 1;
        mRotationMatrix[ 4] = 1;
        mRotationMatrix[ 8] = 1;
        mRotationMatrix[12] = 1;

        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new Preview(this);
        setContentView(mPreview);
        mPreview.setOnTouchListener(this);
        
        arView = new ARView(this);
        arView.setPlaces(places);
        arView.setUrlBase(urlBase);
        arView.setRotation(mRotationMatrix);
        LayoutParams arLayout = new LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        addContentView(arView, arLayout);

        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    defaultCameraId = i;
                }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        cameraCurrentlyLocked = defaultCameraId;
        mPreview.setCamera(mCamera);
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
        mSensorManager.registerListener(this, mGravitySensor, 10000);
        mSensorManager.registerListener(this, mCompassSensor, 10000);
        mLocationManager.requestSingleUpdate(
                mLocationManager.NETWORK_PROVIDER, this, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        mSensorManager.unregisterListener(this);
    }

    public float[] smoothClone(float[] oldVec, float[] newVec)  {
        float[] result = new float[oldVec.length];
        for (int i=0; i <oldVec.length; i++)  {
            result[i] = (oldVec[i] * 9 + newVec[i]) / 10;
        }
        return result;
    }

    public void onSensorChanged(SensorEvent event) {
        boolean viewChanged = false;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = smoothClone(gravity, event.values);
            viewChanged = true;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            compass = smoothClone(compass, event.values);
            viewChanged = true;
        }
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector.
//            SensorManager.getRotationMatrixFromVector(
//                    mRotationMatrix , event.values);
//            arView.setRotation(mRotationMatrix);
//            arView.postInvalidate();

//        Log.d("ARViewActivity", "sensor event " + 
//                rotationToString(mRotationMatrix) );
        }
        if (viewChanged)  {
            float[] rotation = new float[16];
            float[] inclination = new float[16];
            boolean gotRot = SensorManager.getRotationMatrix(
                    rotation, inclination, gravity, compass);

            float[] apr = new float[16]; //azimuth/pitch/roll
            SensorManager.getOrientation(rotation, apr);
            if (gotRot)  {
                //average out some noise
                azimuth = (azimuth * 9 + apr[0]) / 10;
                arView.setCompass(azimuth);
                arView.setRotation(rotation);
                arView.postInvalidate();
            }
        }

        if (null != arView.getSelected())  {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected" , arView.getSelected());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onLocationChanged(Location location) {
        Log.d("ARViewActivity", "location changed " + location);
        this.currentLocation = location;
        arView.setLocation(location);
    }

	public boolean onTouch(View view, MotionEvent me) {		
        Log.d("ARViewActivity", "touch event " + me);
        arView.setTouchPosition(me.getX(), me.getY());
        arView.postInvalidate();
		return super.onTouchEvent(me);
	};

    public static String rotationToString(float[] matrix)  {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++)  {
            for (int j = 0; j < 4; j++)  {
                result.append(matrix[4 * i + j] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }

}

// ----------------------------------------------------------------------

/**
 * A simple wrapper around a Camera and a SurfaceView that renders a centered preview of the Camera
 * to the surface. We need to center the SurfaceView because not all devices have cameras that
 * support preview sizes at the same aspect ratio as the device's display.
 */
class Preview extends ViewGroup implements SurfaceHolder.Callback {
    private final String TAG = "ARView Preview";

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Size mPreviewSize;
    List<Size> mSupportedPreviewSizes;
    Camera mCamera;

    Preview(Context context) {
        super(context);

        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            requestLayout();
        }
    }

    public void switchCamera(Camera camera) {
       setCamera(camera);
       try {
           camera.setPreviewDisplay(mHolder);
       } catch (IOException exception) {
           Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
       }
       Camera.Parameters parameters = camera.getParameters();
       parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
       requestLayout();

       camera.setParameters(parameters);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        if (null == mCamera)  {
            Log.e(TAG, "camera not available, proceeding with black AR view");
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();

        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

}
