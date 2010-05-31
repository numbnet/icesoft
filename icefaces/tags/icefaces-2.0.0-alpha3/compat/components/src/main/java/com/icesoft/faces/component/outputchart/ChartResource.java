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

package com.icesoft.faces.component.outputchart;

import com.icesoft.faces.context.ByteArrayResource;

import java.io.Serializable;

public class ChartResource extends ByteArrayResource implements Serializable {
    private static long prevDigest = 0;
    
    public ChartResource(byte[] content) {
        super(content);
    }

    public String calculateDigest() {
        long digest = System.currentTimeMillis();
        synchronized (getClass()) { // ICE-3052
            if (digest <= prevDigest) {
                digest = prevDigest + 1;
            }
            prevDigest = digest;
        }
        return String.valueOf("CHART"+digest);
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof ChartResource)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }
}
