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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package com.icesoft.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Resource represents a handle to a read-only stream of bytes.
 */
public interface Resource {

    /**
     * Calculate a digest that uniquely identifies the content of the resource.
     *
     * @return the digest
     */
    String calculateDigest();

    /**
     * Open reading stream.
     *
     * @return the stream
     */
    InputStream open() throws IOException;

    /**
     * Return timestamp when resource was last updated or created.
     *
     * @return the timestamp
     * @deprecated use {@link Resource.Options#setLastModified} instead
     */
    Date lastModified();

    /**
     * Set additional options for resource downloading.
     *
     * @param options
     * @throws IOException
     */
    void withOptions(Options options) throws IOException;

    /**
     * Callback for setting optional download information.
     */
    interface Options {

        void setMimeType(String mimeType);

        void setLastModified(Date date);

        void setFileName(String fileName);

        void setExpiresBy(Date date);

        void setAsAttachement();
    }
}
