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

package org.icepush.integration.jsp.samples.region;

import org.icepush.integration.common.notify.GroupNotifier;

import java.util.Hashtable;

public class GroupNotificationCounter extends GroupNotifier{
    private Hashtable counters;
    private long interval;

    public GroupNotificationCounter() {
	super();
	counters = new Hashtable();
    }

    public void setGroup(String grp) {
	super.setGroup(grp);
	if (!counters.containsKey(grp)) {
	    counters.put(grp, new Long(System.currentTimeMillis()));
	}
    }

    public long getInterval() {
	return interval;
    }
    public void setInterval(long interval) {
	this.interval = interval;
    }

    public long getCounter() {
	return getCounter(getGroup());
    }

    public long getCounter(String group) {
	Long start = (Long)counters.get(group);
	if (start == null) {
	    return 0;
	} else {
	    long now = System.currentTimeMillis();
	    return (now - start.longValue())/interval;
	}
    }
}
