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
import org.icefaces.ace.meta.annotation.DefaultValueType;

/**
 * These are the properties for javax.faces.component.UIMessage
 */
public class UIMessageMeta extends UIComponentBaseMeta {
    @Property(//TODO ICE-6109
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Identifier of the component for which to render error " +
            "messages. If this component is within the same NamingContainer " +
            "as the target component, this must be the component " +
            "identifier. Otherwise, it must be an absolute component " +
            "identifier (starting with \":\").")
    private String forValue;

    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether the \"detail\" property of messages " +
            "for the specified component should be rendered.")
    private boolean showDetail;

    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether the \"summary\" property of " +
            "messages for the specified component should be rendered.")
    private boolean showSummary;
    
    //TODO Check Mojarra 2.0.3+ code to see if exists there
    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating this UIMessage instance should redisplay " +
            "FacesMessages that have already been handled.")
    private boolean redisplay;
}
