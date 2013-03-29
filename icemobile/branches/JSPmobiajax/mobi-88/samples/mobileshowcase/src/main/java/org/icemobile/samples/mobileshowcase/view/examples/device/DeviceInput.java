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

package org.icemobile.samples.mobileshowcase.view.examples.device;

import org.icefaces.component.utils.IceOutputResource;

import javax.faces.application.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Base class for all device input beans.  The class aids in the common
 * task of putting an input source file into a Resource object.
 * <p/>
 * {@link org.icemobile.samples.mobileshowcase.view.examples.device.camcorder.CamcorderBean}
 * {@link org.icemobile.samples.mobileshowcase.view.examples.device.microphone.MicrophoneBean}
 * {@link org.icemobile.samples.mobileshowcase.view.examples.device.camera.CameraBean}
 */
public class DeviceInput {

    private static final Logger logger =
            Logger.getLogger(DeviceInput.class.toString());

    // copy the bytes into the resource object.
    public static Resource createResourceObject(File resourceFile,
                                                String resourceName,
                                                String contentType) throws IOException {
        Resource outputResource = null;
        FileInputStream fis = new FileInputStream(resourceFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }
        outputResource = new IceOutputResource(resourceName,
                bos.toByteArray(), contentType);
        bos.close();
        fis.close();

        return outputResource;
    }

    public static byte[] createByteArray(File resourceFile) throws IOException {
        Resource outputResource = null;
        FileInputStream fis = new FileInputStream(resourceFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        bos.close();
        fis.close();

        return bos.toByteArray();
    }
}
