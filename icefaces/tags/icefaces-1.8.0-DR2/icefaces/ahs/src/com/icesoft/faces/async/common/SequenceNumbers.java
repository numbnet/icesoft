package com.icesoft.faces.async.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SequenceNumbers {
    private static final Log LOG = LogFactory.getLog(SequenceNumbers.class);

    private final Map sequenceNumberMap = new HashMap();

    public SequenceNumbers(final String[] xWindowCookieValues) {
        if (xWindowCookieValues != null && xWindowCookieValues.length != 0) {
            for (int i = 0; i < xWindowCookieValues.length; i++) {
                StringTokenizer _values =
                    new StringTokenizer(xWindowCookieValues[i], ";");
                while (_values.hasMoreTokens()) {
                    String _value = _values.nextToken().trim();
                    if (_value.startsWith("Sequence_Numbers")) {
                        StringTokenizer _sequenceNumbers =
                            new StringTokenizer(
                                _value.substring(
                                    _value.indexOf("\"") + 1,
                                    _value.lastIndexOf("\"")),
                                ",");
                        while (_sequenceNumbers.hasMoreTokens()) {
                            String _token =
                                _sequenceNumbers.nextToken();
                            int _index = _token.indexOf(':');
                            try {
                                String _iceFacesId =
                                    _token.substring(0, _index);
                                Long _sequenceNumber =
                                    Long.valueOf(
                                        _token.substring(_index + 1));
                                sequenceNumberMap.put(
                                    _iceFacesId, _sequenceNumber);
                            } catch (NumberFormatException exception) {
                                // do nothing.
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public Long get(final String iceFacesId) {
        return (Long)sequenceNumberMap.get(iceFacesId);
    }

    public boolean isEmpty() {
        return sequenceNumberMap.isEmpty();
    }

    public String toString() {
        StringBuffer _string = new StringBuffer();
        Iterator _entries = sequenceNumberMap.entrySet().iterator();
        while (_entries.hasNext()) {
            Map.Entry _entry = (Map.Entry)_entries.next();
            if (_string.length() != 0) {
                _string.append(",");
            }
            _string.
                append(_entry.getKey()).append(":").append(_entry.getValue());
        }
        return _string.toString();
    }
}
