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
package org.icemobile.client.blackberry.script.audio;

import java.io.IOException;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.device.api.script.ScriptableFunction;

public class AudioPlayback extends ScriptableFunction {

    private ICEmobileContainer mContainer; 

    public AudioPlayback(ICEmobileContainer container) { 
        mContainer = container; 
    }

    public Object invoke( Object thiz, Object[] args) {	

        try { 
            Player p = javax.microedition.media.Manager.createPlayer("http://10.10.10.100:9090/brogcast/images/hello.wav");

            p.realize(); 
            VolumeControl volume = (VolumeControl) p.getControl("VolumeControl"); 

            volume.setLevel(50);
            p.prefetch(); 
            p.start();

        } catch (MediaException me) { 
            ICEmobileContainer.ERROR("Exception in playback:" + me);
        } catch (IOException ioe) { 
            ICEmobileContainer.ERROR("IOException in playback: " + ioe);
        }

        return Boolean.TRUE;
    }

}
