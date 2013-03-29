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

package org.icefaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;

public class PropertyChangeEvent extends ValueChangeEvent {
    private static final long serialVersionUID = 1L;

    /*
    * Name of the property
    */
    private String name;

    public PropertyChangeEvent(UIComponent component, Object oldValue,
                               Object newValue, String name) {
        super(component, oldValue, newValue);
        this.name = name;
    }

    /**
     * <p>Return the value of the <code>name</code> property.</p>
     */
    public String getName() {
        return name;
    }
}
