/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icesoft.util.servlet;

import static org.icesoft.util.PreCondition.checkIfIsNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;

public class DelegatingServletInputStream
extends ServletInputStream
implements Closeable {
    private static final Logger LOGGER = Logger.getLogger(DelegatingServletInputStream.class.getName());

    private final InputStream delegateInputStream;

    public DelegatingServletInputStream(final InputStream delegateInputStream)
    throws NullPointerException {
        this.delegateInputStream = checkIfIsNotNull(delegateInputStream);
    }

    @Override
    public int read()
    throws IOException {
        return getDelegateInputStream().read();
    }

    protected InputStream getDelegateInputStream() {
        return delegateInputStream;
    }
}
