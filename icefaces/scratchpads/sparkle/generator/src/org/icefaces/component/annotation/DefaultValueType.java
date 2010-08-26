package org.icefaces.component.annotation;

public enum DefaultValueType {
	STRING_LITERAL,
	EXPRESSION,
	UNSET;
	
	public static final DefaultValueType DEFAULT_DEFAULT_VALUE_TYPE = DefaultValueType.STRING_LITERAL;
}