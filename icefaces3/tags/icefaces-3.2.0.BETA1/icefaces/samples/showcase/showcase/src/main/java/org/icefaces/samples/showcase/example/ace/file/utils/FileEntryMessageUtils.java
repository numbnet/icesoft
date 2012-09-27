package org.icefaces.samples.showcase.example.ace.file.utils;

import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "messageUtils")
@SessionScoped
public class FileEntryMessageUtils {
	private static ResourceBundle messages = ResourceBundle.getBundle("org.icefaces.samples.showcase.view.resources.messages");

	public static String getMessage(String key) { return messages.getString(key); }
}
