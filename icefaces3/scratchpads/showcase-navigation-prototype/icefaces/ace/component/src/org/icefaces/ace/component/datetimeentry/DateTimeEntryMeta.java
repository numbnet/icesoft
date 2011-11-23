/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.datetimeentry;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "dateTimeEntry",
        componentClass = "org.icefaces.ace.component.datetimeentry.DateTimeEntry",
        generatedClass = "org.icefaces.ace.component.datetimeentry.DateTimeEntryBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.ace.component.datetimeentry.DateTimeEntryRenderer",
        componentFamily = "org.icefaces.ace.component.DateTimeEntry",
        componentType = "org.icefaces.ace.component.DateTimeEntry",
        rendererType = "org.icefaces.ace.component.DateTimeEntryRenderer",
        tlddoc = "DateTimeEntry is an input component to provide a date in various ways. Other than basic " +
                "features datetimeentry supports paging, localization, ajax selection and more.")
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.ace", name = "forms/forms.css"),
        @ResourceDependency(library = "icefaces.ace", name = "jquery/ui/jquery-ui.css"),
        @ResourceDependency(library = "icefaces.ace", name = "jquery/jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "jquery/ui/jquery-ui.js"),
        @ResourceDependency(library = "icefaces.ace", name = "core/core.js"),
        @ResourceDependency(library = "icefaces.ace", name = "datetimeentry/datetimeentry.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="blur", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="change", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="valueChange", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="click", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="dblclick", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="focus", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="keydown", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="keypress", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="keyup", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="mousedown", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="mousemove", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="mouseout", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="mouseover", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="mouseup", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="select", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="dateSelect", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
})
public class DateTimeEntryMeta extends UIInputMeta {
    @Property(tlddoc = "Name of the client side widget.")
    private String widgetVar;

    @Property(tlddoc = "Sets calendar's minimum visible date.")
    private Object mindate;

    @Property(tlddoc = "Sets calendar's maximum visible date.")
    private Object maxdate;

    @Property(defaultValue = "1", tlddoc = "Enables multiple page rendering.")
    private int pages;

    @Property(defaultValue = "false", tlddoc = "Whether to render the calendar inline or as a popup.")
    private boolean renderAsPopup;

    @Property(defaultValue = "MM/dd/yyyy", tlddoc = "DateFormat pattern for localization")
    private String pattern;

    @Property(tlddoc = "Locale to be used for labels and conversion.")
    private Object locale;

    @Property(tlddoc = "Icon of the popup button.")
    private String popupIcon;

    @Property(tlddoc = "When enabled, popup icon is rendered without the button.")
    private boolean popupIconOnly;

    @Property(tlddoc = "Enables month/year navigator.")
    private boolean navigator;

    @Property(tlddoc = "String or a java.util.TimeZone instance to specify the timezone used for date " +
            "conversion, defaults to TimeZone.getDefault()")
    private Object timeZone;

    @Property(tlddoc = "Makes input text of a popup calendar readonly.")
    private boolean readOnlyInputText;

    @Property(tlddoc = "Whether to show the panel containing today button and close button for popup. " +
            "Default is false without time component and true with time component.")
    private Boolean showButtonPanel;

    @Property(tlddoc = "Effect to use when displaying and showing the popup calendar.")
    private String effect;

    @Property(defaultValue = "normal", tlddoc = "Duration of the effect.")
    private String effectDuration;

    @Property(defaultValue = "focus", tlddoc = "Client side event that displays the popup calendar.")
    private String showOn;

    @Property(tlddoc = "Displays the week number next to each week.")
    private boolean showWeek;

    @Property(tlddoc = "Displays days belonging to other months.")
    private boolean showOtherMonths;

    @Property(tlddoc = "Enables selection of days belonging to other months.")
    private boolean selectOtherMonths;

    @Property(tlddoc = "")
    private String yearRange;

    @Property(tlddoc = "")
    private boolean timeOnly;

    @Property(defaultValue = "1", tlddoc = "")
    private int stepHour;

    @Property(defaultValue = "1", tlddoc = "")
    private int stepMinute;

    @Property(defaultValue = "1", tlddoc = "")
    private int stepSecond;

    @Property(defaultValue = "0", tlddoc = "")
    private int minHour;

    @Property(defaultValue = "23", tlddoc = "")
    private int maxHour;

    @Property(defaultValue = "0", tlddoc = "")
    private int minMinute;

    @Property(defaultValue = "59", tlddoc = "")
    private int maxMinute;

    @Property(defaultValue = "0", tlddoc = "")
    private int minSecond;

    @Property(defaultValue = "59", tlddoc = "")
    private int maxSecond;

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(tlddoc = "If true then this date time entry will be disabled and can not be entered.")
    private boolean disabled;

    @Property(tlddoc = "If true then this date time entry will be read-only and can not be entered.")
    private boolean readonly;

    @Property(tlddoc = "Set to true to disable hover styling to improve performance. Default is false.")
    private boolean disableHoverStyling;

    @Property(defaultValue = "0", tlddoc = "Zero-based offset indicating which month should be displayed in the leftmost position.")
    private int leftMonthOffset;
}
