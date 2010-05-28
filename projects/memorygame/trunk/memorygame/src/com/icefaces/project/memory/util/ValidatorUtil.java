package com.personal.memory.util;

import java.io.File;
import java.util.List;

/**
 * Utility class to manage the validation of strings, directories, etc.
 */
public class ValidatorUtil {
	/**
	 * Return true if the passed String is not null and not empty
	 * 
	 * @param toCheck
	 * @return based on validation
	 */
	public static boolean isValidString(String toCheck) {
		return (toCheck != null) && (toCheck.trim().length() > 0);
	}
	
	/**
	 * Return true if the passed List is not null and not empty
	 * 
	 * @param toCheck
	 * @return based on validation
	 */
	public static boolean isValidList(List<?> toCheck) {
		return (toCheck != null) && (toCheck.size() > 0);
	}
	
	/**
	 * Return true if the passed File is not null, exists, is a directory, and is readable
	 * 
	 * @param toCheck
	 * @return based on validation
	 */
	public static boolean isValidDirectory(File toCheck) {
		return (toCheck != null) && (toCheck.exists()) &&
		       (toCheck.isDirectory()) && (toCheck.canRead());
	}
}
