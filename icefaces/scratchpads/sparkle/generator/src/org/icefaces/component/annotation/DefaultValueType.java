package org.icefaces.component.annotation;

public enum DefaultValueType {
	STRING_LITERAL,
	EXPRESSION,
	UNSET;
	
	public static final DefaultValueType DEFAULT = DefaultValueType.STRING_LITERAL;
}