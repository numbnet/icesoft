/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.faces.presenter.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Convenience class used to provide various common manipulation methods and
 * functionality
 */
public class StringResource {
    /**
     * Method to trim a String when it exceeds the maximum length allowed in
     * the UI
     *
     * @param trim      to clean
     * @param maxLength desired length
     * @return String the trimmed String
     */
    public static String trimLength(String trim, int maxLength) {
        if (trim.length() > maxLength) {
            trim = (trim.substring(0, maxLength) + "...");
        }
        return (trim);
    }

    /**
     * Method to remove any tags the user may attempt to input from the UI An
     * example of a troublesome tag would be a closing div tag as a nickname
     *
     * @param clean to remove tags from
     * @return tagless result
     */
    public static String cleanTags(String clean) {
        clean = clean.replaceAll("<", "&lt;");
        clean = clean.replaceAll(">", "&gt;");
        clean = clean.replaceAll("&", "&amp;");
        return (clean);
    }

    /**
     * Method to recognize URLs in a String and pad them with an href link tag
     *
     * @param pad string to check
     * @return padded string (or original, if it is not a url)
     */
    public static String urlRecognize(String pad) {
        int indexHttp = pad.indexOf("http://");
        int indexWww = pad.indexOf("www.");
        int index = -1;

        // Quick check to see if pad could possibly be a URL
        if ((indexHttp == -1) && (indexWww == -1)) {
            return pad;
        }

        // Grab the index to use
        if (indexWww != -1) {
            // Make sure the link has more than just 'www.'
            if (pad.length() <= 4) {
                return pad;
            }
            index = indexWww;
        }
        if (indexHttp != -1) {
            // Make sure the link has more than just 'http://'
            if (pad.length() <= 7) {
                return pad;
            }
            index = indexHttp;
        }

        // Variables for possible text before / after the URL
        String before;
        String after = "";
        String original = pad;

        // Try to cast pad as a URL
        URL checker;

        try {
            // Get the index of the first space after the URL
            int spaceIndex = pad.indexOf(" ", index);
            before = pad.substring(0, index);

            if (spaceIndex != -1) {
                after = pad.substring(spaceIndex, pad.length());

                //Recursively call to identify more than one URL
                after = urlRecognize(after);
            }

            if (spaceIndex != -1) {
                pad = pad.substring(index, spaceIndex);
            } else {
                pad = pad.substring(index);
            }

            // Ensure an http:// is present on the front, otherwise the URL creation will fail
            if (pad.indexOf("http://") == -1) {
                pad = "http://" + pad;
            }

            checker = new URL(pad);
        } catch (MalformedURLException mue) {
            return original;
        }

        // Hooray, it's a valid URL, so add html link tags and return
        pad = checker.toString();
        pad = ("<A href=\"" + pad + "\" target=\"_blank\">" + pad + "</A>");
        pad = before + pad + after;
        return (pad);
    }

    /**
     * Method to fix the case of the passed string This will basically ensure
     * only the first letter of each word is capitalized
     *
     * @param inString to fix capitalization on
     * @return capitalized result
     */
    public static String fixCapitalization(String inString) {
        StringBuffer str = new StringBuffer(inString.trim().toLowerCase());

        if (str.length() == 0) {
            return str.toString();
        }

        Character nextChar;
        int i = 0;
        nextChar = new Character(str.charAt(i));
        while (i < str.length()) {
            str.setCharAt(i++, Character.toUpperCase(nextChar.charValue()));

            if (i == str.length()) {
                return str.toString();
            }

            // Look for whitespace
            nextChar = new Character(str.charAt(i));
            while (i < str.length() - 2 &&
                   !Character.isWhitespace(nextChar.charValue())) {
                nextChar = new Character(str.charAt(++i));
            }

            if (!Character.isWhitespace(nextChar.charValue())) {
                // If not whitespace, we must be at end of string
                return str.toString();
            }

            // Remove all but first whitespace
            nextChar = new Character(str.charAt(++i));
            while (i < str.length() &&
                   Character.isWhitespace(nextChar.charValue())) {
                str.deleteCharAt(i);
                nextChar = new Character(str.charAt(i));
            }
        }

        return str.toString();
    }
}
