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
package org.icefaces.mobi.component.inputText;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.mobi.utils.TLDConstants;


@Component(
        tagName = "inputText",
        componentClass = "org.icefaces.mobi.component.inputText.InputText",
        rendererClass = "org.icefaces.mobi.component.inputText.InputTextRenderer",
        generatedClass = "org.icefaces.mobi.component.inputText.InputTextBase",
        componentType = "org.icefaces.InputText",
        rendererType = "org.icefaces.InputTextRenderer",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.InputText",
        tlddoc = "The inputText component renders an input element with HTML5 and Ajax support. " +
        		"This component supports various input types, when supported on the client platform," +
        		" such as \"date\", \"number\" and \"email\". " 
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="blur", javadoc="Fired when the inputText component has lost focus.",
            tlddoc="Fired when the inputText component has lost focus.", defaultRender="@this", defaultExecute="@all"),
	@ClientEvent(name="change", javadoc="Fired when the inputText component detects value is changed.",
            tlddoc="Fired when the inputText component detects value is changed.",
            defaultRender="@this", defaultExecute="@all")
}, defaultEvent="change")
public class InputTextMeta extends UIInputMeta {

    @Property(defaultValue = "text", tlddoc = "The type attribute for the input element. " +
    		"Currently supports text, textarea, phone, url, email, number, date, time, datetime.  Depending " +
    		"on device capability, a type-specific keyboard may be displayed. ")
    private String type;

    @Property(tlddoc = "The HTML5 placeholder attribute represents a short hint" +
    		" (a word or short phrase) intended to aid the user with data entry " +
    		"when the input element has no value.")
    private String placeholder;

    @Property(defaultValue = "false", tlddoc = TLDConstants.READONLY)
    private boolean readonly;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "The maximum number of characters that may be entered in this field. ")
    private int maxlength;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "The number of characters used to determine the width of this field. ")
    private int size;

    @Property(tlddoc = "The pattern attribute specifies a regular expression against which " +
    		"the control's value, or, when the multiple attribute applies and is set, " +
    		"the control's values, are to be checked. ")
    private String pattern;

    @Property(defaultValue = "false",
            tlddoc = "If the value of this attribute is \"off\", render \"off\" as " +
            		"the value of the attribute. This indicates that the browser should " +
            		"disable its autocomplete feature for this component. This is useful " +
            		"for components that perform autocompletion and do not want the browser " +
            		"interfering. If this attribute is not set or the value is \"on\", render nothing.")
    private boolean autocomplete;

    @Property(defaultValue = "on",
            tlddoc = "Capitalize the first character of the field.")
    private String autocapitalize;

    @Property(defaultValue = "on",
            tlddoc = "Correct spelling errors in the field.")
    private String autocorrect;

    @Property(defaultValue = "false",
            tlddoc = "Use and integrate required with jsf.")
    private boolean required;

    @Property(defaultValue = "3", tlddoc = "Magnifying glass for webkit used to show last three searches on a search field.")
    private int results;


    @Property(tlddoc = "Can have a title which is like tooltip.")
    private String title;
    
    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="Minimum value, only applicable to type number.")
    private int min;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="Maximum value, only applicable to type number.")
    private int max;
    
    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="The step to increase/decrease the value of the number input. " +
            "Applicable only to type \"number\". ")
    private int step;
    
    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Value of the component. ")
    private Object value;

    @Property(defaultValue = "", tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(defaultValue = "", tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue = "false", tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = TLDConstants.TABINDEX)
    private Integer tabindex;

    @Property(defaultValue = "false", tlddoc = TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(defaultValue = "false", tlddoc = TLDConstants.SINGLESUBMIT)
    private boolean singleSubmit;
    
    @Property(tlddoc = "The rendered label for the input field.")
    private String label;
}
