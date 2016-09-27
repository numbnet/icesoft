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

import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectUtilities {
    private static final Logger LOGGER = Logger.getLogger(ObjectUtilities.class.getName());

    public static boolean isEqual(final Object object1, final Object object2) {
        return
             (object1 == null && object2 == null) ||
             (object1 != null && object1.equals(object2));
    }

    public static boolean isNotEqual(final Object object1, final Object object2) {
        return !isEqual(object1, object2);
    }

    public static boolean isNotNull(final Object object) {
        return !isNull(object);
    }

    public static boolean isNull(final Object object) {
        return object == null;
    }
}
