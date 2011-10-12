/*
 * Copyright 2009-2011 Prime Technology.
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.icefaces.component.dateentry;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.icefaces.component.utils.BaseInputRenderer;
import org.icefaces.component.utils.HTML;

public class DateEntryRenderer extends BaseInputRenderer {
    private static Logger logger = Logger.getLogger(DateEntryRenderer.class.getName());

    @Override
    public void decode(FacesContext context, UIComponent component) {
        DateEntry dateEntry = (DateEntry) component;
        String clientId = dateEntry.getClientId(context);
        if(dateEntry.isDisabled() || dateEntry.isReadonly()) {
            return;
        }

        String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId);
        logger.info("dateEntry submittedValue="+submittedValue);
        if(submittedValue != null) {
            dateEntry.setSubmittedValue(submittedValue);
        }

       // decodeBehaviors(context, dateEntry);
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        DateEntry dateEntry = (DateEntry) component;
        String value = DateEntryUtils.getValueAsString(facesContext, dateEntry);
        String clientId = dateEntry.getClientId(facesContext);

        encodeMarkup(facesContext, dateEntry, value);
        encodeScript(facesContext, dateEntry);
    }

    protected void encodeMarkup(FacesContext context, UIComponent uiComponent, String value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        DateEntry dateEntry = (DateEntry) uiComponent;
        String clientId = dateEntry.getClientId(context);

        String type="text";  //no proper keyboard popup yet and don't want the html5 date format

        writer.startElement("input", uiComponent);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", type, null);
        writer.writeAttribute("onfocus","blur", null);
        StringBuilder baseClass = new StringBuilder("tcal");
        String styleClass = dateEntry.getStyleClass();
        if (styleClass != null) {
            baseClass.append(" ").append(styleClass);
        }
        writer.writeAttribute("class", baseClass.toString(), null);
        if (dateEntry.getStyle()!=null){
            writer.writeAttribute("style", dateEntry.getStyle(), null);
        }
        String valueString="";
        if(!isValueBlank(value)) {
            valueString = DateEntryUtils.getValueAsString(context,dateEntry);
        }
        writer.writeAttribute(HTML.VALUE_ATTR, valueString, HTML.VALUE_ATTR);

        if(dateEntry.isReadonly()) writer.writeAttribute("readonly", "readonly", null);
        if(dateEntry.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        boolean singleSubmit = dateEntry.isSingleSubmit();
        if (singleSubmit) {
            writer.writeAttribute("onchange", "ice.se(event,'"+clientId+"');", null);
        }

        writer.endElement("input");

    }

    protected void encodeScript(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ResponseWriter writer= facesContext.getResponseWriter();
        DateEntry dateEntry = (DateEntry) uiComponent;
        Map contextMap = facesContext.getAttributes();
        String deviceType = "ipad";  //default
        String prefix = "tcal";
        if (null != contextMap.get("device")) {
            deviceType = contextMap.get("device").toString();
        }
        // so far only have iphone and ipad defined
     //   if (deviceType.equals("iphone"))prefix="iphone";
        if (dateEntry.getStringPrefix()!=null)prefix = dateEntry.getStringPrefix();
        boolean yrscroll = dateEntry.isYearscroll();
        int wkstrt = dateEntry.getWeekstart();
        String dtFormat = dateEntry.getFormat();
        if (!dtFormat.equals(dateEntry.TCAL_FMT_Ymd) && !dtFormat.equals(dateEntry.TCAL_FMT_dmY) &&
            !dtFormat.equals(dateEntry.TCAL_FMT_LONG)){
            dtFormat = dateEntry.TCAL_FMT_dmY;
        }
        writer.startElement("script", null);
        writer.writeAttribute("text", "text/javascript", null);
        writer.write("calendarTemplate('"+prefix+"',"+yrscroll+","+wkstrt+",'"+dtFormat+"');");

        writer.endElement("script");
    }



    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object value) throws ConverterException {
        DateEntry dateEntry = (DateEntry) component;
        String submittedValue = String.valueOf(value);
        logger.info("converting string="+submittedValue);
        if (dateEntry.getFormat().equals(dateEntry.TCAL_FMT_LONG)){
            //Friday, October 21st 2011
            //Tuesday, April 12, 1952 AD
            logger.info("need to convert long");
            dateEntry.setPattern("EEEEEEEEEE, MMMMMMMMMM dd yyyy");
        }
        else if (dateEntry.getFormat().equals(dateEntry.TCAL_FMT_dmY )) {
             logger.info("converting dmY");
            dateEntry.setPattern("dd-MM-yyyy");
        } else if (dateEntry.getFormat().equals(dateEntry.TCAL_FMT_Ymd)) {
            logger.info("converting Ymd");
            dateEntry.setPattern("yyyy-MM-dd");
        }
        else{
            //warning to use own conversion???
        }
        Converter converter = dateEntry.getConverter();
        if(isValueBlank(submittedValue)) {
            return null;
        }

        //Delegate to user supplied converter if defined
        if(converter != null) {
            return converter.getAsObject(context, dateEntry, submittedValue);
        }

        //Use built-in converter
        try {
            Date convertedValue;
            Locale locale = dateEntry.calculateLocale(context);
            SimpleDateFormat format = new SimpleDateFormat(dateEntry.getPattern(), locale);
            format.setTimeZone(dateEntry.calculateTimeZone());
            convertedValue = format.parse(submittedValue);
            logger.info(" convertedValue = "+convertedValue.toString());
            
            return convertedValue;

        } catch (ParseException e) {
            throw new ConverterException(e);
        }
    }

    public boolean isValueBlank(String value) {
		if(value == null)
			return true;

		return value.trim().equals("");
	}
}

