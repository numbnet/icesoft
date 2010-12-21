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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.context.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *Utility functions for effect
 */
public class EffectUtil {

    /**
     * build up a map from a css string
     * key:value;
     * @param css
     * @return
     */
    public static Map buildCssMap(String css) {
        Map map = new HashMap();
        if (css == null || css.trim().length() == 0)
            return map;
        css = css.trim();
        StringTokenizer st = new StringTokenizer(css, ";");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int i = token.indexOf(":");
            if (i == 0) throw new IllegalArgumentException(
                    "Can't find : in [" + token + "]");
            String name = token.substring(0, i);
            String value = token.substring(++i);
            map.put(name, value);
        }
        return map;
    }
}
