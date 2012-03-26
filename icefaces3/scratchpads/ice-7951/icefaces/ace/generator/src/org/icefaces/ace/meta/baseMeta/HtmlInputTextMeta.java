/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.meta.baseMeta;

import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Implementation;

/**
 * These are the properties for javax.faces.component.html.HtmlInputText
 */
public class HtmlInputTextMeta extends UIInputMeta {

	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String accesskey;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String alt;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String autocomplete;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String dir;
	
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private boolean disabled;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String label;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String lang;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private int maxlength;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onblur;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onchange;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onclick;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String ondblclick;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onfocus;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onkeydown;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onkeypress;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onkeyup;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onmousedown;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onmousemove;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onmouseout;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onmouseover;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onmouseup;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String onselect;
	
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private boolean readonly;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private int size;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String style;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String styleClass;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String tabindex;
	
	@Property(implementation= Implementation.EXISTS_IN_SUPERCLASS)
	private String title;
}
