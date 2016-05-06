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

import static org.icesoft.util.CollectionUtilities.isNotEmpty;
import static org.icesoft.util.CollectionUtilities.isNotNullAndIsNotEmpty;
import static org.icesoft.util.MapUtilities.isNotEmpty;
import static org.icesoft.util.MapUtilities.isNotNullAndIsNotEmpty;
import static org.icesoft.util.ObjectUtilities.isNotNull;
import static org.icesoft.util.StringUtilities.isNotEmpty;
import static org.icesoft.util.StringUtilities.isNotNullAndIsNotEmpty;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreCondition {
    private static final Logger LOGGER = Logger.getLogger(PreCondition.class.getName());

    public static void checkArgument(final boolean result)
    throws IllegalArgumentException {
        if (!result) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(final boolean result, final String message)
    throws IllegalArgumentException {
        if (!result) {
            throw new IllegalArgumentException(message);
        }
    }

    public static int checkIfIsGreaterThan(
        final int number, final int boundary)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static int checkIfIsGreaterThan(
        final int number, final int boundary, final int defaultNumber, final boolean checkDefault)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else if (!checkDefault || defaultNumber > boundary) {
            return defaultNumber;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static int checkIfIsGreaterThan(
        final int number, final int boundary, final int defaultNumber, final boolean checkDefault,
        final String message)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else if (!checkDefault || defaultNumber > boundary) {
            return defaultNumber;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static int checkIfIsGreaterThan(
        final int number, final int boundary, final String message)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static long checkIfIsGreaterThan(
        final long number, final long boundary)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static long checkIfIsGreaterThan(
        final long number, final long boundary, final long defaultNumber, final boolean checkDefault)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else if (!checkDefault || defaultNumber > boundary) {
            return defaultNumber;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static long checkIfIsGreaterThan(
        final long number, final long boundary, final long defaultNumber, final boolean checkDefault,
        final String message)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else if (!checkDefault || defaultNumber > boundary) {
            return defaultNumber;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static long checkIfIsGreaterThan(
        final long number, final long boundary, final String message)
    throws IllegalArgumentException {
        if (number > boundary) {
            return number;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotEmpty(
        final C collection)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(collection)) {
            return collection;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotEmpty(
        final C collection, final C defaultCollection, final boolean checkDefault)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(collection)) {
            return collection;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultCollection)) {
            return defaultCollection;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotEmpty(
        final C collection, final C defaultCollection, final boolean checkDefault, final String message)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(collection)) {
            return collection;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultCollection)) {
            return defaultCollection;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotEmpty(
        final C collection, final String message)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(collection)) {
            return collection;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotEmpty(
        final M map)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(map)) {
            return map;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotEmpty(
        final M map, final M defaultMap, final boolean checkDefault)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(map)) {
            return map;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultMap)) {
            return defaultMap;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotEmpty(
        final M map, final M defaultMap, final boolean checkDefault, final String message)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(map)) {
            return map;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultMap)) {
            return defaultMap;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotEmpty(
        final M map, final String message)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(map)) {
            return map;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static String checkIfIsNotEmpty(
        final String string)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(string)) {
            return string;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String checkIfIsNotEmpty(
        final String string, final String defaultString, final boolean checkDefault)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(string)) {
            return string;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultString)) {
            return defaultString;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String checkIfIsNotEmpty(
        final String string, final String defaultString, final boolean checkDefault,
       final String message)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(string)) {
            return string;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultString)) {
            return defaultString;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static String checkIfIsNotEmpty(
        final String string, final String message)
    throws IllegalArgumentException, NullPointerException {
        // throws NullPointerException
        if (isNotEmpty(string)) {
            return string;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <O> O checkIfIsNotNull(
        final O object)
    throws NullPointerException {
        if (isNotNull(object)) {
            return object;
        } else {
            throw new NullPointerException();
        }
    }

    public static <O> O checkIfIsNotNull(
        final O object, final O defaultObject, final boolean checkDefault)
    throws NullPointerException {
        if (isNotNull(object)) {
            return object;
        } else if (!checkDefault || isNotNull(defaultObject)) {
            return defaultObject;
        } else {
            throw new NullPointerException();
        }
    }

    public static <O> O checkIfIsNotNull(
        final O object, final O defaultObject, final boolean checkDefault, final String message)
    throws NullPointerException {
        if (isNotNull(object)) {
            return object;
        } else if (!checkDefault || isNotNull(defaultObject)) {
            return defaultObject;
        } else {
            throw new NullPointerException(message);
        }
    }

    public static <O> O checkIfIsNotNull(
        final O object, final String message)
    throws NullPointerException {
        if (isNotNull(object)) {
            return object;
        } else {
            throw new NullPointerException(message);
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotNullAndIsNotEmpty(
        final C collection)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(collection)) {
            return collection;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotNullAndIsNotEmpty(
        final C collection, final C defaultCollection, final boolean checkDefault)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(collection)) {
            return collection;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultCollection)) {
            return defaultCollection;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotNullAndIsNotEmpty(
        final C collection, final C defaultCollection, final boolean checkDefault, final String message)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(collection)) {
            return collection;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultCollection)) {
            return defaultCollection;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <C extends Collection<?>> C checkIfIsNotNullAndIsNotEmpty(
        final C collection, final String message)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(collection)) {
            return collection;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotNullAndIsNotEmpty(
        final M map)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(map)) {
            return map;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotNullAndIsNotEmpty(
        final M map, final M defaultMap, final boolean checkDefault)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(map)) {
            return map;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultMap)) {
            return defaultMap;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotNullAndIsNotEmpty(
        final M map, final M defaultMap, final boolean checkDefault, final String message)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(map)) {
            return map;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultMap)) {
            return defaultMap;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static <M extends Map<?, ?>> M checkIfIsNotNullAndIsNotEmpty(
        final M map, final String message)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(map)) {
            return map;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static String checkIfIsNotNullAndIsNotEmpty(
        final String string)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(string)) {
            return string;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String checkIfIsNotNullAndIsNotEmpty(
        final String string, final String defaultString, final boolean checkDefault)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(string)) {
            return string;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultString)) {
            return defaultString;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String checkIfIsNotNullAndIsNotEmpty(
        final String string, final String defaultString, final boolean checkDefault,
       final String message)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(string)) {
            return string;
        } else if (!checkDefault || isNotNullAndIsNotEmpty(defaultString)) {
            return defaultString;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    public static String checkIfIsNotNullAndIsNotEmpty(
        final String string, final String message)
    throws IllegalArgumentException {
        if (isNotNullAndIsNotEmpty(string)) {
            return string;
        } else {
            throw new IllegalArgumentException(message);
        }
    }
}
