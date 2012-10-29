package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.util.Validator;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpMessageHeaderValueValidator
implements Validator {
    private static final Log LOG = LogFactory.getLog(HttpMessageHeaderValueValidator.class);

    /*
     * According to RFC 2616:
     *
     *        OCTET          = <any 8-bit sequence of data>
     *        CHAR           = <any US-ASCII character (octets 0 - 127)>
     *        CTL            = <any US-ASCII control character
     *                         (octets 0 - 31) and DEL (127)>
     *        CR             = <US-ASCII CR, carriage return (13)>
     *        LF             = <US-ASCII LF, linefeed (10)>
     *        SP             = <US-ASCII SP, space (32)>
     *        HT             = <US-ASCII HT, horizontal-tab (9)>
     *        CRLF           = CR LF
     *        LWS            = [CRLF] 1*( SP | HT )
     *        TEXT           = <any OCTET except CTLs,
     *                         but including LWS>
     *        token          = 1*<any CHAR except CTLs or separators>
     *        separators     = "(" | ")" | "<" | ">" | "@"
     *                       | "," | ";" | ":" | "\" | <">
     *                       | "/" | "[" | "]" | "?" | "="
     *                       | "{" | "}" | SP | HT
     *        quoted-string  = ( <"> *(qdtext | quoted-pair ) <"> )
     *        qdtext         = <any TEXT except <">>
     *        quoted-pair    = "\" CHAR
     *        field-value    = *( field-content | LWS )
     *        field-content  = <the OCTETs making up the field-value
     *                         and consisting of either *TEXT or combinations
     *                         of token, separators, and quoted-string>
     *
     * This is a simple pattern matches that checks for the following:
     *
     *     any OCTET except CTLs, but including SP and HT
     */
    private static final Pattern PATTERN = Pattern.compile("[\\x09\\x20-\\x7e\\x80-\\xff]*");

    public boolean isValid(final Object object) {
        return
            object instanceof String &&
            PATTERN.matcher((String)object).matches();
    }

    public static void main(final String[] arguments) {
        if (!new HttpMessageHeaderValueValidator().isValid("Value")) {
            LOG.info("HTTP Message Header Value contains invalid characters: [Value\r\nX-My-Header: Value]");
        }
    }
}
