package org.icefaces.component.utils;

public class JSONBuilder {
    private StringBuilder params = new StringBuilder();

    public static JSONBuilder create() {
        return new JSONBuilder();
    }

    public JSONBuilder beginMap() {
        params.append("{");
        return this;
    }

    public JSONBuilder beginMap(String key) {
        appendCommaAndKey(key);
        return beginMap();
    }

    public JSONBuilder endMap() {
        params.append("}");
        return this;
    }

    public JSONBuilder entry(String key, int value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    public JSONBuilder entry(String key, long value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    public JSONBuilder entry(String key, float value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    public JSONBuilder entry(String key, double value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    public JSONBuilder entry(String key, boolean value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    public JSONBuilder entry(String key, String value) {
        appendCommaAndKey(key);
        value = value.replace("\\", "\\\\");
        value = value.replace("\"", "\\\"");
        value = value.replace("/", "\\/");
        value = value.replace("\b", "\\b");
        value = value.replace("\f", "\\f");
        value = value.replace("\n", "\\n");
        value = value.replace("\r", "\\r");
        value = value.replace("\t", "\\t");
        params.append('"').append(value).append('"');
        return this;
    }

    public String toString() {
        return params.toString();
    }

    private void appendCommaAndKey(String key) {
        if (params.charAt(params.length() - 1) != '{') params.append(",");
        params.append('"').append(key).append('"').append(":");
    }
}
