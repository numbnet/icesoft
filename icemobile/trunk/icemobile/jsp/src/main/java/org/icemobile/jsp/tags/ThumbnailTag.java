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

package org.icemobile.jsp.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.Writer;
import java.lang.String;

import org.icemobile.component.IThumbnail;
import org.icemobile.renderkit.ThumbnailCoreRenderer;

public class ThumbnailTag extends BaseSimpleTag implements IThumbnail{

    private String id;
    private String style;
    private String styleClass;
    private String baseClass;
    private String mFor;

    public static final String THUMBNAIL_CLASS = "mobi-thumb";

    public void doTag() throws IOException {
	    ThumbnailCoreRenderer renderer = new ThumbnailCoreRenderer();
        renderer.encode(this, new TagWriter(getContext()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFor() {
        return mFor;
    }

    public void setFor(String mFor) {
        this.mFor = mFor;
    }

    public String getBaseClass() {
        return styleClass;
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
    public String getClientId() {
            return id;
    }

}