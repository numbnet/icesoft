/*
 *
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
 */

package org.icepush.integration.common.notify;

import org.icepush.PushContext;

/**
 * The Notifier provides a handle to a {@link org.icepush.PushContext}, that can be used to 
 * access the ICEpush API from any POJO.
 * @author ICEsoft Technologies, Inc.
 * @see org.ice.PushContext
 */
public class Notifier {
    private PushContext pushContext;

    /**
     * Constructor
     */
    public Notifier() {
    }
    
    /**
     * Returns the PushContext.
     *
     * @return the PushContext
     */
    public PushContext getPushContext() {
	return pushContext;
    }

    /**
     * Sets the PushContext.
     *
     * @param the current PushContext
     */
    public void setPushContext(PushContext pc) {
	pushContext = pc;
    }

    /**
     * Pushes to a named group.
     *
     * @param name of group to push to
     */
    public void push(String group) {
	pushContext.push(group);
    }
}
