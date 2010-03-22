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
package com.icesoft.net;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>
 *   The HeaderMap class represents a collection of <code>{@link Header}</code>s
 *   each consisting of a field name and a field value. The HeaderMap class
 *   allows duplicate field name entries!
 * </p>
 */
public class HeaderMap
implements Cloneable, Serializable {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private Header[] headerData;
    private int size = 0;

    /**
     * <p>
     *   Constructs an empty <code>HeaderMap</code>.
     * </p>
     */
    public HeaderMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * <p>
     *   Constructs an empty <code>HeaderMap</code> with the specified
     *   <code>initialCapacity</code>.
     * </p>
     *
     * @param      initialCapacity
     *                 the initial capacity of the <code>HeaderMap</code>.
     * @throws     IllegalArgumentException
     *                 if the specified <code>initialCapacity</code> is less
     *                 than <code>0</code>.
     */
    public HeaderMap(int initialCapacity) throws IllegalArgumentException {
        if (initialCapacity < 0) {
            throw
                new IllegalArgumentException(
                    "illegal capacity: " + initialCapacity);
        }
        headerData = new Header[initialCapacity];
    }

    /**
     * <p>
     *   Removes all the headers from this <code>HeaderMap</code>. This
     *   <code>HeaderMap</code> will be empty after this call returns.
     * </p>
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            headerData[i] = null;
        }
        size = 0;
    }

    public Object clone() {
        try {
            HeaderMap _headerMap = (HeaderMap)super.clone();
            _headerMap.headerData = new Header[size];
            System.arraycopy(headerData, 0, _headerMap.headerData, 0, size);
            return _headerMap;
        } catch (CloneNotSupportedException exception) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * <p>
     *   Returns <code>true</code> if this <code>HeaderMap</code> contains a
     *   mapping for the specified <code>fieldName</code>.
     * </p>
     *
     * @param      fieldName
     *                 field name whose presence in this <code>HeaderMap</code>
     *                 is to be tested.
     * @return     <code>true</code> if this <code>HeaderMap</code> contains a
     *             mapping for the specified <code>fieldName</code>.
     */
    public boolean containsFieldName(String fieldName) {
        return getFieldValues(fieldName).length > 0;
    }

    /**
     * <p>
     *   Increases the capacity of this <code>HeaderMap</code> instance, if
     *   necessary, to ensure that it can hold at least the number of headers
     *   specified by the <code>minimumCapacity</code> argument.
     * </p>
     *
     * @param      minimumCapacity
     *                 the desired minimum capacity.
     */
    public void ensureCapacity(int minimumCapacity) {
        int _oldCapacity = headerData.length;
        if (minimumCapacity > _oldCapacity) {
            Header[] _oldHeaderData = headerData;
            int _newCapacity = (_oldCapacity * 3) / 2 + 1;
            if (_newCapacity < minimumCapacity) {
                _newCapacity = minimumCapacity;
            }
            headerData = new Header[_newCapacity];
            System.arraycopy(_oldHeaderData, 0, headerData, 0, size);
        }
    }

    public boolean equals(Object object) {
        if (object instanceof HeaderMap && size == ((HeaderMap)object).size) {
            for (int i = 0; i < size; i++) {
                if (!headerData[i].fieldName.equalsIgnoreCase(
                        ((HeaderMap)object).headerData[i].fieldName) ||
                    !headerData[i].fieldValue.equals(
                        ((HeaderMap)object).headerData[i].fieldValue)) {

                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * <p>
     *   Returns the <code>Header</code> at the specified <code>index</code> in
     *   this <code>HeaderMap</code>.
     * </p>
     *
     * @param      index
     *                 the index of the <code>Header</code> to return.
     * @return     the <code>Header</code> at the specified <code>index</code>
     *             in this <code>HeaderMap</code>, or <code>null</code>.
     */
    public Header get(int index) {
        if (index >= 0 && index < size) {
            return headerData[index];
        } else {
            return null;
        }
    }

    /**
     * <p>
     *   Returns the field name at the specified <code>index</code> in this
     *   <code>HeaderMap</code>.
     * </p>
     *
     * @param      index
     *                 the index of the field name to return.
     * @return     the field name at the specified <code>index</code> in this
     *             <code>HeaderMap</code>, or <code>null</code>.
     */
    public String getFieldName(int index) {
        if (index >= 0 && index < size) {
            return headerData[index].fieldName;
        } else {
            return null;
        }
    }

    /**
     * <p>
     *   Returns the field value at the specified <code>index</code> in this
     *   <code>HeaderMap</code>.
     * </p>
     *
     * @param      index
     *                 the index of the field value to return.
     * @return     the field value at the specified <code>index</code> in this
     *             <code>HeaderMap</code>, or <code>null</code>.
     */
    public String getFieldValue(int index) {
        if (index >= 0 && index < size) {
            return headerData[index].fieldValue;
        } else {
            return null;
        }
    }

    /**
     * <p>
     *   Returns the first field value that is mapped with the specified
     *   <code>fieldName</code> in this <code>HeaderMap</code>.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mapped field value is to be
     *                 returned.
     * @return     the first field value to which this <code>HeaderMap</code>
     *             maps the specified <code>fieldName</code> or
     *             <code>null</code>.
     */
    public String getFieldValue(String fieldName) {
        int _index = getIndex(fieldName);
        if (_index != -1) {
            return headerData[_index].fieldValue;
        } else {
            return null;
        }
    }

    /**
     * <p>
     *   Returns the field value that is mapped with the specified
     *   <code>fieldName</code> in this <code>HeaderMap</code>.
     * </p>
     * <p>
     *   If this <code>HeaderMap</code> contains more than one mapping with the
     *   specified <code>fieldName</code>, all the field values are returned.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mapped field value is to be
     *                 returned.
     * @return     zero or more field values to which this
     *             <code>HeaderMap</code> maps the specified
     *             <code>fieldName</code>.
     */
    public String[] getFieldValues(String fieldName) {
        Vector _fieldValueList = new Vector();
        int _index = 0;
        while ((_index = getIndex(fieldName, _index)) != -1) {
            _fieldValueList.addElement(headerData[_index].fieldValue);
            _index++;
        }
        String[] _fieldValues = new String[_fieldValueList.size()];
        for (int i = 0; i < _fieldValues.length; i++) {
            _fieldValues[i] = (String)_fieldValueList.elementAt(i);
        }
        return _fieldValues;
    }

    /**
     * <p>
     *   Returns the number of <code>Header</code>s in this
     *   <code>HeaderMap</code>.
     * </p>
     *
     * @return     the number of <code>Header</code>s in this
     *             <code>HeaderMap</code>.
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>
     *   Returns <code>true</code> if this <code>HeaderMap</code> contains no
     *   headers.
     * </p>
     *
     * @return     <code>true</code> if this <code>HeaderMap</code> contains no
     *             headers.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * <p>
     *   Maps the specified <code>fieldValue</code> with the specified
     *   <code>fieldName</code> in this <code>HeaderMap</code>. If this
     *   <code>HeaderMap</code> previously contained a mapping for this
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
     * @see        #putHeader(String, String, boolean)
     */
    public void putHeader(String fieldName, String fieldValue) {
        if (fieldName != null &&
            fieldName.trim().length() != 0 &&
            fieldValue != null) {

            if (containsFieldName(fieldName)) {
                String[] _fieldValues = getFieldValues(fieldName);
                for (int i = 0; i < _fieldValues.length; i++) {
                    if (fieldValue.equals(_fieldValues[i])) {
                        return;
                    }
                }
            }

            ensureCapacity(size + 1);
            headerData[size++] = new Header(fieldName, fieldValue);
        }
    }

    /**
     * <p>
     *   Maps the specified <code>fieldValue</code> with the specified
     *   <code>fieldName</code> in this <code>HeaderMap</code>. If the specified
     *   <code>replace</code> boolean equals <code>true</code>, any previous
     *   mappings for this <code>fieldName</code> will be removed, otherwise an
     *   additional mapping is created for the specified
     *   <code>fieldValue</code>.
     * </p>
     *
     * @param      fieldName
     *                 the field name with which the specified
     *                 <code>fieldValue</code> is to be mapped.
     * @param      fieldValue
     *                 the field value to be mapped with the specified
     *                 <code>fieldName</code>.
     * @param      replace
     *                 if set to <code>true</code>, this <code>fieldName</code>
     *                 mapping replaces all previous mappings to
     *                 <code>fieldName</code>.
     * @see        #putHeader(String, String)
     */
    public void putHeader(
        String fieldName, String fieldValue, boolean replace) {

        if (replace && containsFieldName(fieldName)) {
            removeHeaders(fieldName);
        }
        putHeader(fieldName, fieldValue);
    }

    /**
     * <p>
     *   Removes the mapping at the specified <code>index</code> from this
     *   <code>HeaderMap</code> if present.
     * </p>
     *
     * @param      index
     *                 the index of the mapping to be removed from this
     *                 <code>HeaderMap</code>.
     */
    public void removeHeader(int index) {
        if (index >= 0 && index < size) {
            int _toBeMoved = size - index - 1;
            if (_toBeMoved > 0) {
                System.arraycopy(
                    headerData, index + 1, headerData, index, _toBeMoved);
            }
            headerData[--size] = null;
        }
    }

    /**
     * <p>
     *   Removes all mappings for the specified <code>fieldName</code> from
     *   this <code>HeaderMap</code> if one is present.
     * </p>
     *
     * @param      fieldName
     *                 the field name whose mappings are to be removed from this
     *                 <code>HeaderMap</code>.
     */
    public void removeHeaders(String fieldName) {
        int _index = 0;
        while ((_index = getIndex(fieldName, _index)) != -1) {
            removeHeader(_index);
        }
    }

    public String toString() {
        StringBuffer _stringBuffer = new StringBuffer();
        _stringBuffer.append("HeaderMap [");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                _stringBuffer.append(",");
            }
            _stringBuffer.append("\r\n    ");
            _stringBuffer.append(headerData[i]);
        }
        _stringBuffer.append("]");
        return _stringBuffer.toString();
    }

    private int getIndex(String fieldName) {
        return getIndex(fieldName, 0);
    }

    private int getIndex(String fieldName, int fromIndex) {
        if (fromIndex >= 0 && fromIndex < size) {
            for (int i = fromIndex; i < size; i++) {
                if (headerData[i].fieldName.equalsIgnoreCase(fieldName)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     *
     */
    public static class Header implements Serializable {
        private String fieldName;
        private String fieldValue;

        /**
         * <p>
         *   Constructs a new <code>Header</code> with the specified
         *   <code>fieldName</code> and <code>fieldValue</code>.
         * </p>
         *
         * @param      fieldName
         *                 the field name of the <code>Header</code> to be
         *                 created.
         * @param      fieldValue
         *                 the field value of the <code>Header</code> to be
         *                 created.
         */
        public Header(String fieldName, String fieldValue) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public boolean equals(Object object) {
            return
                object instanceof Header &&
                fieldName.equals(((Header)object).fieldName) &&
                fieldValue.equals(((Header)object).fieldValue) &&
                super.equals(object);
        }

        /**
         * <p>
         *   Returns the field name of this <code>Header</code>.
         * </p>
         *
         * @return     the field name of this <code>Header</code>.
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         * <p>
         *   Returns the field value of this <code>Header</code>.
         * </p>
         *
         * @return     the field value of this <code>Header</code>.
         */
        public String getFieldValue() {
            return fieldValue;
        }

        public String toString() {
            return fieldName + ": " + fieldValue;
        }
    }
}
