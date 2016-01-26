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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapUtilities {
    private static final Logger LOGGER = Logger.getLogger(MapUtilities.class.getName());

    public static <M extends Map<?, ?>> boolean isEmpty(final M map)
    throws NullPointerException {
        return
            // throws NullPointerException
            checkIfIsNotNull(map).
                isEmpty();
    }

    public static <M extends Map<?, ?>> boolean isNotEmpty(final M map)
    throws NullPointerException {
        return
            // throws NullPointerException
            !isEmpty(map);
    }

    public static <M extends Map<?, ?>> boolean isNotNullAndIsNotEmpty(final M map) {
        return isNotNull(map) && isNotEmpty(map);
    }

    public static <M extends Map<?, ?>> boolean isNullOrIsEmpty(final M map) {
        return isNull(map) || isEmpty(map);
    }
}
