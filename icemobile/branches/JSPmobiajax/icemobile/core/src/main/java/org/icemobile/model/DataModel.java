/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.icemobile.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * @see javax.faces.model.DataModel
 */

public abstract class DataModel<E> implements Iterable<E> {


    private static final DataModelListener[] EMPTY_DATA_MODEL_LISTENER =
         new DataModelListener[0];
    
    private List<DataModelListener> listeners = null;

    /**
     * @see javax.faces.model.DataModel#isRowAvailable() 
     */
    public abstract boolean isRowAvailable();


    /**
     * @see javax.faces.model.DataModel#getRowCount()
     */
    public abstract int getRowCount();


    /**
     * @see javax.faces.model.DataModel#getRowData()
     */
    public abstract E getRowData();


    /**
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    public abstract int getRowIndex();


    /**
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    public abstract void setRowIndex(int rowIndex);


    /**
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    public abstract Object getWrappedData();


    /**
     * @see javax.faces.model.DataModel#setWrappedData(Object)
     */
    public abstract void setWrappedData(Object data);

     /**
      * @see javax.faces.model.DataModel#addDataModelListener(javax.faces.model.DataModelListener)
      */
     public void addDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            //noinspection CollectionWithoutInitialCapacity
            listeners = new ArrayList<DataModelListener>();
        }
        listeners.add(listener);

    }


     /**
      * @see javax.faces.model.DataModel#getDataModelListeners()
      */
     public DataModelListener[] getDataModelListeners() {

        if (listeners == null) {
            return EMPTY_DATA_MODEL_LISTENER;
        } else {
            return listeners.toArray
                (new DataModelListener[listeners.size()]);
        }
    }


     /**
      * @see javax.faces.model.DataModel#removeDataModelListener(javax.faces.model.DataModelListener)
      */
     public void removeDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }

    }

     /**
      * @see javax.faces.model.DataModel#iterator()
      */
     public Iterator<E> iterator() {

        return new DataModelIterator<E>(this);
        
    }
    

    @SuppressWarnings({"unchecked"})
    private static final class DataModelIterator<T> implements Iterator<T> {

        private DataModel<T> model;
        private int index;

        DataModelIterator(DataModel<T> model) {

            this.model = model;
            this.model.setRowIndex(index);

        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {

            return model.isRowAvailable();

        }


        /**
         * @see java.util.Iterator#next()
         */
        public T next() {

            if (!model.isRowAvailable()) {
                throw new NoSuchElementException();
            }
            Object o = model.getRowData();
            model.setRowIndex(++index);
            return (T) o;

        }


        /**
         * Unsupported.
         * @see java.util.Iterator#remove()
         */
        public void remove() {

            throw new UnsupportedOperationException();

        }

    } 

}
