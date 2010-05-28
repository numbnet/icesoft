package com.icefaces.project.memory.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converter to format an integer grid size into YxY, for example 4 becomes "4x4".
 */
public class GridSizeFormatter implements Converter {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
		return newValue;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		if (value != null) {
			Integer baseSize = (Integer)value;
			
			return baseSize + "x" + baseSize;
		}
		
		return "?";
	}
}
