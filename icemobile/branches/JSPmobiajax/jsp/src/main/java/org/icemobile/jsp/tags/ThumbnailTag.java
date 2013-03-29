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

package org.icemobile.jsp.tags;

import java.io.IOException;

import org.icemobile.component.IThumbnail;
import org.icemobile.renderkit.ThumbnailCoreRenderer;

public class ThumbnailTag extends BaseSimpleTag implements IThumbnail{

    private String mFor;

    public void doTag() throws IOException {
	    ThumbnailCoreRenderer renderer = new ThumbnailCoreRenderer();
        renderer.encode(this, new TagWriter(getContext()));
    }
    public String getFor() {
        return mFor;
    }

    public void setFor(String mFor) {
        this.mFor = mFor;
    }

    public String getBaseClass() {
        return IThumbnail.CSS_CLASS;
    }

    public void setBaseClass(String baseClass) {
        this.styleClass = baseClass;
    }
    public void setMFor(String mFor){
        this.mFor = mFor;
    }
    public String getMFor(){
        return this.mFor;
    }
    public void release(){
        super.release();
        this.mFor = null;
    }

}