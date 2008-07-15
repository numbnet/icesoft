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
 */
package com.icesoft.net.http;

import com.icesoft.net.HeaderMap;
import com.icesoft.util.io.LazyCachedInputStream;
import com.icesoft.util.io.MarkerInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>HttpMessage</code> is an abstract class, which functions as the
 *   base class for HTTP messages containing the common functionality. It is
 *   responsible for all the HTTP header management and the
 *   <code>{@link EntityBody}</code>.
 * </p>
 * <p>
 *   Additionally, it has all the HTTP 1.0 and 1.1 general header, as well as a
 *   commonly used extension general header constant, and HTTP version constants
 *   as a convenience and uniform use of these HTTP elements.
 * </p>
 * <p>
 *   Finally, it contains a couple of convenience methods, namely
 *   <code>getBytes()</code>, <code>getLength()</code> and
 *   <code>getMessage()</code> for byte related tasks with HTTP messages.
 * </p>
 * <p>
 *   Subclasses have to implement at least the only abstract method
 *   <code>getMessage(boolean)</code> for the actual representation of the
 *   specific HTTP message.
 * </p>
 * <p>
 *   For detailed information on the HyperText Transfer Protocol and some of its
 *   extensions see:
 *   <ul>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc1945.txt"
 *          target="_top">RFC 1945</a> - "Hypertext Transfer Protocol -- HTTP/1.0"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2068.txt"
 *          target="_top">RFC 2068</a> - "Hypertext Transfer Protocol -- HTTP/1.1"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2109.txt"
 *          target="_top">RFC 2109</a> - "HTTP State Management Mechanism"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2616.txt"
 *          target="_top">RFC 2616</a> - "Hypertext Transfer Protocol -- HTTP/1.1"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2965.txt"
 *          target="_top">RFC 2965</a> - "HTTP State Management Mechanism"
 *     </li>
 *   </ul>
 * </p>
 */
public abstract class HttpMessage
implements Cloneable, Serializable {
    /** HTTP 1.1 general header: Cache-Control. */
    public static final String CACHE_CONTROL = "Cache-Control";

    /** HTTP 1.1 general header: Connection. */
    public static final String CONNECTION = "Connection";

    /** HTTP 1.0 general header: Date. */
    public static final String DATE = "Date";

    /** HTTP 1.0 general header: Pragma. */
    public static final String PRAGMA = "Pragma";

    /** HTTP 1.1 general header: Trailer. */
    public static final String TRAILER = "Trailer";

    /** HTTP 1.1 general header: Transfer-Encoding. */
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";

    /** HTTP 1.1 general header: Upgrade. */
    public static final String UPGRADE = "Upgrade";

    /** HTTP 1.1 general header: Via. */
    public static final String VIA = "Via";

    /** HTTP 1.1 general header: Warning. */
    public static final String WARNING = "Warning";

    /** HTTP general header (extension): Keep-Alive. */
    public static final String KEEP_ALIVE = "Keep-Alive";

    /** HTTP general header (extension): Proxy-Connection. */
    public static final String PROXY_CONNECTION  = "Proxy-Connection";

    /** HTTP 1.0 version: HTTP/1.0. */
    public static final String HTTP_10 = "HTTP/1.0";

    /** HTTP 1.1 version: HTTP/1.1 */
    public static final String HTTP_11 = "HTTP/1.1";

    /** HTTP 1.1 warning code 110: Response is stale. */
    public static final int RESPONSE_IS_STALE = 110;

    /** HTTP 1.1 warning code 111: Revalidation failed. */
    public static final int REVALIDATION_FAILED = 111;

    /** HTTP 1.1 warning code 112: Disconnected operation. */
    public static final int DISCONNECTED_OPERATION = 112;

    /** HTTP 1.1 warning code 113: Heuristic expiration. */
    public static final int HEURISTIC_EXPIRATION = 113;

    /** HTTP 1.1 warning code 199: Miscellaneous warning. */
    public static final int MISCELLANEOUS_WARNING = 199;

    /** HTTP 1.1 warning code 214: Transformation applied. */
    public static final int TRANSFORMATION_APPLIED = 214;

    /** HTTP 1.1 warning code 299: Miscellaneous persistent warning. */
    public static final int MISCELLANEOUS_PERSISTENT_WARNING = 299;

    protected static final String CR_LF = "\r\n";
    protected static final String COLON = ":";
    protected static final String SPACE = " ";

    private static final Log LOG = LogFactory.getLog(HttpMessage.class);

    /** This <code>HttpMessage</code>'s entity body. */
    protected EntityBody entityBody;

    /** The map containing all the headers for this <code>HttpMessage</code>. */
    protected HeaderMap headerMap = new HeaderMap();

    /** This <code>HttpMessage</code>'s HTTP version string. */
    protected String httpVersion;

    /**
     * <p>
     *   Constructs a new <code>HttpMessage</code> with the specified
     *   <code>httpVersion</code> as the HTTP version.
     * </p>
     *
     * @param      httpVersion
     *                 the HTTP version of the HTTP message to be constructed.
     * @throws     IllegalArgumentException
     *                 if the specified <code>httpVersion</code> is either
     *                 <code>null</code> or empty.
     * @see        #HTTP_10
     * @see        #HTTP_11
     */
    protected HttpMessage(final String httpVersion)
    throws IllegalArgumentException {
        setHttpVersion(httpVersion);
    }

    /**
     * <p>
     *   Removes all the headers from this <code>HttpMessage</code>.
     * </p>
     */
    public void clearHeaders() {
        headerMap.clear();
    }

    public Object clone() {
        try {
            HttpMessage _httpMessage = (HttpMessage)super.clone();
            if (entityBody != null) {
                _httpMessage.entityBody = (EntityBody)entityBody.clone();
            }
            _httpMessage.headerMap = (HeaderMap)headerMap.clone();
            return _httpMessage;
        } catch (CloneNotSupportedException exception) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * <p>
     *   Returns <code>true</code> if this <code>HttpMessage</code> contains a
     *   header with the specified <code>fieldName</code>.
     * </p>
     *
     * @param      fieldName
     *                 field name whose presence in this
     *                 <code>HttpMessage</code> is to be tested.
     * @return     <code>true</code> if this <code>HttpMessage</code> contains a
     *             header with the specified <code>fieldName</code>.
     */
    public boolean containsHeader(final String fieldName) {
        return headerMap.containsFieldName(fieldName);
    }

    /**
     * <p>
     *   Releases all of the resources used by this <code>HttpMessage</code> and
     *   its subcomponent <code>EntityBody</code>, if it exists. That is, the
     *   resources will be destroyed and any memory they consume will be
     *   returned to the OS.
     * </p>
     */
    public void dispose() {
        if (entityBody != null) {
            entityBody.dispose();
        }
    }

    public boolean equals(final Object object) {
        return
            object instanceof HttpMessage &&
            ((HttpMessage)object).httpVersion.equalsIgnoreCase(httpVersion) &&
            ((HttpMessage)object).headerMap.equals(headerMap) &&
            ((HttpMessage)object).entityBody == null ?
                entityBody == null :
                ((HttpMessage)object).entityBody.equals(entityBody);
    }

    /**
     * <p>
     *   Convenience method for getting the byte array of this
     *   <code>HttpMessage</code>. This method has the same effect as:
     *   <pre><code>    getMessage(true).getBytes()</code></pre>
     * </p>
     *
     * @return     the byte array representation of this
     *             <code>HttpMessage</code>.
     */
    public byte[] getBytes() {
        // get the HTTP request message without the entity body; bytes into
        // string into bytes will not be the same!
        byte[] _bytes = getMessage(false).getBytes();
        if (entityBody != null) {
            byte[] _entityBody = entityBody.getBytes();
            byte[] _temp = _bytes;
            _bytes = new byte[_temp.length + _entityBody.length];
            System.arraycopy(_temp, 0, _bytes, 0, _temp.length);
            System.arraycopy(
                _entityBody, 0, _bytes, _temp.length, _entityBody.length);
        }
        return _bytes;
    }

    /**
     * <p>
     *   Returns the entity body of this <code>HttpMessage</code>.
     * </p>
     *
     * @return     the entity body.
     * @see        #setEntityBody(EntityBody)
     */
    public EntityBody getEntityBody() {
        return entityBody;
    }

    /**
     * <p>
     *   Returns the header at the specified <code>index</code> in this
     *   <code>HttpMessage</code>.
     * </p>
     *
     * @param      index
     *                 the index of the header to return.
     * @return     the header at the specified <code>index</code>
     *             in this <code>HttpMessage</code>, or <code>null</code>.
     */
    public HeaderMap.Header getHeader(final int index) {
        return headerMap.get(index);
    }

    /**
     * <p>
     *   Returns the number of headers contained in this
     *   <code>HttpMessage</code>.
     * </p>
     *
     * @return     the number of headers.
     */
    public int getHeaderCount() {
        return headerMap.getSize();
    }

    /**
     * <p>
     *   Returns the field name at the specified <code>index</code> in this
     *   <code>HttpMessage</code>.
     * </p>
     *
     * @param      index
     *                 the index of the field name to return.
     * @return     the field name at the specified <code>index</code> in this
     *             <code>HttpMessage</code>, or <code>null</code>.
     * @see        #getFieldValue(int)
     */
    public String getFieldName(final int index) {
        return headerMap.getFieldName(index);
    }

    /**
     * <p>
     *   Returns the field value at the specified <code>index</code> in this
     *   <code>HttpMessage</code>.
     * </p>
     *
     * @param      index
     *                 the index of the field value to return.
     * @return     the field value at the specified <code>index</code> in this
     *             <code>HttpMessage</code>, or <code>null</code>.
     * @see        #getFieldName(int)
     */
    public String getFieldValue(final int index) {
        return headerMap.getFieldValue(index);
    }

    /**
     * <p>
     *   Returns the first field value that is mapped with the specified
     *   <code>fieldName</code> in this <code>HttpMessage</code>.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mapped field value is to be
     *                 returned.
     * @return     the first field value to which this <code>HttpMessage</code>
     *             maps the specified <code>fieldName</code> or
     *             <code>null</code>.
     */
    public String getFieldValue(final String fieldName) {
        return headerMap.getFieldValue(fieldName);
    }

    /**
     * <p>
     *   Returns the first field value as a primitive int that is mapped with
     *   the specified <code>fieldName</code> in this <code>HttpMessage</code>.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mapped field value is to be
     *                 returned.
     * @return     the first field value to which this <code>HttpMessage</code>
     *             maps the specified <code>fieldName</code> or
     *             <code>null</code>.
     * @throws     NumberFormatException
     */
    public int getFieldValueAsInt(final String fieldName)
    throws NumberFormatException {
        return Integer.parseInt(getFieldValue(fieldName));
    }

    /**
     * <p>
     *   Returns the field values that are mapped with the specified
     *   <code>fieldName</code> in this <code>HttpMessage</code>.
     * </p>
     * <p>
     *   If this <code>HttpMessage</code> contains more than one mapping with
     *   the specified <code>fieldName</code>, all the field values are
     *   returned.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mapped field value is to be
     *                 returned.
     * @return     zero or more field values to which this
     *             <code>HttpMessage</code> maps the specified
     *             <code>fieldName</code>.
     */
    public String[] getFieldValues(final String fieldName) {
        return headerMap.getFieldValues(fieldName);
    }

    /**
     * <p>
     *   Returns the HTTP version of this <code>HttpMessage</code>.
     * </p>
     *
     * @return     the HTTP version.
     * @see        #setHttpVersion(String)
     */
    public String getHttpVersion() {
        return httpVersion;
    }

    /**
     * <p>
     *   Convenience method for getting the length of this
     *   <code>HttpMessage</code>. This method has the same effect as:
     *   <pre><code>    getBytes().length</code></pre>
     * </p>
     *
     * @return     the length.
     */
    public int getLength() {
        return getBytes().length;
    }

    /**
     * <p>
     *   Convenience method for getting the actual <code>String</code>
     *   representation of this <code>HttpMessage</code> without the entity
     *   body.
     * </p>
     *
     * @return     the <code>String</code> representation.
     * @see        #getMessage(boolean)
     */
    public String getMessage() {
        return getMessage(false);
    }

    /**
     * <p>
     *   Convenience method for getting the actual <code>String</code>
     *   representation of this <code>HttpMessage</code> depending on the
     *   specified <code>showBody</code> with or without the entity body.
     * </p>
     *
     * @param      showBody
     * @return     the <code>String</code> representation.
     */
    public abstract String getMessage(final boolean showBody);

    /**
     * <p>
     *   Gets the start-line of this <code>HttpMessage</code>.
     * </p>
     *
     * @return     the start-line.
     */
    public abstract String getStartLine();

    public void putHeader(final String fieldName, final int fieldValue) {
        headerMap.putHeader(fieldName, Integer.toString(fieldValue));
    }

    public void putHeader(
        final String fieldName, final int fieldValue, final boolean replace) {

        headerMap.putHeader(fieldName, Integer.toString(fieldValue), replace);
    }

    /**
     * <p>
     *   Maps the specified <code>fieldValue</code> with the specified
     *   <code>fieldName</code> in this <code>HttpMessage</code>. If this
     *   <code>HttpMessage</code> previously contained a mapping for this
     *   <code>fieldName</code>, an additional mapping is created for the
     *   specified <code>fieldValue</code>.
     * </p>
     *
     * @param      fieldName
     *                 the field name with which the specified
     *                 <code>fieldValue</code> is to be mapped.
     * @param      fieldValue
     *                 the field value to be mapped with the specified
     *                 <code>fieldName</code>.
     */
    public void putHeader(final String fieldName, final String fieldValue) {
        headerMap.putHeader(fieldName, fieldValue);
    }

    public void putHeader(
        final String fieldName, final String fieldValue,
        final boolean replace) {

        headerMap.putHeader(fieldName, fieldValue, replace);
    }

    /**
     * <p>
     *   Removes the mapping at the specified <code>index</code> from this
     *   <code>HttpMessage</code> if present.
     * </p>
     *
     * @param      index
     *                 the index of the mapping to be removed from this
     *                 <code>HttpMessage</code>.
     */
    public void removeHeader(final int index) {
        headerMap.removeHeader(index);
    }

    /**
     * <p>
     *   Removes all mappings for the specified <code>fieldName</code> from
     *   this <code>HttpMessage</code> if one is present.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mappings are to be removed from this
     *                 <code>HttpMessage</code>.
     */
    public void removeHeaders(final String fieldName) {
        headerMap.removeHeaders(fieldName);
    }

    /**
     * <p>
     *   Sets the entity body of this <code>HttpMessage</code> to the specified
     *   <code>entityBody</code>.
     * </p>
     *
     * @param      entityBody
     *                 the new entity body.
     * @see        #getEntityBody()
     */
    public void setEntityBody(final EntityBody entityBody) {
        this.entityBody = entityBody;
    }

    /**
     * <p>
     *   Sets the HTTP version of this <code>HttpMessage</code> to the specified
     *   <code>httpVersion</code>.
     * </p>
     *
     * @param      httpVersion
     *                 the new HTTP version.
     * @throws     IllegalArgumentException
     *                 if the specified <code>httpVersion</code> is either
     *                 <code>null</code> or empty.
     * @see        #getHttpVersion()
     */
    public void setHttpVersion(final String httpVersion)
    throws IllegalArgumentException {
        if (httpVersion == null) {
            throw new IllegalArgumentException("httpVersion is null");
        }
        if (httpVersion.trim().length() == 0) {
            throw new IllegalArgumentException("httpVersion is empty");
        }
        this.httpVersion = httpVersion.toUpperCase();
    }

    /**
     * <p>
     *   The <code>EntityBody</code> class represents the entity body of an HTTP
     *   message. It solely contains the data.
     * </p>
     * <p>
     *   Additionally, it has all the entity header constants as a convenience
     *   and uniform use of these HTTP elements.
     * </p>
     */
    public static class EntityBody
    implements Cloneable, Serializable {
        /** HTTP 1.0 entity header: Allow. */
        public static final String ALLOW = "Allow";

        /** HTTP 1.1 entity header: Content-Base. */
        public static final String CONTENT_BASE = "Content-Base";

        /** HTTP 1.0 entity header: Content-Encoding. */
        public static final String CONTENT_ENCODING = "Content-Encoding";

        /** HTTP 1.1 entity header: Content-Language. */
        public static final String CONTENT_LANGUAGE = "Content-Language";

        /** HTTP 1.0 entity header: Content-Length. */
        public static final String CONTENT_LENGTH = "Content-Length";

        /** HTTP 1.1 entity header: Content-Location. */
        public static final String CONTENT_LOCATION = "Content-Location";

        /** HTTP 1.1 entity header: Content-MD5. */
        public static final String CONTENT_MD5 = "Content-MD5";

        /** HTTP 1.1 entity header: Content-Range. */
        public static final String CONTENT_RANGE = "Content-Range";

        /** HTTP 1.0 entity header: Content-Type. */
        public static final String CONTENT_TYPE = "Content-Type";

        /** HTTP 1.0 entity header: Expires. */
        public static final String EXPIRES = "Expires";

        /** HTTP 1.0 entity header: Last-Modified. */
        public static final String LAST_MODIFIED = "Last-Modified";

        /** The byte array representing this <code>EntityBody</code>'s data. */
        protected byte[] bytes;

        /**
         * The input stream from which this <code>EntityBody</code> gets its
         * data.
         */
        protected InputStream inputStream;
        // if synchronization becomes an issue, uncomment this lock object.
//        private Object lock = new Object();

        /**
         * <p>
         *   Constructs a new <code>EntityBody</code> with the specified
         *   <code>bytes</code> as the contained data.
         * </p>
         *
         * @param      bytes
         *                 the byte array representing the to be created
         *                 <code>EntityBody</code>'s data.
         * @throws     IllegalArgumentException
         *                 if the specified <code>bytes</code> is
         *                 <code>null</code>.
         */
        public EntityBody(final byte[] bytes) throws IllegalArgumentException {
            if (bytes == null) {
                throw new IllegalArgumentException("bytes is null");
            }
            this.bytes = bytes;
        }

        /**
         * <p>
         *   Constructs a new <code>EntityBody</code> with the specified
         *   <code>inputStream</code> as the "contained" data.
         * </p>
         *
         * @param      inputStream
         *                 the input stream from which the to be created
         *                 <code>EntityBody</code> gets its data.
         * @throws     IllegalArgumentException
         *                 if the specified <code>inputStream</code> is
         *                 <code>null</code>.
         */
        public EntityBody(final InputStream inputStream)
        throws IllegalArgumentException {
            if (inputStream == null) {
                throw new IllegalArgumentException("inputStream is null");
            }
            this.inputStream = new LazyCachedInputStream(inputStream);
        }

        public Object clone() {
            try {
                return (EntityBody)super.clone();
            } catch (CloneNotSupportedException exception) {
                // this shouldn't happen, since we are Cloneable
                throw new InternalError();
            }
        }

        /**
         * <p>
         *   Releases all of the resources used by this <code>EntityBody</code>.
         *   That is, the resources will be destroyed and any memory they
         *   consume will be returned to the OS.
         * </p>
         */
        public void dispose() {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("An I/O exception occurred!", exception);
                    }
                }
            }
        }

        public boolean equals(final Object object) {
            // todo: implement this.
            return true;
        }

        /**
         * <p>
         *   Returns a byte array representing the data "contained" in this
         *   <code>EntityBody</code>.
         * </p>
         *
         * @return     a byte array representing the data.
         * @see        #getInputStream()
         */
        public byte[] getBytes() {
            // if synchronization becomes an issue, uncomment these two blocks.
//            if (bytes == null) {
//                synchronized (lock) {
                    if (bytes == null) {
                        try {
                            ByteArrayOutputStream _byteArrayOutputStream =
                                new ByteArrayOutputStream();
                            InputStream _inputStream = getInputStream();
                            byte _byte;
                            while ((_byte = (byte)_inputStream.read()) != -1) {
                                _byteArrayOutputStream.write(_byte);
                            }
                            bytes = _byteArrayOutputStream.toByteArray();
                        } catch (IOException exception) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error(
                                    "An I/O exception occurred!", exception);
                            }
                        }
                    }
//                }
//            }
            return bytes;
        }

        /**
         * <p>
         *   Returns an input stream that reads the data "contained" in this
         *   <code>EntityBody</code>.
         * </p>
         *
         * @return     an input stream that reads the data.
         * @see        #getBytes()
         */
        public InputStream getInputStream() {
            // if synchronization becomes an issue, uncomment these two blocks.
//            if (inputStream == null) {
//                synchronized (lock) {
                    if (inputStream == null) {
                        inputStream =
                            new LazyCachedInputStream(
                                new ByteArrayInputStream(bytes));
                    }
//                }
//            }
            return new MarkerInputStream((LazyCachedInputStream)inputStream);
        }

        public String toString() {
            return new String(getBytes());
        }
    }
}
