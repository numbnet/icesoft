package org.icefaces.component.annotation;

public enum Inherit {
	SUPERCLASS_PROPERTY,
	LOCAL_PROPERTY,
	UNSET;
	
	public final static Inherit DEFAULT = Inherit.LOCAL_PROPERTY;
}