/*
* Copyright 2004-2011 ICEsoft Technologies Canada Corp.
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

package org.icemobile.client.android;

public class CameraInterface implements JavascriptInterface {

    private CameraHandler handler;
    private String result;

    public CameraInterface (CameraHandler handler) {
	this.handler = handler;
    }

    public String shootPhoto(String id, String attr) {
	AttributeExtractor attributes = new AttributeExtractor(attr);
	int width = Integer.parseInt(attributes.getAttribute("maxwidth", "640"));
	int height = Integer.parseInt(attributes.getAttribute("maxheight", "480"));
	int thumbWidth = Integer.parseInt(attributes.getAttribute("thumbWidth", "64"));
	int thumbHeight = Integer.parseInt(attributes.getAttribute("thumbHeight", "64"));
	String thumbId = attributes.getAttribute("thumbId", id + "-thumb");
        return handler.shootPhoto(width, height, thumbId, 
				  thumbWidth, thumbHeight);
    }
}
