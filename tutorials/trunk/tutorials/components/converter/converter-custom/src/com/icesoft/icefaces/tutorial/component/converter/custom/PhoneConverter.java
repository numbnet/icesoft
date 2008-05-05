package com.icesoft.icefaces.tutorial.component.converter.custom;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.lang.StringUtils;

/*
 * User defined converter for phone numbers.  Converts a String into a
 * PhoneNumber onject instance.  Also converts a PhoneNumber instance
 * to a String.
 */
public class PhoneConverter implements Converter{



    public Object getAsObject(FacesContext context, UIComponent component,
            String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }

        PhoneNumber phone = new PhoneNumber();

        String [] phoneComps = StringUtils.split(value," ,()-");

        String countryCode = phoneComps[0];

        phone.setCountryCode(countryCode);

        if(countryCode.equals("1")){
            String areaCode = phoneComps[1];
            String prefix = phoneComps[2];
            String number = phoneComps[3];
            phone.setAreaCode(areaCode);
            phone.setPrefix(prefix);
            phone.setNumber(number);
        }
        else{
            phone.setNumber(value);
        }

        return phone;

    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value){
        if(value == null){
		    return "";
		}
        return value.toString();
    }




}
