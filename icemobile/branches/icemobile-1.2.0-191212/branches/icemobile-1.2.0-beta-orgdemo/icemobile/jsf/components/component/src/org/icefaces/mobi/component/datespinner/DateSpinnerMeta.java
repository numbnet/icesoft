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

package org.icefaces.mobi.component.datespinner;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "dateSpinner",
        componentClass = "org.icefaces.mobi.component.datespinner.DateSpinner",
        generatedClass = "org.icefaces.mobi.component.datespinner.DateSpinnerBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.mobi.component.datespinner.DateSpinnerRenderer",
        componentFamily = "org.icefaces.component.DateSpinner",
        componentType = "org.icefaces.component.DateSpinner",
        rendererType = "org.icefaces.component.DateSpinnerRenderer",
        tlddoc = "DateSpinner is an input component to provide a date for mobile components. ")

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name = "change", javadoc = "...", tlddoc = "...", defaultRender = "@this", defaultExecute = "@all")
}, defaultEvent = "change")
public class DateSpinnerMeta extends UIInputMeta {

    @Property(defaultValue = "yyyy-MM-dd", tlddoc = "DateFormat pattern for localization")
    private String pattern;

    @Property(defaultValue = "1980", tlddoc = "yearStart is the first year to appear in the dateScroller")
    private int yearStart;

    @Property(defaultValue = "2020", tlddoc = "yearEbd is the last year to appear in the dateScroller")
    private int yearEnd;

    @Property(defaultValue = "15", tlddoc = "width of the input text field in characters, where the value of the date will reside")
    private String size;

    @Property(tlddoc = "Locale to be used for labels and conversion.")
    private Object locale;

    @Property(tlddoc = "String or a java.util.TimeZone instance to specify the timezone used for date " +
            "conversion, defaults to TimeZone.getDefault()")
    private Object timeZone;

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(tlddoc = "If true then this date time entry will be disabled and can not be entered.")
    private boolean disabled;

    @Property(tlddoc = "If true then this date time entry will be read-only and can not be entered.")
    private boolean readonly;

    @Property(tlddoc = "If true then this date time entry will submit itself.")
    private boolean singleSubmit;

    @Property(defaultValue = "false", tlddoc = "if useNative is true then iOS5, iOS6, and BlackBerry devices will show the builtin date selection popup" +
            " if false then the mobi datespinner component is shown with native device styling.  This can be expanded if other device operating" +
            " systems provide support for html5 input type of date.")
    private boolean useNative;

}
