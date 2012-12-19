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

package org.icefaces.component.flipswitch;

import org.icefaces.component.utils.Attribute;
import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;


import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;

 

public class FlipSwitch extends FlipSwitchBase {
    //src is NOT part of the pass through attributes
    public static final String FLIPSWITCH_ON_CLASS = "mobi-flip-switch mobi-flip-switch-on ";
    public static final String FLIPSWITCH_OFF_CLASS = "mobi-flip-switch mobi-flip-switch-off ";
    private Attribute[] attributesNames = {
            new Attribute("tabindex", null),
            new Attribute("style", null)};

    private Attribute[] booleanAttNames = {new Attribute("required", null),
            new Attribute("immediate", null),
            new Attribute("singleSubmit", null)};
 
    public Attribute[] getAttributesNames() {
        return attributesNames;
    }

	public Attribute[] getBooleanAttNames() {
		return booleanAttNames;
	}


}
