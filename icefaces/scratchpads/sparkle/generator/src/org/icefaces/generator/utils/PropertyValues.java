package org.icefaces.generator.utils;

import org.icefaces.component.annotation.*;

public class PropertyValues {

	public PropertyValues() {
	
	}
	
	public Expression expression = Expression.UNSET;
	public String methodExpressionArgument = Property.Null;
	public String defaultValue = Property.Null;
	public DefaultValueType defaultValueType = DefaultValueType.UNSET;
	public String tlddoc = Property.Null;
	public String javadocGet = Property.Null;
	public String javadocSet = Property.Null;
	public Required required = Required.UNSET;

	public Implementation implementation = Implementation.UNSET;

	// flag to indicate that the property in question was first defined in a superclass
	public boolean overrides = false;
	
	// flag to indicate that only delegating getter and setter methods should be generated and no state staving code
	public boolean isDelegatingProperty = false;
}