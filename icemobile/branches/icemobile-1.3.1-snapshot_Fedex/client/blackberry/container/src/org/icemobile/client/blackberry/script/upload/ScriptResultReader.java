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
package org.icemobile.client.blackberry.script.upload;

import net.rim.device.api.script.ScriptableFunction;

import org.icemobile.client.blackberry.ContainerController;


public class ScriptResultReader extends ScriptableFunction {

    private ContainerController mController;

    public ScriptResultReader(ContainerController controller) { 
        mController = controller;
    }

    /**
     * This extension fetches a queued result from the container for 
     * consumption by other layers of javascript
     */
    public Object invoke( Object thiz, Object[] args) {

        String responseKey = (String) args[0];
        String result = mController.getPendingResponse( responseKey ).getResult();
//      Logger.DEBUG("ice.upload - fetching response with key: " + responseKey +
//                ", result length: " + result.length());
        return result; 

    } 

}
