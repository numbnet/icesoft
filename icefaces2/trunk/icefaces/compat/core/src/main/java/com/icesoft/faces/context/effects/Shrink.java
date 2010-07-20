/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.context.effects;

import com.icesoft.faces.util.CoreUtils;

/**
 * script.aculo.us Shrink effect
 * Shrink an element to nothing
 */
public class Shrink extends Effect {
    private String direction = "center";

    public Shrink() {
        ea.add("direction", direction);
    }

    public String getDirection() {
        return direction;
    }

    /**
     * Grow to current size center, top-left, top-right, bottom-left,
     * bottom-right
     *
     * @param direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
        ea.add("direction", direction);
    }

    public String getFunctionName() {
        return "Ice.Scriptaculous.Effect.Shrink";
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Shrink)) {
            return false;
        }
        Shrink effect = (Shrink) obj;
        if (!CoreUtils.objectsEqual(direction, effect.direction)) {
            return false;
        }
        return true;
    }
}
