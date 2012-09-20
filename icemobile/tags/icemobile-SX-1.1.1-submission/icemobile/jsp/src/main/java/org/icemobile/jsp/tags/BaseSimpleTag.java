package org.icemobile.jsp.tags;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public abstract class BaseSimpleTag  extends SimpleTagSupport {
	
	protected boolean disabled;
	protected String style;
	protected String styleClass;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
