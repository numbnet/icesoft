package org.icepush.integration.icepushplace.shared;

import java.util.List;

/**
 * Utility class to manage the validation of strings, lists, etc.
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
}
