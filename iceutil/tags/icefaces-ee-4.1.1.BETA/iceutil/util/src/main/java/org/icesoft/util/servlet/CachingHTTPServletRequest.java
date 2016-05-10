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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CachingHTTPServletRequest
extends HttpServletRequestWrapper
implements HttpServletRequest, ServletRequest {
    private static final Logger LOGGER = Logger.getLogger(CachingHTTPServletRequest.class.getName());

    private byte[] cachedBody;

    public CachingHTTPServletRequest(final HttpServletRequest delegateRequest) {
        super(delegateRequest);
    }

    @Override
    public ServletInputStream getInputStream()
    throws IOException {
        if (!hasCachedBody()) {
            ServletInputStream _in = super.getInputStream();
            byte[] _buffer = new byte[8192];
            int _numberOfBytesRead;
            ByteArrayOutputStream _out = new ByteArrayOutputStream();
            while ((_numberOfBytesRead = _in.read(_buffer, 0, _buffer.length)) != -1) {
                _out.write(_buffer, 0, _numberOfBytesRead);
            }
            _out.flush();
            _out.close();
            _in.close();
            setCachedBody(_out.toByteArray());
        }
        return new DelegatingServletInputStream(new ByteArrayInputStream(getCachedBody()));
    }

    protected byte[] getCachedBody() {
        return cachedBody;
    }

    protected boolean hasCachedBody() {
        return getCachedBody() != null;
    }

    protected void setCachedBody(final byte[] cachedBody) {
        this.cachedBody = cachedBody;
    }
}
