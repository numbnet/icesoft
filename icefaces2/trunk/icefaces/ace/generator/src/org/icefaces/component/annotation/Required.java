package org.icefaces.component.annotation;

public enum Required {
	yes,
	no,
	UNSET;
	
	public static final Required DEFAULT = Required.no;
}