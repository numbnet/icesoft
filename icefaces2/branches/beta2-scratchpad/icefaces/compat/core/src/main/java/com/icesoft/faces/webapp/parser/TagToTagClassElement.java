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

package com.icesoft.faces.webapp.parser;

import java.util.ArrayList;

/**
 * A simple class needed to process tag libraries when creating a
 * TagToComponentMap object.  This object is created by the digester to hold
 * relevant values.
 *
 * @author Steve Maryka
 */
public class TagToTagClassElement {
    /* An obect that we can use to digest <tag> entries in a tld */
    private String tagName;
    private String tagClass;
    private String description;
    private ArrayList<AttributeElement> attributes;

    public ArrayList<AttributeElement> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<AttributeElement> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute (AttributeElement a) {
		attributes.add(a);
	}

	/**
     * Constructor.
     */
    public TagToTagClassElement() {
        tagName = null;
        tagClass = null;
        attributes = new ArrayList<AttributeElement>();
    }

    /**
     * TagName getter.
     *
     * @return tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * TagClass getter
     *
     * @return tag class
     */
    public String getTagClass() {
        return tagClass;
    }
    
    public String getDescription() {
    	return description;
    }

    /**
     * TagName setter.
     *
     * @param name tag name
     */
    public void setTagName(String name) {
        tagName = name;
    }

    /**
     * TagClass setter.
     *
     * @param className tag class.
     */
    public void setTagClass(String className) {
        tagClass = className;
    }
    
    public void setDescription (String description) {
    	this.description = description;
    }
}    
