/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icesoft.jasper;

/**
 * Base class for all exceptions generated by the JSP engine. Makes it
 * convienient to catch just this at the top-level.
 *
 * @author Anil K. Vijendran
 */
public class JasperException extends javax.servlet.ServletException {

    public JasperException(String reason) {
        super(reason);
    }

    /**
     * Creates a JasperException with the embedded exception and the reason for
     * throwing a JasperException
     */
    public JasperException(String reason, Throwable exception) {
        super(reason, exception);
    }

    /**
     * Creates a JasperException with the embedded exception
     */
    public JasperException(Throwable exception) {
        super(exception);
    }
}
