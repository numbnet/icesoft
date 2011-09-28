/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
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

package org.icefaces.component.camera;


import java.util.Map;
import java.util.logging.Logger;

public class Camera extends CameraBase {
    private static Logger logger = Logger.getLogger(Camera.class.getName());
    public Camera() {
        super();
    }

    /*if the image properties have to be gotten from the image map to encode*/
    public Object getPropertyFromMap(Map<String, Object> cameraMap, String key) {
        if (cameraMap.containsKey(key)) {
            return cameraMap.get(key);
        } else return null;
    }

    private boolean containsKey(Map<String, Object> cameraMap, String key) {
        if (cameraMap.containsKey(key)) return true;
        else return false;
    }



}
