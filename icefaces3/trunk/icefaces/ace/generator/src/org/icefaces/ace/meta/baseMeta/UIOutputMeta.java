/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.meta.baseMeta;

import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Implementation;

import javax.faces.convert.Converter;

/**
 * These are the properties for javax.faces.component.UIOutput
 */
public class UIOutputMeta extends UIComponentBaseMeta {

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="The current value of the simple component. The value to be " +
            "rendered.")
    private Object value;

    @Property (implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Converter is an interface describing a Java class that can " +
            "perform Object-to-String and String-to-Object conversions " +
            "between model data objects and a String representation of " +
            "those objects that is suitable for rendering.")
    private Converter converter;
}
