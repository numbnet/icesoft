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
package org.icefaces.mobi.component.onlinestatus;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

public class OnlineStatusListenerUtil {
    
    public static void renderOnlineStatusScript(OnlineStatusListener listener, ResponseWriter writer)
        throws IOException{
        String onOffline = listener.getOnOffline();
        if( onOffline != null && onOffline.length() == 0 ){
            onOffline = null;
        }
        String onOnline = listener.getOnOnline();
        if( onOnline != null && onOnline.length() == 0 ){
            onOnline = null;
        }
        if( onOffline != null || onOnline != null ){
            String clientId = listener.getClientId();
            String cleanClientId = clientId.replace(':', '_');
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            String script = "ice.mobi.onlineStatusListener.initClient('" + clientId + "', {";
            
            if( onOnline != null ){
                script += "onOnline: onOnline_" + cleanClientId;
            }
            if( onOnline != null && onOffline != null ){
                script += ",";
            }
            if( onOffline != null ){
                script += "onOffline: onOffline_" + cleanClientId;
            }
            script += "});";
            if( onOnline != null ){
                script += "function onOnline_" + cleanClientId + "(event, elem){"
                           + onOnline
                       + "};";
            }
            if( onOffline != null ){
                script += "function onOffline_" + cleanClientId + "(event, elem){"
                           + onOffline
                       + "};";
            }
            writer.write(script);
            writer.endElement("script");
        }
    }

}
