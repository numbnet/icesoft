/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.application;

public class StartupTime {

    private static long started = System.currentTimeMillis();;
    private static String inc = "/" + started + "/";

    public static long getStartupTime() {
        return started;
    }

    public static String getStartupInc() {
        return inc;
    }

    public static String removeStartupTimeFromPath(String path) {
        return getPath(path, inc);
    }

    private static String getPath(String path, String inc) {
        int start = path.indexOf(inc);
        if (start == -1) return path;
        int end = start + inc.length() - 1;
        if (start > 0) {
            String stringStart = path.substring(0, start);
            String stringEnd = path.substring(end);
            path = stringStart + stringEnd;
        } else {
            path = path.substring(end);
        }
        return path;
    }
}
