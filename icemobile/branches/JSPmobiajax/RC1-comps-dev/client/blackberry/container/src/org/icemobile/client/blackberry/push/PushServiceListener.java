/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
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
