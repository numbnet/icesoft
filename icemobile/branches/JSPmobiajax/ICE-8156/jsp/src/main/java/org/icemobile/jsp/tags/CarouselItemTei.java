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