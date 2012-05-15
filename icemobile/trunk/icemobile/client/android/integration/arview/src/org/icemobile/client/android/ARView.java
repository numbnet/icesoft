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
import java.util.Map;
import java.util.HashMap;

public class ARView extends View {
    private Paint mTextPaint;
    HashMap<String,String> places = new HashMap();
    float[] deviceTransform = new float[16];

    public ARView(Context context) {
        super(context);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        // Must manually scale the desired text size to match screen density
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(0xFFFFFFFF);
        setPadding(3, 3, 3, 3);
    }
    
    void setPlaces(HashMap<String,String> places)  {
        this.places = places;
    }

    void setRotation(float[] transform)  {
        this.deviceTransform = transform;
    }

    float x,y;
    void setTouchPosition(float x, float y)  {
        this.x = x;
        this.y = y;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (String label : places.keySet())  {
            String coordstr = places.get(label);
            String [] coordparts = coordstr.split(",");
            float[] pos = new float[] 
                    {Float.parseFloat(coordparts[0]),
                     Float.parseFloat(coordparts[1])};
            canvas.save();
            canvas.rotate(270, Math.abs(pos[0]), Math.abs(pos[1]));
            canvas.drawText(label, Math.abs(pos[0]), Math.abs(pos[1]), mTextPaint);
            canvas.restore();
        }
        canvas.save();
        canvas.rotate(270, x, y);
        canvas.drawText("Touch", x, y, mTextPaint);
        canvas.restore();
        canvas.drawText("Padded", getPaddingLeft(), getPaddingTop(), mTextPaint);
        canvas.save();
        canvas.rotate(270, 100, 300);
        canvas.drawText(ARViewActivity.rotationToString(deviceTransform), 100, 300, mTextPaint);
        canvas.restore();
    }

}
