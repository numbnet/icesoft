package com.icesoft.icefaces.samples.showcase.components.inputrichtext;
import javax.faces.event.ValueChangeEvent;


public class InputRichTextBean {
	private String value = "";

	public void valueChange(ValueChangeEvent event) {
		System.out.println("Value has been changed");
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
