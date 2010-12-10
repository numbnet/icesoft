package org.icefaces.component.utils;

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
        int argCount = keyValuePairs.length /2;
        StringBuilder localCopy = new StringBuilder();
        for (int idx = 0; idx < argCount; idx ++ ) {
            localCopy.append('"').append( escapeString(keyValuePairs[2*idx])).append('"').
                    append(",").append('"').append( escapeString(keyValuePairs[(2*idx)+1] )).append('"'); 
            localCopy.append(",");
        }
        if (localCopy.length() > 0) {
            localCopy.setLength( localCopy.length() - 1 );
        }
        params.append( localCopy.toString() );
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

    private String escapeString(String value) {
        value = value.replace("\\", "\\\\");
        value = value.replace("\"", "\\\"");
        value = value.replace("/", "\\/");
        value = value.replace("\b", "\\b");
        value = value.replace("\f", "\\f");
        value = value.replace("\n", "\\n");
        value = value.replace("\r", "\\r");
        value = value.replace("\t", "\\t");
        return value;
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
