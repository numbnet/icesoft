package org.icemobile.client.blackberry.script.debug;

import net.rim.device.api.script.ScriptableFunction;
import org.icemobile.client.blackberry.Logger;



public class JavascriptDebugger extends ScriptableFunction {
    

    /**
     *  invoke the call from javascript layer. The purpose of this is to 
     *  log messages from the javascript layer into the standard blackberry logger
     */
    public  Object invoke( Object thiz, Object[] args) { 
        
        String message = (String) args[0]; 
        Logger.DEBUG(message);
        return Boolean.TRUE;
    }

}