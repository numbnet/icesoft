/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/
package com.icefaces.project.memory.util;

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
