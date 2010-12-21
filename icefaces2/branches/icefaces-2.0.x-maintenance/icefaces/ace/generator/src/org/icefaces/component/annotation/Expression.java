package org.icefaces.component.annotation;

public enum Expression {
	METHOD_EXPRESSION,
	VALUE_EXPRESSION,
	UNSET;
	
	public static final Expression DEFAULT = Expression.VALUE_EXPRESSION;
}