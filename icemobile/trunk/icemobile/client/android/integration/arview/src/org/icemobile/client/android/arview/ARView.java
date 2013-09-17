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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.View;
import android.util.Log;
import android.location.Location;
import android.opengl.Matrix;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import org.icemobile.client.android.util.UtilInterface;

public class ARView extends View {
    private Paint mTextPaint;
    private Paint mTextPaintRed;
    HashMap<String,String> places = new HashMap();
    HashMap<String,Bitmap> icons = null;
    float[] deviceTransform = new float[16];
    float compass = 0;
    Location currentLocation;
    String urlBase = null;
    String selected = null;

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

    void setUrlBase(String url)  {
        this.urlBase = url;
    }

    void setCompass(float radians)  {
        this.compass = radians;
    }

    void setLocation(Location location)  {
        this.currentLocation = location;
    }

    float x,y;
    void setTouchPosition(float x, float y)  {
        this.x = x;
        this.y = y;
    }

    public String getSelected()  {
        return selected;
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

    public Bitmap getImage(String url)  {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(
                    UtilInterface.getContentStream(url) );
        } catch (Exception e)  {
            e.printStackTrace();
        }
        return bitmap;
    }

    public HashMap<String,Bitmap> getIcons()  {
        try {
            if (null == icons)  {
                icons = new HashMap();
                for (String label : places.keySet())  {
                    String coordstr = places.get(label);
                    if (null == coordstr)  {
                        continue;
                    }
                    String [] coordparts = coordstr.split(",");
                    
                    if (coordparts.length >= 4)  {
                        String iconPath = coordparts[4];
                        if (null != urlBase)  {
                            iconPath = urlBase + iconPath;
                        }
                        Bitmap icon = getImage(iconPath);
                        icons.put(label, icon);
                    }
                }
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        return icons;
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xCenter = canvas.getWidth() / 2;
        int yCenter = canvas.getHeight() / 2;

        HashMap<String,Bitmap> iconBitmaps = getIcons();
        selected = null;

        for (String label : places.keySet())  {
            String coordstr = places.get(label);
            if (null == coordstr)  {
                continue;
            }
            String [] coordparts = coordstr.split(",");
            
            if ((null != currentLocation) && (coordparts.length >= 2)) {
                String iconPath = null;
                if (coordparts.length >= 4)  {
                    iconPath = coordparts[4];
                }
                float[] labelLoc = new float[2];
                try {
                    labelLoc[0] = Float.parseFloat(coordparts[0]);
                    labelLoc[1] = Float.parseFloat(coordparts[1]);
                } catch (Exception e)  {
                    Log.d("ARView ", "malformed AR item " + label + ":" +
                            coordstr);
                }

                float[] virt = new float[4];
                virt[0] = 4000 * (labelLoc[0]
                        - (float)currentLocation.getLatitude()); 
                virt[1] = 4000 * (labelLoc[1]
                        - (float)currentLocation.getLongitude());
                virt[2] = 0f;
                virt[3] = 0f;

                float[] coord = scale(normalize(virt), 500);

                float[] v = new float[]{0, 0, 0, 1};
                Matrix.multiplyMV(v, 0, deviceTransform, 0, coord, 0);

                //roation -90 around a,b relocates origin to (a+b,-a+b)
                float[] touchPoint = new float[]{
                        xCenter + yCenter - y, x - xCenter + yCenter };

                canvas.save();
                canvas.rotate(270, xCenter, yCenter);

                if ( v[2] > 0)  {
                    if (null != iconBitmaps)  {
                        Bitmap myIcon = iconBitmaps.get(label);
                        if (null != myIcon)  {
                            canvas.drawBitmap(myIcon, v[0] + xCenter, v[1] + yCenter,
                                    mTextPaint);
                        }
                    }
                    
                    canvas.drawText(label, v[0] + xCenter, v[1] + yCenter, mTextPaint);

                    float touchDistance = vecLength(new float[]{
                            touchPoint[0] - (v[0] + xCenter),
                            touchPoint[1] - (v[1] + yCenter) });
                    if (touchDistance < 100.0f)  {
                        selected = label;
                    }
                } else {
//                    canvas.drawText(label, v[0] + xCenter, v[1] + yCenter, mTextPaintRed);
                }
//Log.d("ARView ", "drawing " + label + " " + v[0] + "," + v[1]);
                canvas.restore();
            }
        }

    }

    private float[] scale(float[] vec, float factor)  {
        float[] result = new float[vec.length];
        for (int i = 0; i < vec.length; i++)  {
            result[i] = vec[i] * factor;
        }
        return result;
    }

    private float vecLength(float[] vec)  {
        float displacement = 0;
        float length = 1.0f;
        for (int i = 0; i < vec.length; i++)  {
            displacement += vec[i] * vec[i];
        }
        length = (float) Math.sqrt(displacement);
        return length;
    }

    private float[] normalize(float[] vec)  {
        float[] result = new float[vec.length];
        float length = vecLength(vec);
        if (0.0 == length)  {
            length = 1.0f;
        }
        result = scale(vec, 1.0f / length);
        return result;
    }
}
