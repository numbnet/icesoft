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

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * The extra info tag is required to define to the JSP engine what sort
 * of element is currently being iterated so it can make it accessable to
 * scriptlets within the iteration scope.
 */
public class CarouselItemTei extends TagExtraInfo {

    public CarouselItemTei() {
    }

    public VariableInfo[] getVariableInfo(TagData data) {

        VariableInfo[] returnVal = new VariableInfo[1];

        // retrieve the tag property values
        String ref = data.getAttributeString("ref");
        String type = data.getAttributeString("type");

        returnVal[0] = new VariableInfo(ref, type, true, VariableInfo.NESTED);

        return returnVal;
    }
}