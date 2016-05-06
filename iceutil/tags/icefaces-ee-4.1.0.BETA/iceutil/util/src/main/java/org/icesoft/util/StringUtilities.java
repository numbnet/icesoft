/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icesoft.util;

import static org.icesoft.util.ObjectUtilities.isNotNull;
import static org.icesoft.util.ObjectUtilities.isNull;
import static org.icesoft.util.PreCondition.checkIfIsNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

public class StringUtilities {
    private static final Logger LOGGER = Logger.getLogger(StringUtilities.class.getName());

    public static String toCamelCase(final String string, final String delimitingRegex, final String delimiter)
    throws PatternSyntaxException {
        StringBuilder _camelCaseStringBuilder = new StringBuilder();
        boolean _first = true;
        for (final String _string : string.split(delimitingRegex)) {
            if (!_first) {
                _camelCaseStringBuilder.append(delimiter);
            } else {
                _first = false;
            }
            _camelCaseStringBuilder.
                append(_string.substring(0, 1).toUpperCase()).append(_string.substring(1).toLowerCase());
        }
        return _camelCaseStringBuilder.toString();
    }

    public static boolean isEmpty(final String string)
    throws NullPointerException {
        return
            // throws NullPointerException
            checkIfIsNotNull(string, "Illegal argument string: null").
                trim().length() == 0;
    }

    public static boolean isNotEmpty(final String string)
    throws NullPointerException {
        return
            // throws NullPointerException
            !isEmpty(string);
    }

    public static boolean isNotNullAndIsNotEmpty(final String string) {
        return isNotNull(string) && isNotEmpty(string);
    }

    public static boolean isNullOrIsEmpty(final String string) {
        return isNull(string) || isEmpty(string);
    }

    public static String join(final String delimiter, final String... strings) {
        StringBuilder _stringBuilder = new StringBuilder();
        boolean _first = true;
        for (final String _string : strings) {
            if (!_first) {
                _stringBuilder.append(delimiter);
            } else {
                _first = false;
            }
            _stringBuilder.append(_string);
        }
        return _stringBuilder.toString();
    }
}
