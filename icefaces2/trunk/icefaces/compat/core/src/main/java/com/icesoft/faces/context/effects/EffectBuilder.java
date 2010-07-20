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

/**
 * Used to translate simple strings into effects
 */
public class EffectBuilder {

    public static Effect build(String name) {
        if ("appear".equalsIgnoreCase(name)) {
            return new Appear();
        }
        if("move".equalsIgnoreCase(name)){
            return new Move();
        }

        if ("fade".equalsIgnoreCase(name)) {
            System.err.println("Returning [" + Fade.class.getName() + "]");
            return new Fade();
        }
        if ("highlight".equalsIgnoreCase(name)) {
            return new Highlight();
        }
        if ("pulsate".equalsIgnoreCase(name)) {
            return new Pulsate();
        }
        if("scale".equalsIgnoreCase(name)){
            return new Scale(.5f);
        }
        if("puff".equalsIgnoreCase(name)){
            return new Puff();
        }
        if("blindup".equalsIgnoreCase(name)){
            return new BlindUp();
        }
        if("blinddown".equalsIgnoreCase(name)){
            return new BlindDown();
        }
        if("swtichoff".equalsIgnoreCase(name)){
            return new SwitchOff();
        }
        if("dropout".equalsIgnoreCase(name)){
            return new DropOut();
        }
        if("shake".equalsIgnoreCase(name)) {
            return new Shake();
        }
        if("slidedown".equalsIgnoreCase(name)){
            return new SlideDown();
        }
        if("slideup".equalsIgnoreCase(name)){
            return new SlideUp();
        }
        if("squish".equalsIgnoreCase(name)){
            return new Squish();
        }
        if("grow".equalsIgnoreCase(name)){
            return new Grow();
        }
        if("shrink".equalsIgnoreCase(name)){
            return new Shrink();
        }
        if("fold".equalsIgnoreCase(name)){
            return new Fold();
        }
        if("opacity".equalsIgnoreCase(name)){
            return new Opacity();
        }
        return null;
    }
}
