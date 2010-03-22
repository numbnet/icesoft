/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
/* Original Copyright
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.icesoft.faces.component.selectinputdate;

import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.HtmlCommandLink;
import com.icesoft.faces.component.ext.HtmlGraphicImage;
import com.icesoft.faces.component.ext.HtmlOutputText;
import com.icesoft.faces.component.ext.HtmlSelectOneMenu;
import com.icesoft.faces.component.ext.renderkit.FormRenderer;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.faces.utils.MessageUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> The SelectInputDateRenderer class is an ICEfaces D2D renderer for the
 * SelectInputDate component. Note: This class originally was derived from the
 * MyFaces Calendar.</p>
 */
public class SelectInputDateRenderer
        extends DomBasicInputRenderer {
    //	add a static log member
    private static final Log log =
            LogFactory.getLog(SelectInputDateRenderer.class);

    private static final String CALENDAR_TABLE = "_calendarTable";
    private static final String CALENDAR_BUTTON = "_calendarButton";
    private static final String CALENDAR_POPUP = "_calendarPopup";
    private static final String HIDDEN_FIELD_NAME = "showPopup";
    private static final String DATE_SELECTED = "dateSelected";

    // constants for navigation link ids
    private static final String SELECT_MONTH = "_selMo";
    private static final String SELECT_YEAR = "_selYr";
    private static final String PREV_MONTH = "_prevmo";
    private static final String NEXT_MONTH = "_nextmo";
    private static final String PREV_YEAR = "_prevyr";
    private static final String NEXT_YEAR = "_nextyr";

    // constant for selectinputdate links
    private static final String CALENDAR = "_calendar_";
    private static final String CALENDAR_CLICK = "_calendarClick";
    private static final String ROOT_DIV = "_rootDiv";
    
    private static final int IS_NOT             = 0;
    private static final int IS_CALENDAR_BUTTON = 1;
    private static final int IS_CALENDAR        = 2;
    private static final int IS_PREV_MONTH      = 3;
    private static final int IS_NEXT_MONTH      = 4;
    private static final int IS_PREV_YEAR       = 5;
    private static final int IS_NEXT_YEAR       = 6;
    
    private static final String INPUT_TEXT_TITLE =
        "com.icesoft.faces.component.selectinputdate.INPUT_TEXT_TITLE";
    private static final String CALENDAR_TITLE =
        "com.icesoft.faces.component.selectinputdate.CALENDAR_TITLE";
    private static final String CALENDAR_SUMMARY =
        "com.icesoft.faces.component.selectinputdate.CALENDAR_SUMMARY";
    private static final String POPUP_CALENDAR_TITLE =
        "com.icesoft.faces.component.selectinputdate.POPUP_CALENDAR_TITLE";
    private static final String POPUP_CALENDAR_SUMMARY =
        "com.icesoft.faces.component.selectinputdate.POPUP_CALENDAR_SUMMARY";
    private static final String YEAR_MONTH_SUMMARY =
        "com.icesoft.faces.component.selectinputdate.YEAR_MONTH_SUMMARY";
    private static final String OPEN_POPUP_ALT =
        "com.icesoft.faces.component.selectinputdate.OPEN_POPUP_ALT";
    private static final String OPEN_POPUP_TITLE =
        "com.icesoft.faces.component.selectinputdate.OPEN_POPUP_TITLE";
    private static final String CLOSE_POPUP_ALT =
        "com.icesoft.faces.component.selectinputdate.CLOSE_POPUP_ALT";
    private static final String CLOSE_POPUP_TITLE =
        "com.icesoft.faces.component.selectinputdate.CLOSE_POPUP_TITLE";
    private static final String PREV_YEAR_ALT =
        "com.icesoft.faces.component.selectinputdate.PREV_YEAR_ALT";
    private static final String PREV_YEAR_TITLE =
        "com.icesoft.faces.component.selectinputdate.PREV_YEAR_TITLE";
    private static final String NEXT_YEAR_ALT =
        "com.icesoft.faces.component.selectinputdate.NEXT_YEAR_ALT";
    private static final String NEXT_YEAR_TITLE =
        "com.icesoft.faces.component.selectinputdate.NEXT_YEAR_TITLE";
    private static final String PREV_MONTH_ALT =
        "com.icesoft.faces.component.selectinputdate.PREV_MONTH_ALT";
    private static final String PREV_MONTH_TITLE =
        "com.icesoft.faces.component.selectinputdate.PREV_MONTH_TITLE";
    private static final String NEXT_MONTH_ALT =
        "com.icesoft.faces.component.selectinputdate.NEXT_MONTH_ALT";
    private static final String NEXT_MONTH_TITLE =
        "com.icesoft.faces.component.selectinputdate.NEXT_MONTH_TITLE";
    private static final String PREV_YEAR_LABEL =
        "com.icesoft.faces.component.selectinputdate.PREV_YEAR_LABEL";
    private static final String NEXT_YEAR_LABEL =
        "com.icesoft.faces.component.selectinputdate.NEXT_YEAR_LABEL";
    private static final int yearListSize = 11;

    /* (non-Javadoc)
    * @see javax.faces.render.Renderer#getRendersChildren()
    */
    public boolean getRendersChildren() {

        return true;
    }

    /* (non-Javadoc)
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) {

    }

    private String getHiddenFieldName(FacesContext facesContext,
                                      UIComponent uiComponent) {
        UIComponent form = findForm(uiComponent);
        String formId = form.getClientId(facesContext);
        String clientId = uiComponent.getClientId(facesContext);
        String hiddenFieldName = formId
                                 + NamingContainer.SEPARATOR_CHAR
                                 + UIViewRoot.UNIQUE_ID_PREFIX
                                 + clientId
                                 + HIDDEN_FIELD_NAME;
        return hiddenFieldName;
    }

    /* (non-Javadoc)
     * @see com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, SelectInputDate.class);
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        SelectInputDate selectInputDate = (SelectInputDate) uiComponent;

        // get the parentForm
        UIComponent parentForm = findForm(selectInputDate);
        // if there is no parent form - ERROR
        if (parentForm == null) {
            log.error("SelectInputDate::must be in a FORM");
            return;
        }
        String clientId;
        if (!domContext.isInitialized()) {
            Element root = domContext.createRootElement(HTML.DIV_ELEM);
            boolean popupState = selectInputDate.isShowPopup();
            
            clientId = uiComponent.getClientId(facesContext);
            if (uiComponent.getId() != null)
                root.setAttribute("id", clientId + ROOT_DIV);
            if (selectInputDate.isRenderAsPopup()) {
                if (log.isTraceEnabled()) {
                    log.trace("Render as popup");
                }
                // ICE-2492
                root.setAttribute(HTML.CLASS_ATTR,
                        Util.getQualifiedStyleClass(uiComponent, CSS_DEFAULT.DEFAULT_CALENDARPOPUP_CLASS, false));
                Element dateText = domContext.createElement(HTML.INPUT_ELEM);
//System.out.println("value: " + selectInputDate.getValue());
                
                dateText.setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT); // ICE-2302
                dateText.setAttribute(HTML.VALUE_ATTR,
                                      selectInputDate.getTextToRender());
                dateText.setAttribute(HTML.ID_ATTR,
                                      clientId + SelectInputDate.CALENDAR_INPUTTEXT);
                dateText.setAttribute(HTML.NAME_ATTR,
                                      clientId + SelectInputDate.CALENDAR_INPUTTEXT);
                dateText.setAttribute(HTML.CLASS_ATTR,
                                      selectInputDate.getCalendarInputClass());
                dateText.setAttribute(HTML.ONFOCUS_ATTR, "setFocus('');");
                dateText.setAttribute("onkeypress", this.ICESUBMIT);
                String onblur = combinedPassThru(
                    "setFocus('');",
                    selectInputDate.getPartialSubmit()?ICESUBMITPARTIAL:null);
                dateText.setAttribute(HTML.ONBLUR_ATTR, onblur);
                if( selectInputDate.getTabindex() != null ){
                	dateText.setAttribute(HTML.TABINDEX_ATTR, selectInputDate.getTabindex());
                }
                if (selectInputDate.getAutocomplete() != null) {
                    dateText.setAttribute("autocomplete",
                                          selectInputDate.getAutocomplete());
                }
                String tooltip = null;
                tooltip = selectInputDate.getInputTitle();
                if(tooltip == null || tooltip.length() == 0) {
                    // extract the popupdate format and use it as a tooltip
                    tooltip = getMessageWithParamFromResource(
                        facesContext, INPUT_TEXT_TITLE,
                        selectInputDate.getSpecifiedPopupDateFormat());
                }
                if(tooltip != null && tooltip.length() > 0) {
                    dateText.setAttribute(HTML.TITLE_ATTR, tooltip);
                }
                if (selectInputDate.isDisabled()) {
                    dateText.setAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR);
                }
                root.appendChild(dateText);
                Element calendarButton =
                        domContext.createElement(HTML.INPUT_ELEM);
                calendarButton
                        .setAttribute(HTML.ID_ATTR, clientId + CALENDAR_BUTTON);
                calendarButton.setAttribute(HTML.NAME_ATTR,
                                            clientId + CALENDAR_BUTTON);
                calendarButton.setAttribute(HTML.TYPE_ATTR, "image");
                calendarButton.setAttribute(HTML.ONFOCUS_ATTR, "setFocus('');");
                // render onclick to set value of hidden field to true
                String onClick = "document.forms['" +
                                 parentForm.getClientId(facesContext) + "']['" +
                                 this.getLinkId(facesContext, uiComponent) +
                                 "'].value='" + clientId + CALENDAR_BUTTON +
                                 "';"
                                 + "document.forms['" +
                                 parentForm.getClientId(facesContext) + "']['" +
                                 getHiddenFieldName(facesContext, uiComponent) +
                                 "'].value='toggle';"
                                 + "iceSubmitPartial( document.forms['" +
                                 parentForm.getClientId(facesContext) +
                                 "'], this,event); return false;";
                calendarButton.setAttribute(HTML.ONCLICK_ATTR, onClick);
                if (selectInputDate.isDisabled()) {
                    calendarButton.setAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR);
                }
                root.appendChild(calendarButton);
                // render a hidden field to manage the popup state; visible || hidden
                FormRenderer.addHiddenField(facesContext, getHiddenFieldName(
                        facesContext, uiComponent));
                String resolvedSrc;
                if (popupState) {

                    if (selectInputDate.isImageDirSet()) {
                        resolvedSrc = CoreUtils.resolveResourceURL(facesContext,
                                selectInputDate.getImageDir() + selectInputDate.getClosePopupImage());
                    } else {
                        // ICE-2127: allow override of button images via CSS
                        calendarButton.setAttribute(HTML.CLASS_ATTR, selectInputDate.getClosePopupClass());
                        // without this Firefox would display a default text on top of the image
                        resolvedSrc = CoreUtils.resolveResourceURL(facesContext,
                                selectInputDate.getImageDir() + "spacer.gif");
                    }
                    calendarButton.setAttribute(HTML.SRC_ATTR, resolvedSrc );
                    addAttributeToElementFromResource(facesContext,
                        CLOSE_POPUP_ALT, calendarButton, HTML.ALT_ATTR);
                    addAttributeToElementFromResource(facesContext,
                        CLOSE_POPUP_TITLE, calendarButton, HTML.TITLE_ATTR);
                } else {
                    if (selectInputDate.isImageDirSet()) {
                        resolvedSrc = CoreUtils.resolveResourceURL(facesContext,
                                selectInputDate.getImageDir() + selectInputDate.getOpenPopupImage());
                    } else {
                        // ICE-2127: allow override of button images via CSS
                        calendarButton.setAttribute(HTML.CLASS_ATTR, selectInputDate.getOpenPopupClass());
                        // without this Firefox would display a default text on top of the image
                        resolvedSrc = CoreUtils.resolveResourceURL(facesContext,
                                selectInputDate.getImageDir() + "spacer.gif");
                    }
                    calendarButton.setAttribute(HTML.SRC_ATTR, resolvedSrc );
                    addAttributeToElementFromResource(facesContext,
                        OPEN_POPUP_ALT, calendarButton, HTML.ALT_ATTR);
                    addAttributeToElementFromResource(facesContext,
                        OPEN_POPUP_TITLE, calendarButton, HTML.TITLE_ATTR);
                    FormRenderer.addHiddenField(
                        facesContext,
                        parentForm.getClientId(facesContext)+ ":_idcl");
                    PassThruAttributeRenderer.renderAttributes(
                        facesContext, uiComponent, new String[] {"title"});
                    domContext.stepOver();
                    return ;
                }
                
                if (!domContext.isStreamWriting()) {
                    Text br = domContext.createTextNode("<br/>");
                    root.appendChild(br);
                }
                
                Element calendarDiv = domContext.createElement(HTML.DIV_ELEM);
                calendarDiv
                        .setAttribute(HTML.ID_ATTR, clientId + CALENDAR_POPUP);
                calendarDiv.setAttribute(HTML.NAME_ATTR,
                                         clientId + CALENDAR_POPUP);
                calendarDiv.setAttribute(HTML.STYLE_ELEM,
                          "position:absolute;z-index:10;");
                addAttributeToElementFromResource(facesContext,
                    POPUP_CALENDAR_TITLE, calendarDiv, HTML.TITLE_ATTR);
                Element table = domContext.createElement(HTML.TABLE_ELEM);
                table.setAttribute(HTML.ID_ATTR, clientId + CALENDAR_TABLE);
                table.setAttribute(HTML.NAME_ATTR, clientId + CALENDAR_TABLE);
                table.setAttribute(HTML.CLASS_ATTR,
                                   selectInputDate.getStyleClass());
                table.setAttribute(HTML.STYLE_ATTR, "position:absolute;");                 
                table.setAttribute(HTML.CELLPADDING_ATTR, "0");
                table.setAttribute(HTML.CELLSPACING_ATTR, "0");
                // set mouse events on table bug 372
                String mouseOver = selectInputDate.getOnmouseover();
                table.setAttribute(HTML.ONMOUSEOVER_ATTR, mouseOver);
                String mouseOut = selectInputDate.getOnmouseout();
                table.setAttribute(HTML.ONMOUSEOUT_ATTR, mouseOut);
                String mouseMove = selectInputDate.getOnmousemove();
                table.setAttribute(HTML.ONMOUSEMOVE_ATTR, mouseMove);
                addAttributeToElementFromResource(facesContext,
                    POPUP_CALENDAR_SUMMARY, table, HTML.SUMMARY_ATTR);
                calendarDiv.appendChild(table);
                Text iframe = domContext.createTextNode("<!--[if lte IE"+
                        " 6.5]><iframe src='"+ CoreUtils.resolveResourceURL
                        (FacesContext.getCurrentInstance(),"/xmlhttp/blank")+ 
                        "' class=\"iceSelInpDateIFrameFix\"></iframe><![endif]-->");
                calendarDiv.appendChild(iframe);                 
                root.appendChild(calendarDiv);

            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Select input Date Normal");
                }
                Element table = domContext.createElement(HTML.TABLE_ELEM);
                table.setAttribute(HTML.ID_ATTR, clientId + CALENDAR_TABLE);
                table.setAttribute(HTML.NAME_ATTR, clientId + CALENDAR_TABLE);
                table.setAttribute(HTML.CLASS_ATTR,
                                   selectInputDate.getStyleClass());
                addAttributeToElementFromResource(facesContext,
                    CALENDAR_TITLE, table, HTML.TITLE_ATTR);
                table.setAttribute(HTML.CELLPADDING_ATTR, "0");
                table.setAttribute(HTML.CELLSPACING_ATTR, "0");
                // set mouse events on table bug 372
                String mouseOver = selectInputDate.getOnmouseover();
                table.setAttribute(HTML.ONMOUSEOVER_ATTR, mouseOver);
                String mouseOut = selectInputDate.getOnmouseout();
                table.setAttribute(HTML.ONMOUSEOUT_ATTR, mouseOut);
                String mouseMove = selectInputDate.getOnmousemove();
                table.setAttribute(HTML.ONMOUSEMOVE_ATTR, mouseMove);
                addAttributeToElementFromResource(facesContext,
                    CALENDAR_SUMMARY, table, HTML.SUMMARY_ATTR);
                root.appendChild(table);
                
                Element dateText = domContext.createElement(HTML.INPUT_ELEM);
                dateText.setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN);
                dateText.setAttribute(HTML.VALUE_ATTR,
                                      selectInputDate.getTextToRender());
                dateText.setAttribute(HTML.ID_ATTR,
                                      clientId + SelectInputDate.CALENDAR_INPUTTEXT);
                dateText.setAttribute(HTML.NAME_ATTR,
                                      clientId + SelectInputDate.CALENDAR_INPUTTEXT);
                root.appendChild(dateText);
            }
        }
        clientId = uiComponent.getClientId(facesContext);

        Date value;

        if (selectInputDate.isNavEvent()) {
            if (log.isTraceEnabled()) {
                log.trace("Rendering Nav Event");
            }
            value = selectInputDate.getNavDate();
//System.out.println("navDate: " + value);
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Logging non nav event");
            }
            value = CustomComponentUtils.getDateValue(selectInputDate);
//System.out.println("CustomComponentUtils.getDateValue: " + value);
        }
        
        TimeZone tz = selectInputDate.resolveTimeZone(facesContext);
        Locale currentLocale = selectInputDate.resolveLocale(facesContext);
        
        Calendar timeKeeper = Calendar.getInstance(tz, currentLocale);
        timeKeeper.setTime(value != null ? value : new Date());

        DateFormatSymbols symbols = new DateFormatSymbols(currentLocale);

        String[] weekdays = mapWeekdays(symbols);
        String[] weekdaysLong = mapWeekdaysLong(symbols);
        String[] months = mapMonths(symbols);

        // use the currentDay to set focus - do not set
        int lastDayInMonth = timeKeeper.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = timeKeeper.get(Calendar.DAY_OF_MONTH); // starts at 1

        if (currentDay > lastDayInMonth) {
            currentDay = lastDayInMonth;
        }

        timeKeeper.set(Calendar.DAY_OF_MONTH, 1);

        int weekDayOfFirstDayOfMonth =
                mapCalendarDayToCommonDay(timeKeeper.get(Calendar.DAY_OF_WEEK));

        int weekStartsAtDayIndex =
                mapCalendarDayToCommonDay(timeKeeper.getFirstDayOfWeek());

        // do not require a writer - clean out all methods that reference a writer
        ResponseWriter writer = facesContext.getResponseWriter();

        Element root = (Element) domContext.getRootNode();

      
        if (selectInputDate.isRenderAsPopup()) {
            if (log.isTraceEnabled()) {
                log.trace("SelectInputDate as Popup");
            }

            // assumption input text is first child
            Element dateText = (Element) root.getFirstChild();
//System.out.println("dateText  currentValue: " + currentValue);
            dateText.setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT); // ICE-2302
            dateText.setAttribute(
                HTML.VALUE_ATTR,
                selectInputDate.getTextToRender());
    
            // get tables , our table is the first and only one
            NodeList tables = root.getElementsByTagName(HTML.TABLE_ELEM);
            // assumption we want the first table in tables. there should only be one
            Element table = (Element) tables.item(0);

            PassThruAttributeRenderer.renderAttributes(
                facesContext, uiComponent, new String[] {"title"});

            Element tr1 = domContext.createElement(HTML.TR_ELEM);

            table.appendChild(tr1);
	            writeMonthYearHeader(domContext, facesContext, writer,
	                                 selectInputDate, timeKeeper,
	                                 currentDay, tr1,
	                                 selectInputDate.getMonthYearRowClass(),
                                     currentLocale, months, weekdays, weekdaysLong);
	
	            Element tr2 = domContext.createElement(HTML.TR_ELEM);
	            table.appendChild(tr2);
	
	            writeWeekDayNameHeader(domContext, weekStartsAtDayIndex, weekdays,
	                                   facesContext, writer, selectInputDate, tr2,
	                                   selectInputDate.getWeekRowClass(),
                                       timeKeeper, months, weekdaysLong);
	
	            writeDays(domContext, facesContext, writer, selectInputDate,
	                      timeKeeper,
	                      currentDay, weekStartsAtDayIndex,
	                      weekDayOfFirstDayOfMonth,
	                      lastDayInMonth, table,
                          months, weekdays, weekdaysLong);


        } else {
            if (log.isTraceEnabled()) {
                log.trace("renderNormal::endcodeEnd");
            }
            // assume table is the first child
            Element table = (Element) root.getFirstChild();

            PassThruAttributeRenderer.renderAttributes(
                facesContext, uiComponent, new String[] {"title"});

            Element tr1 = domContext.createElement(HTML.TR_ELEM);
            table.appendChild(tr1);

            writeMonthYearHeader(domContext, facesContext, writer,
                                 selectInputDate, timeKeeper,
                                 currentDay, tr1,
                                 selectInputDate.getMonthYearRowClass(),
                                 currentLocale, months, weekdays, weekdaysLong);

            Element tr2 = domContext.createElement(HTML.TR_ELEM);

            writeWeekDayNameHeader(domContext, weekStartsAtDayIndex, weekdays,
                                   facesContext, writer, selectInputDate, tr2,
                                   selectInputDate.getWeekRowClass(),
                                   timeKeeper, months, weekdaysLong);

            table.appendChild(tr2);

            writeDays(domContext, facesContext, writer, selectInputDate,
                      timeKeeper,
                      currentDay, weekStartsAtDayIndex,
                      weekDayOfFirstDayOfMonth,
                      lastDayInMonth, table,
                      months, weekdays, weekdaysLong);

        }

        // purge child components as they have been encoded no need to keep them around
        selectInputDate.getChildren().removeAll(selectInputDate.getChildren());

        // steps to the position where the next sibling should be rendered
        domContext.stepOver();
        domContext.streamWrite(facesContext, uiComponent);
    }

    private void writeMonthYearHeader(DOMContext domContext,
                                      FacesContext facesContext,
                                      ResponseWriter writer,
                                      SelectInputDate inputComponent,
                                      Calendar timeKeeper,
                                      int currentDay, Element headerTr,
                                      String styleClass, Locale currentLocale,
                                      String[] months, String[] weekdays, String[] weekdaysLong)
            throws IOException {

        Element table = domContext.createElement(HTML.TABLE_ELEM);
        table.setAttribute(HTML.CELLPADDING_ATTR, "0");
        table.setAttribute(HTML.CELLSPACING_ATTR, "0");
        table.setAttribute(HTML.WIDTH_ATTR, "100%");
        addAttributeToElementFromResource(facesContext,
            YEAR_MONTH_SUMMARY, table, HTML.SUMMARY_ATTR);

        Element tr = domContext.createElement(HTML.TR_ELEM);

        Element headertd = domContext.createElement(HTML.TD_ELEM);
        table.appendChild(tr);
        headertd.appendChild(table);
        headerTr.appendChild(headertd);

        headertd.setAttribute(HTML.COLSPAN_ATTR, "7"); // weekdays.length = 7

        int calYear = timeKeeper.get(Calendar.YEAR);
        if (inputComponent.getHightlightRules().containsKey(Calendar.YEAR + "$" + calYear)) {
            inputComponent.setHighlightYearClass(inputComponent.getHightlightRules().get(Calendar.YEAR + "$" + calYear) + " ");
        } else {
            inputComponent.setHighlightYearClass("");
        }

        int calMonth = timeKeeper.get(Calendar.MONTH) + 1;
        if (inputComponent.getHightlightRules().containsKey(Calendar.MONTH + "$" + calMonth)) {
            inputComponent.setHighlightMonthClass(inputComponent.getHightlightRules().get(Calendar.MONTH + "$" + calMonth) + " ");
        } else {
            inputComponent.setHighlightMonthClass("");
        }
        // first render month with navigation back and forward
        if (inputComponent.isRenderMonthAsDropdown()) {
            writeMonthDropdown(facesContext, domContext, inputComponent, tr, months, timeKeeper, currentDay, styleClass);
        } else {
            Calendar cal = shiftMonth(facesContext, timeKeeper, currentDay, -1);
            writeCell(domContext, facesContext, writer, inputComponent,
                      "<", cal.getTime(), styleClass, tr,
                      inputComponent.getImageDir() +
                      inputComponent.getMovePreviousImage(), -1,
                      timeKeeper, months, weekdaysLong);

            Element td = domContext.createElement(HTML.TD_ELEM);
            td.setAttribute(HTML.CLASS_ATTR, styleClass);
            td.setAttribute(HTML.WIDTH_ATTR, "40%");
            Text text = domContext
                    .createTextNode(months[timeKeeper.get(Calendar.MONTH)] + "");
            td.appendChild(text);

            tr.appendChild(td);

            cal = shiftMonth(facesContext, timeKeeper, currentDay, 1);
/*
            int calYear = cal.get(Calendar.YEAR);

            if (inputComponent.getHightlightRules().containsKey(Calendar.YEAR+"$"+calYear)) {
                inputComponent.setHighlightYearClass(inputComponent.getHightlightRules().get(Calendar.YEAR+"$"+calYear) + " ");
            } else {
                inputComponent.setHighlightYearClass("");
            }

            int calMonth = cal.get(Calendar.MONTH);
            if (inputComponent.getHightlightRules().containsKey(Calendar.MONTH+"$"+calMonth)) {
                inputComponent.setHighlightMonthClass(inputComponent.getHightlightRules().get(Calendar.MONTH+"$"+calMonth) + " ");
            } else {
                inputComponent.setHighlightMonthClass("");
            }
*/
            writeCell(domContext, facesContext, writer, inputComponent,
                      ">", cal.getTime(), styleClass, tr,
                      inputComponent.getImageDir() +
                      inputComponent.getMoveNextImage(), -1,
                      timeKeeper, months, weekdaysLong);
        }

        // second add an empty td
        Element emptytd = domContext.createElement(HTML.TD_ELEM);
        emptytd.setAttribute(HTML.CLASS_ATTR, styleClass);
        Text emptytext = domContext.createTextNode("");
        emptytd.appendChild(emptytext);

        tr.appendChild(emptytd);

        // third render year with navigation back and forward
        if (inputComponent.isRenderYearAsDropdown()) {
            writeYearDropdown(facesContext, domContext, inputComponent, tr, timeKeeper, currentDay, styleClass);
        } else {
            Calendar cal = shiftYear(facesContext, timeKeeper, currentDay, -1);

            writeCell(domContext, facesContext, writer, inputComponent,
                      "<<", cal.getTime(), styleClass, tr,
                      inputComponent.getImageDir() +
                      inputComponent.getMovePreviousImage(), -1,
                      timeKeeper, months, weekdaysLong);

            Element yeartd = domContext.createElement(HTML.TD_ELEM);
            yeartd.setAttribute(HTML.CLASS_ATTR, styleClass);
            Text yeartext =
                    domContext.createTextNode("" + timeKeeper.get(Calendar.YEAR));
            yeartd.appendChild(yeartext);

            tr.appendChild(yeartd);

            cal = shiftYear(facesContext, timeKeeper, currentDay, 1);

            writeCell(domContext, facesContext, writer, inputComponent,
                      ">>", cal.getTime(), styleClass, tr,
                      inputComponent.getImageDir() +
                      inputComponent.getMoveNextImage(), -1,
                      timeKeeper, months, weekdaysLong);
        }

    }

    private void writeMonthDropdown(FacesContext facesContext,
                                    DOMContext domContext,
                                    SelectInputDate component,
                                    Element tr,
                                    String[] months,
                                    Calendar timeKeeper,
                                    int currentDay,
                                    String styleClass) throws IOException {
        Element td = domContext.createElement(HTML.TD_ELEM);
        if (styleClass != null) {
            td.setAttribute(HTML.CLASS_ATTR, styleClass);
        }
        tr.appendChild(td);

        domContext.setCursorParent(td);
        domContext.streamWrite(facesContext, component, domContext.getRootNode(), td);

        HtmlSelectOneMenu dropDown = new HtmlSelectOneMenu();
        dropDown.setId(component.getId() + SELECT_MONTH);
        dropDown.setPartialSubmit(true);
        dropDown.setTransient(true);
        dropDown.setImmediate(component.isImmediate());
        dropDown.setDisabled(component.isDisabled());
        dropDown.setStyleClass(component.getMonthYearDropdownClass());

        UISelectItem selectItem;
        Calendar calendar;
        int currentMonth = timeKeeper.get(Calendar.MONTH);
        Converter converter = component.resolveDateTimeConverter(facesContext);
        for (int i = 0; i < months.length; i++) {
            selectItem = new UISelectItem();
            calendar = shiftMonth(facesContext, timeKeeper, currentDay, i - currentMonth);
            selectItem.setItemValue(converter.getAsString(facesContext, component, calendar.getTime()));
            selectItem.setItemLabel(months[i]);
            dropDown.getChildren().add(selectItem);
            if (i == currentMonth) {
                dropDown.setValue(selectItem.getItemValue());
            }
        }

        component.getChildren().add(dropDown);
        dropDown.encodeBegin(facesContext);
        dropDown.encodeChildren(facesContext);
        dropDown.encodeEnd(facesContext);
        component.getChildren().remove(dropDown);

        domContext.stepOver();
    }

    private void writeYearDropdown(FacesContext facesContext,
                                   DOMContext domContext,
                                   SelectInputDate component,
                                   Element tr,
                                   Calendar timeKeeper,
                                   int currentDay,
                                   String styleClass) throws IOException {
        Element td = domContext.createElement(HTML.TD_ELEM);
        if (styleClass != null) {
            td.setAttribute(HTML.CLASS_ATTR, styleClass);
        }
        tr.appendChild(td);

        domContext.setCursorParent(td);
        domContext.streamWrite(facesContext, component, domContext.getRootNode(), td);

        HtmlSelectOneMenu dropDown = new HtmlSelectOneMenu();
        dropDown.setId(component.getId() + SELECT_YEAR);
        dropDown.setPartialSubmit(true);
        dropDown.setTransient(true);
        dropDown.setImmediate(component.isImmediate());
        dropDown.setDisabled(component.isDisabled());
        dropDown.setStyleClass(component.getMonthYearDropdownClass());

        int timeKeeperYear = timeKeeper.get(Calendar.YEAR);
        int startYear = timeKeeperYear - yearListSize / 2; // not perfectly centered if size is even
        Converter converter = component.resolveDateTimeConverter(facesContext);
        UISelectItem selectItem;
        Calendar calendar;
        String itemValue, itemLabel;

        for (int i = startYear - 1, j = i, k = startYear + yearListSize; i <= k; i++) {
            if (i == j) {
                calendar = shiftYear(facesContext, timeKeeper, currentDay, -yearListSize);
                itemLabel = MessageUtils.getResource(facesContext, PREV_YEAR_LABEL);
            } else if (i == k) {
                calendar = shiftYear(facesContext, timeKeeper, currentDay, yearListSize);
                itemLabel = MessageUtils.getResource(facesContext, NEXT_YEAR_LABEL);
            } else {
                calendar = shiftYear(facesContext, timeKeeper, currentDay, i - timeKeeperYear);
                itemLabel = String.valueOf(calendar.get(Calendar.YEAR));
            }
            itemValue = converter.getAsString(facesContext, component, calendar.getTime());
            selectItem = new UISelectItem();
            selectItem.setItemValue(itemValue);
            selectItem.setItemLabel(itemLabel);
            dropDown.getChildren().add(selectItem);
            if (i == timeKeeperYear) {
                dropDown.setValue(itemValue);
            }
        }

        component.getChildren().add(dropDown);
        dropDown.encodeBegin(facesContext);
        dropDown.encodeChildren(facesContext);
        dropDown.encodeEnd(facesContext);
        component.getChildren().remove(dropDown);

        domContext.stepOver();
    }

    private Calendar shiftMonth(FacesContext facesContext,
                                Calendar timeKeeper, int currentDay,
                                int shift) {
        Calendar cal = copyCalendar(facesContext, timeKeeper);

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + shift);

        if (currentDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            currentDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        cal.set(Calendar.DAY_OF_MONTH, currentDay);
        return cal;
    }

    private Calendar shiftYear(FacesContext facesContext,
                               Calendar timeKeeper, int currentDay, int shift) {
        Calendar cal = copyCalendar(facesContext, timeKeeper);
        
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + shift);

        if (currentDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            currentDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        cal.set(Calendar.DAY_OF_MONTH, currentDay);
        return cal;
    }

    private Calendar copyCalendar(FacesContext facesContext,
                                  Calendar timeKeeper) {
        Calendar cal = (Calendar) timeKeeper.clone();
        return cal;
    }

    private void writeWeekDayNameHeader(DOMContext domContext,
                                        int weekStartsAtDayIndex,
                                        String[] weekdays,
                                        FacesContext facesContext,
                                        ResponseWriter writer,
                                        SelectInputDate inputComponent, Element tr,
                                        String styleClass,
                                        Calendar timeKeeper,
                                        String[] months, String[] weekdaysLong)
            throws IOException {
        // the week can start with Sunday (index 0) or Monday (index 1)
        for (int i = weekStartsAtDayIndex; i < weekdays.length; i++) {
            writeCell(domContext, facesContext,
                      writer, inputComponent, weekdays[i], null, styleClass, tr,
                      null, i,
                      timeKeeper, months, weekdaysLong);
        }

        // if week start on Sunday this block is not executed
        // if week start on Monday this block will run once adding Sunday to End of week.
        for (int i = 0; i < weekStartsAtDayIndex; i++) {
            writeCell(domContext, facesContext, writer,
                      inputComponent, weekdays[i], null, styleClass, tr, null, i,
                      timeKeeper, months, weekdaysLong);
        }
    }

    private void writeDays(DOMContext domContext, FacesContext facesContext,
                           ResponseWriter writer,
                           SelectInputDate inputComponent, Calendar timeKeeper,
                           int currentDay, int weekStartsAtDayIndex,
                           int weekDayOfFirstDayOfMonth, int lastDayInMonth,
                           Element table, String[] months,
                           String[] weekdays, String[] weekdaysLong)
            throws IOException {
        Calendar cal;

        int space = (weekStartsAtDayIndex < weekDayOfFirstDayOfMonth) ?
                    (weekDayOfFirstDayOfMonth - weekStartsAtDayIndex)
                    : (weekdays.length - weekStartsAtDayIndex +
                       weekDayOfFirstDayOfMonth);

        if (space == weekdays.length) {
            space = 0;
        }

        int columnIndexCounter = 0;

        Element tr1 = null;
        for (int i = 0; i < space; i++) {
            if (columnIndexCounter == 0) {
                tr1 = domContext.createElement(HTML.TR_ELEM);
                table.appendChild(tr1);
            }
            
            writeCell(domContext, facesContext, writer, inputComponent, "&nbsp;",
                      null, inputComponent.getDayCellClass(), tr1, null,
                      (weekStartsAtDayIndex + i) % 7,
                      timeKeeper, months, weekdaysLong);
            columnIndexCounter++;
        }

        Element tr2 = null;
        for (int i = 0; i < lastDayInMonth; i++) {
            if (columnIndexCounter == 0) {
                // don't create a new row until we have finished the last
                tr2 = domContext.createElement(HTML.TR_ELEM);
                table.appendChild(tr2);
            }

            cal = copyCalendar(facesContext, timeKeeper);
            cal.set(Calendar.DAY_OF_MONTH,
                    i + 1); // i starts at 0 DAY_OF_MONTH start at 1

            // get day, month and year
            // use these to check if the currentDayCell style class should be used
            int day = 0;
            int month = 0;
            int year = 0;
            try {
                Date currentDate = (Date) inputComponent.getValue();
                Calendar current = copyCalendar(facesContext, timeKeeper);
                current.setTime(currentDate);
                
                day = current.get(Calendar.DAY_OF_MONTH); // starts with 1
                month = current.get(Calendar.MONTH); // starts with 0
                year = current.get(Calendar.YEAR);
            } catch (Exception e) {
                // hmmm this should never happen
            }
            
           
            if (inputComponent.getHightlightRules().size()>0) {
                int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
                int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
                int date =cal.get(Calendar.DATE);
                int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                int dayOfWeekInMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                
                if (inputComponent.getHightlightRules().containsKey(Calendar.WEEK_OF_YEAR+"$"+weekOfYear)) {
                    inputComponent.addHighlightWeekClass(String.valueOf(inputComponent.getHightlightRules().get(Calendar.WEEK_OF_YEAR+"$"+weekOfYear)));
                }
                if (inputComponent.getHightlightRules().containsKey(Calendar.WEEK_OF_MONTH+"$"+weekOfMonth)) {
                    inputComponent.addHighlightWeekClass(String.valueOf(inputComponent.getHightlightRules().get(Calendar.WEEK_OF_MONTH+"$"+weekOfMonth)));
                }
                if (inputComponent.getHightlightRules().containsKey(Calendar.DATE+"$"+date)) {
                    inputComponent.addHighlightDayClass(String.valueOf(inputComponent.getHightlightRules().get(Calendar.DATE+"$"+date)));
                }  
                if (inputComponent.getHightlightRules().containsKey(Calendar.DAY_OF_YEAR+"$"+dayOfYear)) {
                    inputComponent.addHighlightDayClass(String.valueOf(inputComponent.getHightlightRules().get(Calendar.DAY_OF_YEAR+"$"+dayOfYear)));
                } 
                if (inputComponent.getHightlightRules().containsKey(Calendar.DAY_OF_WEEK+"$"+dayOfWeek)) {
                    inputComponent.addHighlightDayClass(String.valueOf(inputComponent.getHightlightRules().get(Calendar.DAY_OF_WEEK+"$"+dayOfWeek)));
                } 
                if (inputComponent.getHightlightRules().containsKey(Calendar.DAY_OF_WEEK_IN_MONTH+"$"+dayOfWeekInMonth)) {
                    inputComponent.addHighlightDayClass(String.valueOf(inputComponent.getHightlightRules().get(Calendar.DAY_OF_WEEK_IN_MONTH+"$"+dayOfWeekInMonth)));
                }                
            }
            
            String cellStyle = inputComponent.getDayCellClass() + " " + inputComponent.getHighlightDayCellClass();            
            
            
            if ((cal.get(Calendar.DAY_OF_MONTH) == day) &&
                    (cal.get(Calendar.MONTH) == month) &&
                    (cal.get(Calendar.YEAR) == year)) {
                    cellStyle = inputComponent.getCurrentDayCellClass();
                }
             
            
            // do not automatically select date when navigating by month
            if ((cal.get(Calendar.DAY_OF_MONTH) == day) &&
                (cal.get(Calendar.MONTH) == month) &&
                (cal.get(Calendar.YEAR) == year)) {
                cellStyle = inputComponent.getCurrentDayCellClass();
            }

            if (tr2 == null) {
                // finish the first row
                writeCell(domContext, facesContext, writer,
                          inputComponent, String.valueOf(i + 1), cal.getTime(),
                          cellStyle, tr1, null, i,
                          timeKeeper, months, weekdaysLong);
            } else {
                // write to new row
                writeCell(domContext, facesContext, writer,
                          inputComponent, String.valueOf(i + 1), cal.getTime(),
                          cellStyle, tr2, null, i,
                          timeKeeper, months, weekdaysLong);
            }

            columnIndexCounter++;

            if (columnIndexCounter == weekdays.length) {
                columnIndexCounter = 0;
            }
            inputComponent.resetHighlightClasses(Calendar.WEEK_OF_YEAR);
        }

        if ((columnIndexCounter != 0) && (tr2 != null)) {
            for (int i = columnIndexCounter; i < weekdays.length; i++) {
                writeCell(domContext, facesContext, writer,
                          inputComponent, "&nbsp;", null,
                          inputComponent.getDayCellClass(), tr2, null,
                          (weekStartsAtDayIndex + i) % 7,
                          timeKeeper, months, weekdaysLong);
             }
        }

    }

    private void writeCell(DOMContext domContext, FacesContext facesContext,
                           ResponseWriter writer, SelectInputDate component,
                           String content,
                           Date valueForLink, String styleClass, Element tr,
                           String imgSrc, int weekDayIndex,
                           Calendar timeKeeper,
                           String[] months, String[] weekdaysLong)
            throws IOException {
        Element td = domContext.createElement(HTML.TD_ELEM);
        tr.appendChild(td);

        if (styleClass != null) {
            td.setAttribute(HTML.CLASS_ATTR, styleClass);
        }

        if (valueForLink == null) {
            if(content != null && content.length() > 0) {
                Text text = domContext.createTextNode(content);
                td.setAttribute(HTML.TITLE_ATTR,weekdaysLong[weekDayIndex]);
                td.appendChild(text);
            }
        } else {
            // set cursor to render into the td
            domContext.setCursorParent(td);
            domContext.streamWrite(facesContext, component,
                                   domContext.getRootNode(), td);
            writeLink(content, component, facesContext, valueForLink,
                      styleClass, imgSrc, td, timeKeeper,
                      months, weekdaysLong);
            // steps to the position where the next sibling should be rendered
            domContext.stepOver();
        }

    }

    private void writeLink(String content,
                           SelectInputDate component,
                           FacesContext facesContext,
                           Date valueForLink,
                           String styleClass,
                           String imgSrc,
                           Element td,
                           Calendar timeKeeper,
                           String[] months, String[] weekdaysLong)
            throws IOException {

        Converter converter = component.resolveDateTimeConverter(facesContext);
        HtmlCommandLink link = new HtmlCommandLink();
        Calendar cal = copyCalendar(facesContext, timeKeeper);
        cal.setTime(valueForLink);
        String month = months[cal.get(Calendar.MONTH)];
        String year = String.valueOf(cal.get(Calendar.YEAR));
        int dayInt = cal.get(Calendar.DAY_OF_WEEK);
        dayInt = mapCalendarDayToCommonDay(dayInt);
        String day = weekdaysLong[dayInt];
        String altText = null;
        String titleText = null;
        // assign special ids for navigation links
        if (content.equals("<")) {
            link.setId(component.getId() + this.PREV_MONTH);
            altText = getMessageWithParamFromResource(
                facesContext, PREV_MONTH_ALT, month);
            titleText = getMessageWithParamFromResource(
                facesContext, PREV_MONTH_TITLE, month);
        } else if (content.equals(">")) {
            link.setId(component.getId() + this.NEXT_MONTH);
            altText = getMessageWithParamFromResource(
                facesContext, NEXT_MONTH_ALT, month);
            titleText = getMessageWithParamFromResource(
                facesContext, NEXT_MONTH_TITLE, month);
        } else if (content.equals(">>")) {
            link.setId(component.getId() + this.NEXT_YEAR);
            altText = getMessageWithParamFromResource(
                facesContext, NEXT_YEAR_ALT, year);
            titleText = getMessageWithParamFromResource(
                facesContext, NEXT_YEAR_TITLE, year);
        } else if (content.equals("<<")) {
            link.setId(component.getId() + this.PREV_YEAR);
            altText = getMessageWithParamFromResource(
                facesContext, PREV_YEAR_ALT, year);
            titleText = getMessageWithParamFromResource(
                facesContext, PREV_YEAR_TITLE, year);
        } else {
            link.setId(component.getId() + CALENDAR + content.hashCode());
            if (log.isDebugEnabled()) {
                log.debug("linkId=" +
                    component.getId() + CALENDAR + content.hashCode());
            }
        }

        link.setPartialSubmit(true);
        link.setTransient(true);
        link.setImmediate(component.isImmediate());
        link.setDisabled(((SelectInputDate) component).isDisabled());

        if (imgSrc != null) {
            HtmlGraphicImage img = new HtmlGraphicImage();
            if (component.isImageDirSet()) {
                img.setUrl(imgSrc);
            } else {
                // ICE-2127: allow override of button images via CSS
                // getImageDir() returns default
                // without a dummy image Firefox would show the alt text
                img.setUrl(component.getImageDir() + "spacer.gif");
                if (content.equals("<") || content.equals("<<")) {
                    img.setStyleClass(component.getMovePrevClass());
                } else if (content.equals(">") || content.equals(">>")) {
                    img.setStyleClass(component.getMoveNextClass());
                }
            }
            img.setHeight("16");
            img.setWidth("17");
            img.setStyle("border:none;");
            if(altText != null)
                img.setAlt(altText);
            if(titleText != null)
                img.setTitle(titleText);
            img.setId(component.getId() + "_img_" + content.hashCode());
            img.setTransient(true);
            link.getChildren().add(img);
        } else {            
            HtmlOutputText text = new HtmlOutputText();
            text.setValue(content);
            text.setId(component.getId() + "_text_" + content.hashCode());
            text.setTransient(true);
            text.setTitle(day);
            link.getChildren().add(text);
        }
        // links are focus aware       
        UIParameter parameter = new UIParameter();
        parameter.setId(
                component.getId() + "_" + valueForLink.getTime() + "_param");
        parameter.setTransient(true);
        parameter.setName(component.getClientId(facesContext) + CALENDAR_CLICK);
        parameter.setValue(
                converter.getAsString(facesContext, component, valueForLink));

        component.getChildren().add(link);
        link.getChildren().add(parameter);

        //don't add this parameter for next and previouse button/link        
        if (!content.equals("<") && !content.equals(">") &&
            !content.equals(">>") && !content.equals("<<")) {
            //this parameter would be use to close the popup selectinputdate after date selection.
            parameter = new UIParameter();
            parameter.setId(component.getId() + "_" + valueForLink.getTime() +
                            "_" + DATE_SELECTED);
            parameter.setName(getHiddenFieldName(facesContext, component));
            parameter.setValue("false");
            link.getChildren().add(parameter);
        }
        link.encodeBegin(facesContext);
        link.encodeChildren(facesContext);
        link.encodeEnd(facesContext);
        td.setAttribute(HTML.ID_ATTR, link.getClientId(facesContext) + "td");
        try {
            Integer.parseInt(content);
            ((SelectInputDate) component).getLinkMap()
                    .put(link.getClientId(facesContext), td);
            if (styleClass.equals(CSS_DEFAULT.DEFAULT_CALENDAR + CSS_DEFAULT
                    .DEFAULT_CURRENTDAYCELL_CLASS)) {
                ((SelectInputDate) component)
                        .setSelectedDayLink(link.getClientId(facesContext));
            }
        } catch (NumberFormatException e) {

        }


    }
    
    protected void addAttributeToElementFromResource(
        FacesContext facesContext, String resName, Element elem, String attrib)
    {
        String res = MessageUtils.getResource(facesContext, resName);
        if(res != null && res.length() > 0) {
            elem.setAttribute(attrib, res);
        }
    }
    
    protected String getMessageWithParamFromResource(
        FacesContext facesContext, String resName, String param)
    {
        String msg = null;
        if(param != null && param.length() > 0) {
            String messagePattern = MessageUtils.getResource(
                facesContext, resName);
            if(messagePattern != null && messagePattern.length() > 0) {
                msg = MessageFormat.format(
                    messagePattern, new Object[] {param});
            }
        }
        return msg;
    }
    
    private int mapCalendarDayToCommonDay(int day) {
        switch (day) {
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }

    private static String[] mapWeekdays(DateFormatSymbols symbols) {
        String[] weekdays = new String[7];

        String[] localeWeekdays = symbols.getShortWeekdays();

        weekdays[0] = localeWeekdays[Calendar.MONDAY];
        weekdays[1] = localeWeekdays[Calendar.TUESDAY];
        weekdays[2] = localeWeekdays[Calendar.WEDNESDAY];
        weekdays[3] = localeWeekdays[Calendar.THURSDAY];
        weekdays[4] = localeWeekdays[Calendar.FRIDAY];
        weekdays[5] = localeWeekdays[Calendar.SATURDAY];
        weekdays[6] = localeWeekdays[Calendar.SUNDAY];

        return weekdays;
    }

    private static String[] mapWeekdaysLong(DateFormatSymbols symbols) {
        String[] weekdays = new String[7];

        String[] localeWeekdays = symbols.getWeekdays();

        weekdays[0] = localeWeekdays[Calendar.MONDAY];
        weekdays[1] = localeWeekdays[Calendar.TUESDAY];
        weekdays[2] = localeWeekdays[Calendar.WEDNESDAY];
        weekdays[3] = localeWeekdays[Calendar.THURSDAY];
        weekdays[4] = localeWeekdays[Calendar.FRIDAY];
        weekdays[5] = localeWeekdays[Calendar.SATURDAY];
        weekdays[6] = localeWeekdays[Calendar.SUNDAY];

        return weekdays;
    }

    /**
     * @param symbols
     * @return months - String[] containing localized month names
     */
    public static String[] mapMonths(DateFormatSymbols symbols) {
        String[] months = new String[12];

        String[] localeMonths = symbols.getMonths();

        months[0] = localeMonths[Calendar.JANUARY];
        months[1] = localeMonths[Calendar.FEBRUARY];
        months[2] = localeMonths[Calendar.MARCH];
        months[3] = localeMonths[Calendar.APRIL];
        months[4] = localeMonths[Calendar.MAY];
        months[5] = localeMonths[Calendar.JUNE];
        months[6] = localeMonths[Calendar.JULY];
        months[7] = localeMonths[Calendar.AUGUST];
        months[8] = localeMonths[Calendar.SEPTEMBER];
        months[9] = localeMonths[Calendar.OCTOBER];
        months[10] = localeMonths[Calendar.NOVEMBER];
        months[11] = localeMonths[Calendar.DECEMBER];

        return months;
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @return id - used for the commandlink hidden field in the form
     */
    public String getLinkId(FacesContext facesContext,
                            UIComponent uiComponent) {
        //this is a fix for bug 340
        UIComponent form = findForm(uiComponent);
        String formId = form.getClientId(facesContext);
        return formId + ":_idcl";
    }

    private int checkLink(String clickedLink, String clientId) {
        if (clickedLink == null) {
            return IS_NOT;
        }
        else if( (clientId+CALENDAR_BUTTON).equals(clickedLink) ) {
            return IS_CALENDAR_BUTTON;
        }
        else if( clickedLink.startsWith(clientId+CALENDAR) ) {
            return IS_CALENDAR;
        }
        else if( (clientId+PREV_MONTH).equals(clickedLink) ) {
            return IS_PREV_MONTH;
        }
        else if( (clientId+NEXT_MONTH).equals(clickedLink) ) {
            return IS_NEXT_MONTH;
        }
        else if( (clientId+PREV_YEAR).equals(clickedLink) ) {
            return IS_PREV_YEAR;
        }
        else if( (clientId+NEXT_YEAR).equals(clickedLink) ) {
            return IS_NEXT_YEAR;
        }
        return IS_NOT;
    }

    /* (non-Javadoc)
    * @see com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
    */
    public void decode(FacesContext facesContext, UIComponent component) {
        validateParameters(facesContext, component, SelectInputDate.class);
        SelectInputDate dateSelect = (SelectInputDate) component;
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        Object linkId = getLinkId(facesContext, component);
        Object clickedLink = requestParameterMap.get(linkId);
        String clientId = component.getClientId(facesContext);
//System.out.println("SIDR.decode()  clientId: " + clientId);
        
        Object eventCapturedId = requestParameterMap.get("ice.event.captured");
        String monthClientId = clientId + SELECT_MONTH;
        String yearClientId = clientId + SELECT_YEAR;
        if (monthClientId.equals(eventCapturedId)) {
            dateSelect.setNavEvent(true);
            dateSelect.setNavDate((Date) getConvertedValue(facesContext, component, requestParameterMap.get(monthClientId)));
        }
        else if (yearClientId.equals(eventCapturedId)) {
            dateSelect.setNavEvent(true);
            dateSelect.setNavDate((Date) getConvertedValue(facesContext, component, requestParameterMap.get(yearClientId)));
        }
        else if (clickedLink != null) {
            if (log.isDebugEnabled()) {
                log.debug("linkId::" + linkId + "  clickedLink::" +
                          clickedLink + "  clientId::" + clientId);
            }
            
            String sclickedLink = (String) clickedLink;
            int check = checkLink(sclickedLink, clientId); 
            if (check != IS_NOT) {
                if (log.isDebugEnabled()) {
                    log.debug("---------------------------------");
                    log.debug("----------START::DECODE----------");
                    log.debug("---------------------------------");
                    log.debug("decode::linkId::" + linkId + "=" + clickedLink +
                              " clientId::" + clientId);
                }

                if (check == IS_PREV_MONTH ||
                    check == IS_NEXT_MONTH ||
                    check == IS_PREV_YEAR ||
                    check == IS_NEXT_YEAR) {
                    if (log.isDebugEnabled()) {
                        log.debug("-------------Navigation Event-------------");
                    }
                    decodeNavigation(facesContext, component);
                } else if (check == IS_CALENDAR) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "-------------Select Date Event-------------");
                    }
                    decodeSelectDate(facesContext, component);
                } else if (check == IS_CALENDAR_BUTTON) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "-------------Popup Event-------------------");
                    }
                    decodePopup(facesContext, component);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("-------------InputText enterkey Event ??----");
                }
                boolean enterKeyPressed = false;
                if (requestParameterMap.get("ice.event.target") != null ) {
                    enterKeyPressed =  requestParameterMap.get("ice.event.target")
                    .equals(clientId + SelectInputDate.CALENDAR_INPUTTEXT) && 
                    ("13".equalsIgnoreCase(String.valueOf
                            (requestParameterMap.get("ice.event.keycode"))) ||
                                 "onblur".equalsIgnoreCase(String.valueOf
                                        (requestParameterMap.get("ice.event.type"))));
                }
                decodeInputText(facesContext, component);
                if (enterKeyPressed) {
                    component.queueEvent(new ActionEvent(component));
                }
            }
        }
    }

    private void decodeNavigation(FacesContext facesContext,
                                  UIComponent component) {
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        SelectInputDate dateSelect = (SelectInputDate) component;

        // set the navDate on the Calendar
        if (log.isDebugEnabled()) {
            log.debug("setNavDate::");
            log.debug("#################################");
        }
        dateSelect.setNavEvent(true);
        dateSelect.setNavDate(
            (Date) getConvertedValue(
                facesContext, dateSelect, requestParameterMap.get(
                    dateSelect.getClientId(facesContext) + CALENDAR_CLICK)));
    }

    private void decodePopup(FacesContext facesContext, UIComponent component) {
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String popupState = getHiddenFieldName(facesContext, component);
        String showPopup = (String) requestParameterMap.get(popupState);
        SelectInputDate dateSelect = (SelectInputDate) component;

        if (log.isDebugEnabled()) {
            log.debug("decodePopup::" + showPopup);
            log.debug("#################################");
        }
        // check showPopup
        if (showPopup != null) {

            dateSelect.setShowPopup(!dateSelect.isShowPopup());
        }
        // not a nav event
        dateSelect.setNavEvent(false);
    }

    private void decodeSelectDate(FacesContext facesContext,
                                  UIComponent component) {

        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String popupState = getHiddenFieldName(facesContext, component);
        String showPopup = (String) requestParameterMap.get(popupState);
        SelectInputDate dateSelect = (SelectInputDate) component;

        if (log.isDebugEnabled()) {
            log.debug("selectDate::showPopup" + showPopup);
            log.debug("#################################");
        }
        if (showPopup != null) {
            if (showPopup.equalsIgnoreCase("true")) {
                dateSelect.setShowPopup(true);
            } else {
                dateSelect.setShowPopup(false);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("decodeUIInput::");
            log.debug("#################################");
        }
        String clientId = component.getClientId(facesContext); 
        String inputDateTextId = clientId + SelectInputDate.CALENDAR_INPUTTEXT;
        if (requestParameterMap.containsKey(inputDateTextId)) {
            String inputDateButtonId = component.getClientId(facesContext) +
                CALENDAR_BUTTON;
            ((BridgeFacesContext)facesContext).setFocusId(inputDateButtonId);
        }
        CustomComponentUtils.decodeUIInput(facesContext, component, clientId+CALENDAR_CLICK);
        // not a navigation event
        dateSelect.setNavEvent(false);
    }

    private void decodeInputText(FacesContext facesContext,
                                 UIComponent component) {
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String popupState = getHiddenFieldName(facesContext, component);
        String showPopup = (String) requestParameterMap.get(popupState);
        SelectInputDate dateSelect = (SelectInputDate) component;
        String clientId = dateSelect.getClientId(facesContext);
//System.out.println("SIDR.decodeInputText()  clientId: " + clientId);
        Object linkId = getLinkId(facesContext, component);
        Object clickedLink = requestParameterMap.get(linkId);
        String inputTextDateId = clientId + SelectInputDate.CALENDAR_INPUTTEXT;
//System.out.println("SIDR.decodeInputText()  inputTextDateId: " + inputTextDateId);

        // inputtext is only available in popup mode 
        if (requestParameterMap.containsKey(inputTextDateId)) {
            if (log.isDebugEnabled()) {
                log.debug("decoding InputText EnterKey::");
                log.debug("###################################");
            }
            if (showPopup != null) {
                if (checkLink((String) clickedLink, clientId) != IS_NOT) {
                    if (showPopup.equalsIgnoreCase("true")) {
//System.out.println("SIDR.decodeInputText()  setShowPopup( true )");
                        dateSelect.setShowPopup(true);
                    } else {
//System.out.println("SIDR.decodeInputText()  setShowPopup( false )");
                        dateSelect.setShowPopup(false);
                    }
                }
            }
            Object inputTextDate = requestParameterMap.get(inputTextDateId);
//System.out.println("SIDR.decodeInputText()  inputTextDate: " + inputTextDate);
            if (inputTextDate == null) {
                dateSelect.setSubmittedValue(null);
            }
            else if (String.valueOf(inputTextDate).trim().length() == 0) {
                dateSelect.setSubmittedValue("");
            }
            else {
                dateSelect.setSubmittedValue(inputTextDate);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer#getConvertedValue(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public Object getConvertedValue(FacesContext facesContext,
                                    UIComponent uiComponent,
                                    Object submittedValue)
            throws ConverterException {
        validateParameters(facesContext, uiComponent, SelectInputDate.class);
        
        Converter converter = ((SelectInputDate)uiComponent).resolveDateTimeConverter(facesContext);

        if (!(submittedValue == null || submittedValue instanceof String)) {
            throw new IllegalArgumentException(
                    "Submitted value of type String expected");
        }
        Object o = converter.getAsObject(facesContext, uiComponent,
                (String) submittedValue);
        
        return o;
    }
}
