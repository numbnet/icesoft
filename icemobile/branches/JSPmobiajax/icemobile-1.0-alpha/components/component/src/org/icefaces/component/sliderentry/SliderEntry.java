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

package org.icefaces.component.sliderentry;

import org.icefaces.component.utils.Attribute;
import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;

//Does JSF respect order?
//TODO make it so resources can be coalesce and served as whole 

public class SliderEntry extends SliderEntryBase {
    //src is NOT part of the pass through attributes
    private Attribute[] attributesNames = {
            new Attribute("height", null),
            new Attribute("singleSubmit", null),
            new Attribute("max", null),
            new Attribute("min", null),
            new Attribute("onchange", null),
            new Attribute("step", null),
            new Attribute("tabindex", null),
            new Attribute("style", null)};

    //	   private Attribute[] booleanAttNames={new Attribute("readonly", null),
//			                                new Attribute("disabled", null)};
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }

    public void broadcast(FacesEvent event)
            throws AbortProcessingException {
        super.broadcast(event);
        //event was fired by me
        if (event != null) {

            //To keep it simple slider uses the broadcast to update value, so it doesn't
            //have to keep submitted value

            //1. update the value
            ValueExpression ve = getValueExpression("value");
            if (ve != null) {
                try {
                    setValue((Integer) ((ValueChangeEvent) event).getNewValue());
                } catch (ELException ee) {
                    ee.printStackTrace();
                }
            } else {
                setValue((Integer) ((ValueChangeEvent) event).getNewValue());
            }
            //invoke a valuechange listener if any
            MethodExpression method = getValueChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }

    public void queueEvent(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        super.queueEvent(event);
    }


    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }

    public Attribute[] getAttributesNames() {
        return attributesNames;
    }

    public void setAttributesNames(Attribute[] attributesNames) {
        this.attributesNames = attributesNames;
    }

//	public Attribute[] getBooleanAttNames() {
//		return booleanAttNames;
//	}
//
//	public void setBooleanAttNames(Attribute[] booleanAttNames) {
//		this.booleanAttNames = booleanAttNames;
//	}

}
