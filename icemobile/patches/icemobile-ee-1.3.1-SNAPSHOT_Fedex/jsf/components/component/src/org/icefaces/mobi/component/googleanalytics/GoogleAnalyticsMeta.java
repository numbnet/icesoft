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

package org.icefaces.mobi.component.googleanalytics;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(tagName = "googleAnalytics",
    componentClass = "org.icefaces.mobi.component.googleanalytics.GoogleAnalytics",
    rendererClass = "org.icefaces.mobi.component.googleanalytics.GoogleAnalyticsRenderer",
    generatedClass = "org.icefaces.mobi.component.googleanalytics.GoogleAnalyticsBase",
    componentType = "org.icefaces.GoogleAnalytics",
    rendererType = "org.icefaces.GoogleAnalyticsRenderer",
    extendsClass = "javax.faces.component.UIPanel",
    componentFamily = "org.icefaces.GoogleAnalytics",
    tlddoc = "The googleAnalytics component will render the necessary script for using "
        + " Google Analytics. The ga account and domain may be specified with "
        + "the system properties org.icemobile.googleAnalyticsAccount and "
        + "org.icemobile.googleAnalyticsDomain or the component attributes "
        + "\"account\" and \"domain\". If the \"account\" attribute is set, "
        + "this will override any system environment variable.")
@ResourceDependencies({ 
    @ResourceDependency(library = "org.icefaces.component.util", 
            name = "component.js") })
public class GoogleAnalyticsMeta {

    @Property(tlddoc = "The Google Analytics account name. If none is provided, "
            + "the system property is used.")
    private String account;

    @Property(tlddoc = "If sub or multiple domains are being tracked, the domain should be "
            + "specified. This may also be specified with the system property "
            + "org.icemobile.googleAnalyticsDomain. Please see the Google docs at "
            + "https://developers.google.com/analytics/devguides/collection/gajs/gaTrackingSite .")
    private String domain;

}
