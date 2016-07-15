/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobile;

import java.io.Serializable;
import org.icefaces.mobi.utils.IceOutputResource;

import javax.faces.application.Resource;
import java.io.*;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaBean implements Serializable {
    protected String type;
    protected boolean loop;
    protected String preload;
    protected boolean controls;
    protected boolean muted;
    protected String linkLabel;
    protected boolean autoPlay;
    protected String style;
    protected String styleClass;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public String getPreload() {
        return preload;
    }

    public void setPreload(String preload) {
        this.preload = preload;
    }

    public boolean isControls() {
        return controls;
    }

    public void setControls(boolean controls) {
        this.controls = controls;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }
    public static byte[] createByteArray(File resourceFile) throws IOException {
        FileInputStream fis = new FileInputStream(resourceFile);
        return createByteArray(fis);
    }

    public static byte[] createByteArray(InputStream fis) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        bos.close();
        fis.close();

        return bos.toByteArray();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
