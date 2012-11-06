/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icemobile.client.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.util.Log;
import android.location.Location;
import android.opengl.Matrix;
import java.util.Map;
import java.util.HashMap;

public class ARView extends View {
    private Paint mTextPaint;
    private Paint mTextPaintRed;
    HashMap<String,String> places = new HashMap();
    float[] deviceTransform = new float[16];
    Location currentLocation;

    public ARView(Context context) {
        super(context);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        // Must manually scale the desired text size to match screen density
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(0xFFFFFFFF);
        mTextPaintRed = new Paint();
        mTextPaintRed.setAntiAlias(true);
        mTextPaintRed.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaintRed.setColor(0xFFFF0000);
        setPadding(3, 3, 3, 3);
    }
    
    void setPlaces(HashMap<String,String> places)  {
        this.places = places;
    }

    void setRotation(float[] transform)  {
        this.deviceTransform = transform;
    }

    void setLocation(Location location)  {
        this.currentLocation = location;
    }

    float x,y;
    void setTouchPosition(float x, float y)  {
        this.x = x;
        this.y = y;
    }

    //increase by factor 2, shift by 200,200
    //Matrix uses column major order to make the
    //matrix unreadable in source code
    float[] adjust1 = new float[] {
          2,   0, 0, 0,
          0,   2, 0, 0,
          0,   0, 2, 0,
        200, 200, 0, 1
    };
    float[] adjust = new float[] {
          2, 0, 0, 0,
          0, 2, 0, 0,
          0, 0, 2, 0,
          0, 0, 0, 1
    };

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (String label : places.keySet())  {
            String coordstr = places.get(label);
            if (null == coordstr)  {
                continue;
            }
            String [] coordparts = coordstr.split(",");
            if ((null != currentLocation) && (coordparts.length >= 2)) {
                float[] labelLoc = new float[2];
                try {
                    labelLoc[0] = Float.parseFloat(coordparts[0]);
                    labelLoc[1] = Float.parseFloat(coordparts[1]);
                } catch (Exception e)  {
                    Log.d("ARView ", "malformed AR item " + label + ":" +
                            coordstr);
                }

                float[] coord = new float[4];
                coord[0] = 1000 * (labelLoc[0] 
                        - (float)currentLocation.getLatitude()); 
                coord[1] = 1000 * (labelLoc[1] 
                        - (float)currentLocation.getLongitude());
                coord[2] = 0f;
                coord[3] = 1f;

                float[] mat = new float[16];
//                Matrix.invertM(mat, 0, deviceTransform, 0);
                mat = deviceTransform;
                float[] v1 = new float[]{0, 0, 0, 1};
                Matrix.multiplyMV(v1, 0, mat, 0, coord, 0);
                float[] v = new float[]{0, 0, 0, 1};
                Matrix.multiplyMV(v, 0, adjust, 0, v1, 0);

                //project x-z and discard y=x as behind us
                canvas.save();
                canvas.rotate(270, v[0], v[1]);
                if ( (v[1] - v[0]) > 0)  {
                    canvas.drawText(label, v[0] - 300, v[1] + 300, mTextPaint);
                } else {
                    canvas.drawText(label, v[0] - 300, v[1] + 300, mTextPaintRed);
                }
Log.d("ARView ", "rotated " + label + v[0] + "," + v[1]);
                canvas.restore();
            }
        }
        canvas.save();
        canvas.rotate(270, x, y);
        canvas.drawText("Touch", x, y, mTextPaint);
        canvas.restore();
    }

}
