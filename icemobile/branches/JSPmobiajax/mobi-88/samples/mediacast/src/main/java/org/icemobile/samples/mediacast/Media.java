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

package org.icemobile.samples.mediacast;

import org.icefaces.component.utils.IceOutputResource;

import javax.faces.application.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Photo represents one image upload.  Each message will normally have
 * three version, small, medium and large.
 */
public class Media {

    private static final Logger logger =
            Logger.getLogger(Media.class.toString());

    private Resource data;
    private final Object dataLock = new Object();
    private int width;
    private int height;

    public Media(byte[] data, int width, int height) {

        this.width = width;
        this.height = height;

        ByteArrayInputStream fis = new ByteArrayInputStream(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[4096];
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }
            synchronized (dataLock){
                this.data = new IceOutputResource(
                        UUID.randomUUID().toString(),
                        bos.toByteArray(), "image/png");
            }
            bos.close();
            fis.close();
        } catch (Throwable e) {
            logger.warning("Error creating image resource");
        }
    }

    /**
     * dispose the underlying data array, there is a simple lock to avoid
     * deleting a file that is just being created.  Should not occur under
     * normal application load.
     */
    public void dispose(){
        if (data != null){
            synchronized (dataLock){
                data = null;
            }
        }
    }

    public Resource getData() {
        synchronized (dataLock){
            return data;
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
