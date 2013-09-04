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
package org.icemobile.client.blackberry.script.test;


import org.icemobile.client.blackberry.Logger;

import net.rim.device.api.script.ScriptableFunction;

public class ScriptableTest extends ScriptableFunction {


    public ScriptableTest( ) { 
    }

    public Object invoke( Object thiz, Object[] args) {
        if (args.length > 0) { 
            if (args[0] instanceof java.lang.String ) { 
                Logger.DIALOG("Args[0] = " + (String) args[0]);
            } else { 
                Logger.DIALOG("Args[0] = " + args[0]);
            }
        }else { 
            Logger.DIALOG("Zero args call");				
        } 
        return "Success";
    }
}
