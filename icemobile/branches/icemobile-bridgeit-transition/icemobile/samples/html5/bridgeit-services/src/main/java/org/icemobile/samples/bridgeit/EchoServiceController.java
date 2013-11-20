/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
package org.icemobile.samples.bridgeit;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

@Controller
public class EchoServiceController {

    @Autowired
    private WebApplicationContext context;

    //allow 100 demo tables; for SaaS this would be unlimited
    Map echoTables = createCache(100);

    @RequestMapping(value = "/echoput/{tableName}", 
            method=RequestMethod.POST, consumes="application/json")
    public @ResponseBody String postEchoPut(HttpServletRequest request,
        @PathVariable String tableName,
        @RequestBody Map jsonBlob) throws IOException {
        
        Map echoTable = (Map) echoTables.get(tableName);
        if (null == echoTable)  {
            echoTable = createCache(100);
            echoTables.put(tableName, echoTable);
        }

        echoTable.put(new Object(), jsonBlob);
        push(tableName);

        return "Thanks!";
   }

    @RequestMapping(value = "/echofetch/{tableName}", 
            method=RequestMethod.GET)
    public @ResponseBody List postEchoFetch(HttpServletRequest request,
        @PathVariable String tableName) throws IOException {
        
        Map echoTable = (Map) echoTables.get(tableName);
        if (null == echoTable)  {
            //need an HTTP error response here
            return null;
        }

        return new ArrayList(echoTable.values());
   }

    public void push(String group) throws IOException {
        //should be encapsulated in an ICEpush service API
        String pushServiceURL = "http://api.bridgeit.mobi/push";
        String pushServiceConfig = context.getServletContext()
                .getInitParameter("ice.push.configuration.contextPath");
        if (null != pushServiceConfig)  {
            pushServiceURL = pushServiceConfig;
        }
        //REST variant
//        URLConnection pushServiceConnection =
//                new URL(pushServiceURL + "/rest/groups/" + group + "?apikey=197EBF31-40CD-444F-826F-10158A0F3581")
//                .openConnection();
        URLConnection pushServiceConnection =
                new URL(pushServiceURL + "/notify.icepush")
                .openConnection();
        pushServiceConnection.addRequestProperty("Referer", "http://bridgeit.mobi/demo");
        pushServiceConnection.setDoOutput(true);
        OutputStream commandStream = pushServiceConnection.getOutputStream();
        commandStream.write(
        ("delay=2000&duration=0&ice.push.apikey=197EBF31-40CD-444F-826F-10158A0F3581&ice.push.browser=deadbeef&group=" + group).getBytes());
        commandStream.flush();
        commandStream.close();

        //wait for but discard server response
        InputStream result = pushServiceConnection.getInputStream();
        byte[] buf = new byte[100];
        while (result.read(buf) > -1) {
        }
        result.close();
    }

    Map createCache(int size)  {
        final int maxEntries = size;
        //construct using "false" to indicate insertion-order iteration
        //rather than access-order iteration so the map is not modified
        //during iteration
        Map cache = new LinkedHashMap(maxEntries+1, .75F, false) {
            // This method is called just after a new entry has been added
            public boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxEntries;
            }
        };
        return cache;
    }
}
