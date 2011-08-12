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

public class AttributeExtractor {

    private String[] attribute;

    public AttributeExtractor(String attr) {
	if (attr == null) {
	    attribute = null;
	} else {
	    attribute = attr.split("&");
	}
    }

    public String getAttribute(String attrName, String attrDefault) {
	if (attribute == null) {
	    return attrDefault;
	}
	int i=0;
	boolean found = false;
	while (i<attribute.length && !found) {
	    if (attribute[i].startsWith(attrName)) {
		found=true;
	    } else {
		i++;
	    }
	}
	if (found) return attribute[i].substring(attribute[i].indexOf('=')+1);
	return attrDefault;
    }
}