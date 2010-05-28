package com.icefaces.project.memory.filter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter to only accept .png, .jpg. and .gif files.
 */
public class CardImageFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.toLowerCase().endsWith(".png")) ||
		       (name.toLowerCase().endsWith(".jpg")) ||
		       (name.toLowerCase().endsWith(".gif"));
	}
}
