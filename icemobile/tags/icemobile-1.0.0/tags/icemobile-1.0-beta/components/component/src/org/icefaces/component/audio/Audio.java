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

package org.icefaces.component.audio;

import org.icefaces.component.utils.Attribute;

import java.util.logging.Logger;


public class Audio extends AudioBase {
    private static final Logger logger =
            Logger.getLogger(Audio.class.toString());
    private Attribute[] attributesNames = {new Attribute("url", null),
            new Attribute("controls", null),
            new Attribute("autoplay", null),
            new Attribute("style", null),
            new Attribute("loops", null),
            new Attribute("tabindex", null),
            new Attribute("name", null),
            new Attribute("library", null),
            new Attribute("preload", null)};

//	   private Attribute[] booleanAttNames={new Attribute("readonly", null),
//			                                new Attribute("disabled", null)};

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
