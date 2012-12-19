/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.microphone;

import javax.el.MethodExpression;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import java.util.Map;


public class Microphone extends MicrophoneBase {

    public Microphone() {
        super();
    }


    /*if the image properties have to be gotten from the image map to encode*/
    public Object getPropertyFromMap(Map<String, Object> microphoneMap, String key) {
        if (microphoneMap.containsKey(key)) {
            return microphoneMap.get(key);
        } else return null;
    }

    private boolean containsKey(Map<String, Object> microphoneMap, String key) {
        return microphoneMap.containsKey(key);
    }

    public void broadcast(FacesEvent event)
       throws AbortProcessingException {
         if (event instanceof ValueChangeEvent){
            if (event != null) {
                 ValueChangeEvent e = (ValueChangeEvent)event;
                 MethodExpression method = getValueChangeListener();
                 if (method != null) {
                     method.invoke(getFacesContext().getELContext(), new Object[]{event});
                 }
             }
         }
     }

    public void queueEvent(FacesEvent event) {
         if (event.getComponent() instanceof Microphone) {
             if (isImmediate()) {
                 event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
}
             else {
                 event.setPhaseId(PhaseId.INVOKE_APPLICATION);
             }
         }
         super.queueEvent(event);
     }
}
