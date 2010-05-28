package com.personal.memory.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * Filter to only accept valid, readable directories that have two or more images inside it. 
 */
public class DirectoryFilter implements FileFilter {
	public boolean accept(File toCheck) {
		if ((toCheck.isDirectory()) && (toCheck.canRead())) {
			return toCheck.list(new CardImageFilter()).length > 1;
		}
		
		return false;
	}
}
