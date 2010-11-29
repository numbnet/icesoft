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

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

/**
 * Class used to handle an auction item that is added to the current auction
 * monitor list This class will also handle parsing an items file into a usable
 * item list
 */
public class AddAuctionItem extends AuctionBean {
    private static Log log = LogFactory.getLog(AddAuctionItem.class);

    public static Hashtable parseFile(Reader reader) {
        BufferedReader in = new BufferedReader(reader);
        Hashtable params = new Hashtable();
        String line;
        try {
            String name;
            String value;
            while (null != (line = in.readLine())) {
                int index = line.indexOf(' ');
                name = line.substring(0, index);
                value = line.substring(index + 1);
                params.put(name, value);
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(
                        "Error while parsing an auction item file into the hashtable");
            }
        }
        return params;
    }

    public static String getPictureName(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (fileName.substring(0, index) + ".jpg");
    }

}
