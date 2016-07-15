/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.samples.mediacast;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PortableRenderer;
import org.icepush.servlet.MainServlet;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bean for providing a Cloud Push debug view
 */
@ManagedBean
@ApplicationScoped
public class CloudDebug implements Serializable, MainServlet.TraceListener {
    public static final String DEBUG_GROUP = "clouddebug";
    private PortableRenderer portableRenderer;
    ArrayList<String> messages = new ArrayList();

    public CloudDebug() {
        messages.add("Cloud Push Debug");
        portableRenderer = PushRenderer.getPortableRenderer();
        MainServlet.addTraceListener(this);
    }

    public void handleTrace(String message)  {
        messages.add(messages.size() + ". " + message);
        portableRenderer.render(DEBUG_GROUP);
    }
    
    public List getMessages()  {
        return messages;
    }
}
