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
package org.icefaces.component.inputText;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Implementation;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIInputMeta;

@Component(
        tagName = "inputText",
        componentClass = "org.icefaces.component.inputText.InputText",
        rendererClass = "org.icefaces.component.inputText.InputTextRenderer",
        generatedClass = "org.icefaces.component.inputText.InputTextBase",
        componentType = "org.icefaces.InputText",
        rendererType = "org.icefaces.InputTextRenderer",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.InputText",
        tlddoc = "This mobility component renders an inputText object" +
                "with added attributes of html5 input component and css support " +
                "for mobility devices"
)


public class InputTextMeta extends UIInputMeta {

    //at this time list and autocomplete are not supported on webkit safari mobile
    //type of text, search, url and tel all support the following attributes
    //password supports all the following but not list

    @Property(defaultValue = "text", tlddoc = "html5 type attribute.  Currently supports text, textarea, inputSecret,"+
    		"phone, url, email, number.  Each of the last 4 types brings up the proper keyboard on the mobility device")
    private String type;

    @Property(tlddoc = "default value of placeholder from html5 properties")
    private String placeholder;

    @Property(defaultValue = "false", tlddoc = "support readonly property of html5 so value of field cannot be modified")
    private boolean readonly;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "maxlength of input field allowed from html5")
    private int maxlength;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "specifies number of characters visible in the input field")
    private int size;

    @Property(tlddoc = "regexp_pattern to format ")
    private String pattern;

    @Property(defaultValue = "false",
            tlddoc = "not implemented in many browsers yet")
    private boolean autocomplete;

    @Property(defaultValue = "on",
            tlddoc = "capitalize the first character of the field")
    private String autocapitalize;

    @Property(defaultValue = "on",
            tlddoc = "correct spelling errors in the field")
    private String autocorrect;

    @Property(defaultValue = "false",
            tlddoc = "use and integrate required with jsf")
    private boolean required;

    @Property(defaultValue = "3", tlddoc = "magnifying glass for webkit used to show last three searches on a search field")
    private int results;


    @Property(tlddoc = "can have a title which is like tooltip")
    private String title;
    
    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="minimum value")
    private int min;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="maximum value")
    private int max;
    
    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="step to increase/decrease the value of the number input")
    private int step;
    
    //see miketaylr.com/code/input-type-attr.html to see what is supported on browser used don't have list of supported browsers yet
    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Value of the component as a Date object.")
    private Object value;

    @Property(defaultValue = "", tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(defaultValue = "", tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(defaultValue = "false", tlddoc = "If true then this date time entry will be disabled and can not be entered.")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private Integer tabindex;

    @Property(defaultValue = "false", tlddoc = "immediate as per jsf specs")
    private boolean immediate;

    @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, triggering an action on this component will submit" +
                    " and execute this component only. Equivalent to <f:ajax execute='@this' render='@all'>." +
                    " When singleSubmit is false, triggering an action on this component will only update a hidden field " +
                    " and another component must do the submit of the form to achieve server side updates. " +
                    " The default value is false.")
    private boolean singleSubmit;
}
