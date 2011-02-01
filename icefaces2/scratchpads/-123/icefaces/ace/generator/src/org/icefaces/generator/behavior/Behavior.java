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

package org.icefaces.generator.behavior;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.icefaces.component.annotation.Property;
import org.icefaces.generator.artifacts.ComponentArtifact;
import org.icefaces.generator.context.ComponentContext;


public abstract class Behavior {
	private Map<String, Field> properties = new HashMap<String, Field>();
	
	public Behavior(Class clazz) {
		for (Field field: clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Property.class)) {
				getProperties().put(field.getName(), field);
			}
		}
	}
	
	public Map<String, Field> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Field> properties) {
		this.properties = properties;
	}
	
	public boolean hasInterface() {
		return false;
	}
	public String getInterfaceName() {
		return null;
	}
	public abstract boolean hasBehavior(Class clazz);
	
	public void addImportsToComponent(StringBuilder stringBuilder ) {
		
	}
	
	public void addCodeToComponent(StringBuilder stringBuilder ) {
		
	}
	public void addImportsToTag(StringBuilder stringBuilder ) {
		
	}
	public void addProperties(ComponentContext componentContext ) {
		
	}
	public void addPropertiesEnumToComponent(StringBuilder output) {
		
	}
	
	public void addGetterSetter(ComponentArtifact artifact, StringBuilder output) {
		Iterator<Field> fields = getProperties().values().iterator();
		while (fields.hasNext()) {
			artifact.addGetterSetter(fields.next());
		}
	}	
}
