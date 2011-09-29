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

package org.icefaces.ace.util;

import java.util.ArrayList;

/**
 * Utility API that builds the parameter strings, performs param escaping.
 * Output is a JSON string as specified at <a href="http://www.json.org/">json.org</a>.
 * Based on <a href="http://jira.icefaces.org/browse/ICE-5831">spec.</a> from Mark Collette
 * and <a href="http://yui.yahooapis.com/2.8.1/build/json/json.js"">code from YUI</a>.
 * List of escaped characters can be found at <a href="http://www.json.org/">json.org</a>.
 */
public class JSONBuilder {
    private StringBuilder params = new StringBuilder();

    /**
     * Makes a new instance of JSONBuilder.
     * @return a reference to this object.
     */
    public static JSONBuilder create() {
        return new JSONBuilder();
    }

    /**
     * Begins an anonymous object.
     * @return a reference to this object.
     */
    public JSONBuilder beginMap() {
        params.append("{");
        return this;
    }

    /**
     * Begins a named object.
     * @param key name of the object.
     * @return a reference to this object.
     */
    public JSONBuilder beginMap(String key) {
        appendCommaAndKey(key);
        return beginMap();
    }

    /**
     * Ends an object.
     * @return a reference to this object.
     */
    public JSONBuilder endMap() {
        params.append("}");
        return this;
    }

    public JSONBuilder beginArray(String key) {
        appendCommaAndKey(key);
        params.append("[");
        return this;
    }

    public JSONBuilder endArray() {
        params.append("]");
        return this;
    }

    /**
     * Adds an int property.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, int value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a long property.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, long value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a float property.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, float value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a double property.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, double value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a boolean property.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, boolean value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Append a key bound String array that is itself a set of key-value pairs.
     * Even array indexes = key, odd array index  = values
     * @param key overall key to put entry under
     * @param keyValuePairs Array of key value pair string entries 
     * @return The builder object 
     */
    public JSONBuilder entry (String key, String[] keyValuePairs) {
        beginArray(key);
        int len = keyValuePairs.length;
        for (int idx = 0; idx < len; idx ++) {
            String curr = keyValuePairs[idx];
            if (curr == null) {
                params.append("null");
            }
            else {
                params.append('"').append(escapeString(curr)).append('"');
            }
            if (idx < (len-1)) {
                params.append(',');
            }
        }
        endArray();
        return this; 
    }

    /**
     * Adds a String property.
     * Adds quotes and does JSON string escaping, as described at <a href="http://www.json.org/">json.org</a>. 
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, String value) {
        return entry(key, value, false);
    }
    
    /**
     * Adds a String property as String literal optionally.
     * Adds quotes and does JSON string escaping, as described at <a href="http://www.json.org/">json.org</a>. 
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, String value, boolean isStringLiteral) {
        appendCommaAndKey(key);
        if (isStringLiteral) {
        	params.append(value);   
        } else {
            value = escapeString(value);
	    	params.append('"').append(value).append('"');     
        }
        return this;
    }

    public JSONBuilder item(String value) {
        conditionallyAppendComma();
        value = escapeString(value);
        params.append('"').append(value).append('"');
        return this;
    }


    public static String escapeString(String value) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int idx = 0; idx < value.length(); idx++) {
            c = value.charAt(idx);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Outputs the JSON string.
     * @return the JSON string.
     */
    public String toString() {
        return params.toString();
    }

    private void appendCommaAndKey(String key) {
        conditionallyAppendComma();
        params.append('"').append(key).append('"').append(":");
    }

    private void conditionallyAppendComma() {
        char lastChar = params.charAt(params.length() - 1);
        if (lastChar != '{' && lastChar != '[') {
            params.append(",");
        }
    }
}
