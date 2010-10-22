package org.icefaces.component.annotation;

public enum Required {
	REQUIRED,
	OPTIONAL,
	UNSET;
	
	public static final Required DEFAULT = Required.OPTIONAL;
}