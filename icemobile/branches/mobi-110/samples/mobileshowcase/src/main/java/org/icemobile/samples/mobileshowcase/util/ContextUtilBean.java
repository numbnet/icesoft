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

package org.icemobile.samples.mobileshowcase.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * It's used to generate the iframe markup for the Description and Source tab panels.
 * It adds the context path of the application dynamically in front of the src attribute
 * to ensure that the resource can be found when running in both portlet and plain servlet
 * environments.
 *
 * @since 1.6
 */
public class ContextUtilBean implements Serializable {

    private static final String IFRAME_PREFIX = "<iframe src=\"";
    private static final String IFRAME_SUFFIX = "\"class=\"includeIframe\" width=\"100%\"></iframe>";

    private static final String SOURCE_CODE_ULR = "/sourcecodeStream.html?path=";


    public String getContextPath() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        return ec.getRequestContextPath();
    }

    private static String getContextPathStatic() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        return ec.getRequestContextPath();
    }

    /**
     * Generates iFrame markup with the specified source as the
     * url to the iFrame content. Does not modify the
     * source path
     *
     * @param source path to some web resource
     * @return iFrame markup to load specified resource.
     */
    public static String generateIFrame(String source) {
        StringBuilder markup = new StringBuilder();
        markup.append(IFRAME_PREFIX);
        markup.append(source);
        markup.append(IFRAME_SUFFIX);
        return markup.toString();
    }

    /**
     * Generates iFrame markup with the specified source as the url to the iFrame content.
     * Modifies the source path by prepending the context path.
     *
     * @param source path to some web resource
     * @return iFrame markup to load specified resource.
     */
    public static String generateIFrameWithContextPath(String source) {
        return generateIFrame(getContextPathStatic() + source);
    }

    /**
     * Generates iFrame markup with the specified source as the url to the iFrame content.
     * Modifies the source path by prepending the source code loading servlet path.
     *
     * @param source path to some web resource
     * @return iFrame markup to load specified resource.
     */
    public static String generateSourceCodeIFrame(String source) {
        return generateIFrameWithContextPath(SOURCE_CODE_ULR + source);
    }

}
