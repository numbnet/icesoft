/*
 * Version: MPL 1.1
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
 */

package org.icefaces.impl.util;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Resource;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class Util {
    private static Logger log = Logger.getLogger(Util.class.getName());
    private static List DEFAULT_EXCLUSIONS = Arrays.asList(
            "image/gif",  "image/png", "image/jpeg", "image/tiff", "application/pdf", 
            "application/zip", "application/x-compress", "application/x-gzip ",
            "application/java-archive", "video/x-sgi-movie", "audio/x-mpeg ",
            "video/mp4", "video/mpeg");

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1000];
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                out.write(buf, 0, l);
            }
        }
    }
    
    public static void compressStream(InputStream in, OutputStream out) throws IOException {
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        copyStream(in, gzip);
        gzip.finish();
    }
    
    public static boolean acceptGzip(ExternalContext externalContext)  {
        String acceptHeader = externalContext.getRequestHeaderMap()
            .get("Accept-Encoding");
        boolean acceptGzip = (null != acceptHeader) && 
            (acceptHeader.indexOf("gzip") >=0);
        return acceptGzip;
    }

    public static boolean shouldCompress(String contentType)  {
        return !DEFAULT_EXCLUSIONS.contains(contentType);
    }
}
