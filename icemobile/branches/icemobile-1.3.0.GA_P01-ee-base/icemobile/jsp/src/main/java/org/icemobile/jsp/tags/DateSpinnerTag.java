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

package org.icemobile.jsp.tags;

import static org.icemobile.util.HTML.CLASS_ATTR;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;

import java.io.IOException;
import java.io.Writer;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class DateSpinnerTag extends SimpleTagSupport {

    private String id;
    private String name;
    private String style;
    private String styleClass;
    private String pattern;

    private boolean disabled;
    private String value;
    private boolean readOnly;
    private boolean useNative = true;
    //    private Object Timezone;
    private int yearStart;
    private int yearEnd;

    private int yearInt;
    private int monthInt;
    private int dayInt;
    private TagUtil tu;

    public static final String BLACKOUT_PNL_CLASS = "mobi-date-bg";
    public static final String BLACKOUT_PNL_INVISIBLE_CLASS = "mobi-date-bg-inv";
    public static final String CONTAINER_CLASS = "mobi-date-container";
    public static final String CONTAINER_INVISIBLE_CLASS = "mobi-date-container-inv";
    public static final String INPUT_CLASS = "mobi-input-text";
    public static final String POP_UP_CLASS = "mobi-date-popup-btn";
    public static final String TITLE_CLASS = "mobi-date-title-container";
    public static final String SELECT_CONT_CLASS = "mobi-date-select-container";
    public static final String VALUE_CONT_CLASS = "mobi-date-select-value-cont";
    public static final String BUTTON_INC_CONT_CLASS = "mobi-date-btn-cont-incr";
    public static final String BUTTON_INC_CLASS = "mobi-date-btn mobi-date-btn-incr";
    public static final String SEL_VALUE_CLASS = "mobi-date-select-value";
    public static final String BUTTON_DEC_CONT_CLASS = "mobi-date-btn-cont-decr";
    public static final String BUTTON_DEC_CLASS = "mobi-date-btn mobi-date-btn-decr";

    private static final String JS_NAME = "datespinner.js";
    private static final String JS_MIN_NAME = "datespinner-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.datespinner";

    public static final String TOUCH_START_EVENT = "ontouchstart";
    public static final String CLICK_EVENT = "onclick";

    public void doTag() throws IOException {

        PageContext pageContext = (PageContext) getJspContext();
        Writer out = pageContext.getOut();
        if (tu == null) {
            tu = new TagUtil();
        }
        ClientDescriptor client = ClientDescriptor.getInstance((HttpServletRequest)pageContext.getRequest());
        if (useNative && client.isHasNativeDatePicker()) {
            out.write(tu.INPUT_TAG);
            tu.writeAttribute(out, "type", "date");
            tu.writeAttribute(out, "id", id);
            if (!tu.isValueBlank(name)) {
                tu.writeAttribute(out, "name", name);
            }
            if (tu.isValueBlank(value)) {
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                Date aDate = new Date();
                tu.writeAttribute(out, "value", df2.format(aDate));

            } else {
                tu.writeAttribute(out, "value", value);
            }
            if (disabled) {
                tu.writeAttribute(out, "disabled", "true");
            }
            if (readOnly) {
                tu.writeAttribute(out, "readonly", "true");
            }
            if( !tu.isValueBlank(style)){
                tu.writeAttribute(out, "style", style);
            }
            if( !tu.isValueBlank(styleClass)){
                tu.writeAttribute(out, "class", styleClass);
            }
            
            out.write(">" + tu.INPUT_TAG_END);
        } else {
            //            writeJavascriptFile(pageContext, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
            encodeMarkup(pageContext, out, encodeValue(value));
            encodeScript(out);
        }

    }

    protected void encodeMarkup(PageContext context, Writer writer,
                                String value)
        throws IOException {

        // check for a touch enable device and setup events accordingly
        String eventStr = tu.isTouchEventEnabled(context) ?
            TOUCH_START_EVENT : CLICK_EVENT;
        //prep for ajax submit
        StringBuilder builder = new StringBuilder(255);
        StringBuilder builder2 = new StringBuilder(255);
        String inputCallStart = "ice.mobi.datespinner.inputSubmit('";
        String jsCallStart = "ice.mobi.datespinner.select('";
        builder2.append(id).append("',{ event: event, singlesubmit: false");
        /*builder2.append("singleSubmit: ").append(singleSubmit);
	if (hasBehaviors) {
            String behaviors = this.encodeClientBehaviors(context, cbh, "change").toString();
            behaviors = behaviors.replace("\"", "\'");
            builder2.append(behaviors);
	    }*/
        builder2.append("});");
        builder.append(jsCallStart).append(builder2);
        StringBuilder inputCall = new StringBuilder(inputCallStart).append(builder2);
        String jsCall = builder.toString();

        //first do the input field and the button
        // build out first input field
        writer.write(tu.SPAN_TAG);
        tu.writeAttribute(writer, "id", id);
        //tu.writeAttribute(writer, "name", name);
        tu.writeAttribute(writer, "class", "mobi-date-wrapper");
        writer.write("> " + tu.INPUT_TAG);
        tu.writeAttribute(writer, "id", id + "_input");
        if (!tu.isValueBlank(name)) {
            tu.writeAttribute(writer, "name", name);
        }
        //tu.writeAttribute(writer, "onblur", inputCall.toString());

        // apply class attribute.
        StringBuilder classNames = new StringBuilder(INPUT_CLASS);
        if (!tu.isValueBlank(styleClass)) {
            classNames.append(" ").append(styleClass);
        }
        tu.writeAttribute(writer, "class", classNames.toString());
        if (!tu.isValueBlank(value)) {
            tu.writeAttribute(writer, "value", value);
        }
        tu.writeAttribute(writer, "type", "text");
        if (readOnly) {
            tu.writeAttribute(writer, "readonly", "true");
        }
        if (isDisabled()) {
            tu.writeAttribute(writer, "disabled", "true");
        }
        writer.write(">" + tu.INPUT_TAG_END);
        //hidden field to store state of current instance
        writer.write(tu.INPUT_TAG);
        tu.writeAttribute(writer, "type", "hidden");
        tu.writeAttribute(writer, "id", id + "_hidden");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.SPAN_TAG_END);
        // build out command button for show/hide of date select popup.
        writer.write(tu.INPUT_TAG);
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, "value", "");
        tu.writeAttribute(writer, "class", POP_UP_CLASS);
        if (isDisabled()) {
            tu.writeAttribute(writer, "disabled", "disabled");
        } else {
            // touch event can be problematic sometime not actualy getting called
            // for ui widgets that don't require rapid response then stick with onClick
            tu.writeAttribute(writer, CLICK_EVENT, "ice.mobi.datespinner.toggle('" + id + "');");
        }
        writer.write(">" + tu.INPUT_TAG_END);

        // dive that is use to hide/show the popup screen black out, invisible by default.
        writer.write(tu.DIV_TAG);
        tu.writeAttribute(writer, "id", id + "_bg");
        tu.writeAttribute(writer, "class", "mobi-date-bg-inv");
        writer.write(">" + tu.DIV_TAG_END);

        // actual popup code.
        writer.write(tu.DIV_TAG);
        tu.writeAttribute(writer, "id", id + "_popup");
        tu.writeAttribute(writer, "class", CONTAINER_INVISIBLE_CLASS);
        writer.write(">" + tu.DIV_TAG);
        tu.writeAttribute(writer, "id", id + "_title");
        tu.writeAttribute(writer, "class", TITLE_CLASS);
        writer.write(">" + value);
        writer.write(tu.DIV_TAG_END);
        writer.write(tu.DIV_TAG);                            //entire selection container
        tu.writeAttribute(writer, "class", SELECT_CONT_CLASS);
        writer.write(">");

        // if we have patter to work off then find out the best way to proceed. 
        if (pattern != null) {
            // use the index to decide the ordering type. 
            int yStart = pattern.toLowerCase().indexOf("y");
            int mStart = pattern.toLowerCase().indexOf("m");
            int dStart = pattern.toLowerCase().indexOf("d");

            // yyy MM dd
            if (yStart < mStart && mStart < dStart) {
                renderYearInput(writer, id, eventStr);
                renderMonthInput(writer, id, eventStr);
                renderDayInput(writer, id, eventStr);
            } // yyyy/dd/MM
            else if (yStart < dStart && dStart < mStart) {
                renderYearInput(writer, id, eventStr);
                renderDayInput(writer, id, eventStr);
                renderMonthInput(writer, id, eventStr);
            } // dd/MM/yyyy
            else if (dStart < mStart && mStart < yStart) {
                renderDayInput(writer, id, eventStr);
                renderMonthInput(writer, id, eventStr);
                renderYearInput(writer, id, eventStr);
            } // MM/dd/yyyy
            else if (mStart < dStart && dStart < yStart) {
                renderMonthInput(writer, id, eventStr);
                renderDayInput(writer, id, eventStr);
                renderYearInput(writer, id, eventStr);
            }  // default yyyy MM dd
            else {
                renderYearInput(writer, id, eventStr);
                renderMonthInput(writer, id, eventStr);
                renderDayInput(writer, id, eventStr);
            }
        }
        // default yyyy MM dd
        else {
            renderYearInput(writer, id, eventStr);
            renderMonthInput(writer, id, eventStr);
            renderDayInput(writer, id, eventStr);
        }

        writer.write(tu.DIV_TAG_END);    //end of selection container

        writer.write(tu.DIV_TAG);                          //button container for set or cancel
        tu.writeAttribute(writer, "class", "mobi-date-submit-container");
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", CSSUtils.STYLECLASS_BUTTON);
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, "value", "Set");
        if (!isDisabled() && !isReadOnly()) {
            tu.writeAttribute(writer, CLICK_EVENT, jsCall);
        }
        writer.write(">" + tu.INPUT_TAG_END);

        writer.write(tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", CSSUtils.STYLECLASS_BUTTON);
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, "value", "Cancel");
        tu.writeAttribute(writer, CLICK_EVENT, "ice.mobi.datespinner.close('" + id + "');");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                                        //end of button container
        writer.write(tu.DIV_TAG_END);                                         //end of entire container
    }

    public void encodeScript(Writer writer) throws IOException {
        //need to initialize the component on the page and can also
        //separate the value into yrInt, mthInt, dateInt for now just use contstants
        writer.write(tu.SPAN_TAG);
        tu.writeAttribute(writer, "id", id + "_script");
        tu.writeAttribute(writer, CLASS_ATTR, "mobi-hidden");
        writer.write(">" + tu.SCRIPT_TAG);
        tu.writeAttribute(writer, "type", "text/javascript");
        writer.write(">ice.mobi.datespinner.init('" + id + "'," + yearInt + ","
                         + monthInt + "," + dayInt + ",'" + pattern + "');");
        writer.write(tu.SCRIPT_TAG_END);
        writer.write(tu.SPAN_TAG_END);
    }

    private String encodeValue(String initialValue) throws IOException {
        String value = "";
        Date aDate = new Date();
        if (pattern == null) {
            pattern = new String("yyyy-MM-dd");
        }
        SimpleDateFormat df2 = new SimpleDateFormat(pattern);
        if (tu.isValueBlank(initialValue)) {
            //nothing values already set as default
        } else {
            try {
                if (isFormattedDate(initialValue, pattern)) {
                    value = initialValue;
                    aDate = df2.parse(value);
                } else if (isFormattedDate(initialValue, "EEE MMM dd hh:mm:ss zzz yyyy")) {
                    value = convertStringInput("EEE MMM dd hh:mm:ss zzz yyyy", pattern, initialValue); //converts to the patter the spinner is set for
                    aDate = df2.parse(value);
                }
            } catch (Exception e) {
                throw new IOException("Initial date value is invalid or does not match pattern");
            }

        }
        this.setIntValues(aDate);
        return value;
    }

    private boolean isFormattedDate(String inStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(inStr, new ParsePosition(0)) != null;
    }


    private void setIntValues(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        yearInt = cal.get(Calendar.YEAR);
        monthInt = cal.get(Calendar.MONTH) + 1;  //month is 0-indexed 0 = jan, 1=feb, etc
        dayInt = cal.get(Calendar.DAY_OF_MONTH);
    }

    private String convertStringInput(String patternIn, String patternOut,
                                      String inString) {
        SimpleDateFormat df1 = new SimpleDateFormat(patternIn);   //default date pattern
        SimpleDateFormat df2 = new SimpleDateFormat(patternOut);
        String returnString = inString;
        try {
            Date aDate = df1.parse(inString);
            returnString = df2.format(aDate);
        } catch (Exception e) {
            //means that it was already in the pattern format so just return the inString
        }
        return returnString;
    }

    private void renderDayInput(Writer writer,
                                String clientId,
                                String eventStr) throws IOException {
        writer.write(tu.DIV_TAG);                             //date select container
        tu.writeAttribute(writer, "class", VALUE_CONT_CLASS);
        writer.write(">" + tu.DIV_TAG);                            //button increment
        tu.writeAttribute(writer, "class", BUTTON_INC_CONT_CLASS);
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", BUTTON_INC_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_dUpBtn");
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, eventStr, "ice.mobi.datespinner.dUp('" + clientId + "');");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                          //end button incr
        writer.write(tu.DIV_TAG);                          //day value
        tu.writeAttribute(writer, "class", SEL_VALUE_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_dInt");
        writer.write(">" + String.valueOf(dayInt));
        writer.write(tu.DIV_TAG_END);                //end of day value
        writer.write(tu.DIV_TAG);                          //button decrement
        tu.writeAttribute(writer, "class", BUTTON_DEC_CONT_CLASS);
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", BUTTON_DEC_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_dDnBtn");
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, eventStr, "ice.mobi.datespinner.dDn('" + clientId + "');");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                             //end button decrement
        writer.write(tu.DIV_TAG_END);                         //end of dateEntry select container
    }

    private void renderMonthInput(Writer writer,
                                  String clientId,
                                  String eventStr) throws IOException {
        writer.write(tu.DIV_TAG);                             //month select container
        tu.writeAttribute(writer, "class", VALUE_CONT_CLASS);
        writer.write(">" + tu.DIV_TAG);                            //button increment
        tu.writeAttribute(writer, "class", BUTTON_INC_CONT_CLASS);
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", BUTTON_INC_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_mUpBtn");
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, eventStr, "ice.mobi.datespinner.mUp('" + clientId + "');");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                                        //end button incr
        writer.write(tu.DIV_TAG);                             //month value
        tu.writeAttribute(writer, "class", SEL_VALUE_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_mInt");
        writer.write(">" + String.valueOf(monthInt));
        writer.write(tu.DIV_TAG_END);                          //end of month value
        writer.write(tu.DIV_TAG);                             //button dectrement
        tu.writeAttribute(writer, "class", BUTTON_DEC_CONT_CLASS);
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", BUTTON_DEC_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_mDnBtn");
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, eventStr, "ice.mobi.datespinner.mDn('" + clientId + "');");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                                        //end button decrement
        writer.write(tu.DIV_TAG_END);                                //end of month select container
    }

    private void renderYearInput(Writer writer,
                                 String clientId,
                                 String eventStr) throws IOException {

        int yMin = yearStart;
        int yMax = yearEnd;

        writer.write(tu.DIV_TAG);                             //year select container
        tu.writeAttribute(writer, "class", VALUE_CONT_CLASS);
        writer.write(">" + tu.DIV_TAG);                            //button increment
        tu.writeAttribute(writer, "class", BUTTON_INC_CONT_CLASS);
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", BUTTON_INC_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_yUpBtn");
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, eventStr, "ice.mobi.datespinner.yUp('" + clientId + "'," + yMin + "," + yMax + ");");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                                         //end button incr
        writer.write(tu.DIV_TAG);                             //year value
        tu.writeAttribute(writer, "class", SEL_VALUE_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_yInt");
        writer.write(">" + String.valueOf(yearInt));
        writer.write(tu.DIV_TAG_END);                                         //end of year value
        writer.write(tu.DIV_TAG);                             //button decrement
        tu.writeAttribute(writer, "class", BUTTON_DEC_CONT_CLASS);
        writer.write(">" + tu.INPUT_TAG);
        tu.writeAttribute(writer, "class", BUTTON_DEC_CLASS);
        tu.writeAttribute(writer, "id", clientId + "_yDnBtn");
        tu.writeAttribute(writer, "type", "button");
        tu.writeAttribute(writer, eventStr, "ice.mobi.datespinner.yDn('" + clientId + "'," + yMin + "," + yMax + ");");
        writer.write(">" + tu.INPUT_TAG_END);
        writer.write(tu.DIV_TAG_END);                                        //end button decrement
        writer.write(tu.DIV_TAG_END);                                //end of year select container
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isUseNative() {
        return useNative;
    }

    public void setUseNative(boolean useNative) {
        this.useNative = useNative;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }
}
