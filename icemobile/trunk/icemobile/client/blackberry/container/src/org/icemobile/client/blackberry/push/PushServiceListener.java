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
package org.icemobile.client.blackberry.push;

import net.rim.device.api.io.messaging.*;

import java.io.IOException; 
import java.io.InputStream;

import org.icemobile.client.blackberry.Logger;

public class PushServiceListener implements MessageListener {


    public void onMessage(Destination dest, Message incomingMessage) {

        Logger.DEBUG("bis.push - onMessage");
        String payload = null; 
        if  (incomingMessage instanceof ByteMessage) { 
            ByteMessage reply = (ByteMessage) incomingMessage; 
            payload = (String) reply.getStringPayload();
        } else if (incomingMessage instanceof StreamMessage) { 
            StreamMessage reply  = (StreamMessage) incomingMessage; 
            InputStream is = reply.getStreamPayload();
            byte[] data = null; 
            try { 
                data = net.rim.device.api.io.IOUtilities.streamToBytes(is);

                if (data != null) { 
                    payload = new String(data); 
                }

            } catch (IOException e) { 
            	Logger.ERROR("bis.push - Exception in push listener: " + e);
            }


        }
        if (payload != null) { 
            Logger.DEBUG("bis.push - Message received ! : " + payload); 
        }

    }

    public void onMessageCancelled(Destination arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    public void onMessageFailed(Destination arg0, MessageFailureException arg1) {
        // TODO Auto-generated method stub

    }


}
