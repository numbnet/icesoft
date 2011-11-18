/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.component.datetimeentry;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName         = "dateTimeEntry",
        componentClass  = "org.icefaces.ace.component.datetimeentry.DateTimeEntry",
        rendererClass   = "org.icefaces.ace.component.datetimeentry.DateTimeEntryRenderer",
        generatedClass  = "org.icefaces.ace.component.datetimeentry.DateTimeEntryBase",
        extendsClass    = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.ace.DateTimeEntry",
        componentType   = "org.icefaces.ace.component.DateTimeEntry",
        rendererType    = "org.icefaces.ace.component.DateTimeEntryRenderer",
        tlddoc = "This component allows entry of a date/time. It can be in inline mode or popup mode. In either mode " +
                "you can enter a date/time by clicking. In popup mode you have the additional option of entering in " +
                "a text input field. The format of the input is determined by the nested &lt;f:convertDateTime&gt; " +
                "tag. For more information, see the " +
                "<a href=\"http://wiki.icefaces.org/display/ICE/DateTimeEntry\">Wiki doc</a>."
)
@ResourceDependencies({
    	@ResourceDependency(name="yui/yui-min.js",library="yui/3_3_0"),
    	@ResourceDependency(name="loader/loader-min.js",library="yui/3_3_0"),
        @ResourceDependency(name="util/combined.js",library="icefaces.ace"),
        @ResourceDependency(name="util/combined.css",library="icefaces.ace")
})
@ClientBehaviorHolder (events={"transition"}, defaultEvent="transition")
public class DateTimeEntryMeta extends UIInputMeta {
    @Property(defaultValue = "false", tlddoc = "When rendered as a popup, you can allow entering the date/time in " +
            "a text input field as well. This attribute flags whether text input is allowed. Format of input is " +
            "determined by the nested &lt;f:convertDateTime&gt; tag.")
    private boolean renderInputField;

    @Property(defaultValue = "false", tlddoc = "Whether to render the calendar inline or as a popup.")
    private boolean renderAsPopup;

    @Property(defaultValue = "false", tlddoc = "When singleSubmit is true, changing the value of this component will " +
            "submit and execute this component only (equivalent to &lt;f:ajax execute=\"@this\" render=\"@all\"&gt;). " +
            "When singleSubmit is false, no submit will occur. The default value is false.")
    private boolean singleSubmit;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Value of the component as a Date object.")
    private Object value;

    @Property (defaultValue = "", tlddoc="style will be rendered on a root element of this component")
    private String style;
    
    @Property (defaultValue = "", tlddoc="style class will be rendered on a root element of this component")
    private String styleClass;

    @Property (tlddoc="If true then this date time entry will be disabled and can not be entered.")
    private boolean disabled;

    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;
}
