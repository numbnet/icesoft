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

package com.icesoft.faces.webapp.http.common.standard;

import com.icesoft.faces.webapp.http.common.Server;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ModifiablePathDispatcherServer extends PathDispatcherServer {
    protected List constantPaths = new ArrayList();

    protected synchronized Server findServer(String path) {
        return super.findServer(path);
    }

    public synchronized void dispatchOn(String pathExpression, Server toServer) {
        dispatchOn(pathExpression, pathExpression, toServer);
    }
    
    public synchronized void dispatchOn(
            String constantPath, String pathExpression, Server toServer) {
        super.dispatchOn(pathExpression, toServer);
        constantPaths.add(constantPath);
    }

    /**
     * For the given constantPath, if the pre-existing corresponding
     * pathExpression is different from the given pathExpression, then
     * update to using the newly given pathExpression and server.
     * @return If updated to using new pathExpression and toServer
     */
    public synchronized boolean updateDispatch(String constantPath, String pathExpression, Server toServer) {
        for (int index = 0; index < constantPaths.size(); index++) {
            if (constantPaths.get(index).equals(constantPath)) {
                Pattern pattern = (Pattern) matchers.get(index);
                if (!pattern.matcher(pathExpression).find()) {
                    servers.set(index, toServer);
                    matchers.set(index, Pattern.compile(pathExpression));
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized void stopDispatchFor(Server toServer) {
        int position = servers.indexOf(toServer);
        servers.remove(position);
        matchers.remove(position);
        constantPaths.remove(position);
    }
}
