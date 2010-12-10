package org.icefaces.component.annotation;

public enum Implementation {
	EXISTS_IN_SUPERCLASS,
	GENERATE,
	UNSET;
	
	public final static Implementation DEFAULT = Implementation.GENERATE;
}