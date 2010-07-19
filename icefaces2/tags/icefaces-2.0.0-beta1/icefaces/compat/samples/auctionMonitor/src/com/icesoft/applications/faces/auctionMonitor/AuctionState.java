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

package com.icesoft.applications.faces.auctionMonitor;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Class used to maintain an overall state of the auction This class is a
 * singleton as there should only be a single state at one time Also this class
 * will handle global auction events
 */
public class AuctionState {
    public static final String AUCTION_STATE =
            "com.icesoft.applications.faces.auctionMonitor.AuctionState";
    private static Map auctionMap = new Hashtable();
    private static AuctionState singleton = null;
    private WeakHashMap listeners = new WeakHashMap();

    public static Map getAuctionMap() {
        return auctionMap;
    }

    public static synchronized AuctionState getInstance() {
        if (null == singleton) {
            singleton = new AuctionState();
        }
        return singleton;
    }

    public void addAuctionListener(AuctionListener listener) {
        listeners.put(listener, null);
    }

    public void fireAuctionEvent(final AuctionEvent auctionEvent) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Iterator listenerList = listeners.keySet().iterator();
                AuctionListener auctionListener;
                while (listenerList.hasNext()) {
                    auctionListener = (AuctionListener) listenerList.next();
                    Thread.currentThread().setContextClassLoader(
                            auctionListener.getClass().getClassLoader());
                    auctionListener.handleAuctionEvent(auctionEvent);
                }
            }
        });
        t.start();
    }
}
