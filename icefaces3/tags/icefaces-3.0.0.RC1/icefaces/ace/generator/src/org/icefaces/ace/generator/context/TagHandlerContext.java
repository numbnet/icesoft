/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.generator.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


import org.icefaces.ace.generator.artifacts.Artifact;
import org.icefaces.ace.generator.artifacts.ComponentArtifact;
import org.icefaces.ace.generator.artifacts.ComponentHandlerArtifact;
import org.icefaces.ace.generator.artifacts.TagArtifact;
import org.icefaces.ace.generator.artifacts.TagHandlerArtifact;
import org.icefaces.ace.generator.behavior.Behavior;
import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.*;

public class TagHandlerContext extends MetaContext {
	private Map<String, Field> fieldsForTagHandlerClass = new HashMap<String, Field>();
        
    public Map<String, Field> getFieldsForTagHandlerClass() {
		return fieldsForTagHandlerClass;
	}
    
    public List<Field> getPropertyFieldsForTagHandlerClassAsList() {
        ArrayList<Field> ret = new ArrayList<Field>(fieldsForTagHandlerClass.size()+1);
		Iterator<Field> fields = fieldsForTagHandlerClass.values().iterator();
		while(fields.hasNext()) {
			Field field = fields.next();
			if(field.isAnnotationPresent(Property.class)){
				ret.add(field);
			}
		}
        return ret;
    }

	public void setFieldsForComponentClass(
			Map<String, Field> fieldsForTagHandlerClass) {
		this.fieldsForTagHandlerClass = fieldsForTagHandlerClass;
	}

	public TagHandlerContext(Class clazz) {
		super(clazz);
    	processAnnotation(clazz, true);
    	
		artifacts.put(TagHandlerArtifact.class.getName(), new TagHandlerArtifact(this));
	}
    
    private void processAnnotation(Class clazz, boolean isBaseClass) {

        //This is annotated class 
        if (clazz.isAnnotationPresent(TagHandler.class)) {
			
			// original fields
			Field[] localFields = clazz.getDeclaredFields();
			HashSet<Field> localFieldsSet = new HashSet<Field>();
			
            for (int i=0; i < localFields.length; i++) {
				localFieldsSet.add(localFields[i]);
            }
			
			// disinherit properties
			//String[] disinheritProperties = component.disinheritProperties();
			//HashSet<String> disinheritPropertiesSet = new HashSet<String>();
			
            //for (int i=0; i < disinheritProperties.length; i++) {
			//	disinheritPropertiesSet.add(disinheritProperties[i]);
            //}
			
            //get all properties which are defined on the annotated component itself.
            
			Field[] fields = getDeclaredFields(clazz);
            for (int i=0; i<fields.length; i++) {
                Field field = fields[i];
				// this functionality is suspended for now
				//if (disinheritPropertiesSet.contains(field.getName())) { // skip property if it's in disinheritProperties list
				//	continue;
				//}
				
                if(field.isAnnotationPresent(Property.class)){
                    Property property = (Property) field.getAnnotation(Property.class);
					PropertyValues propertyValues = collectPropertyValues(field.getName(), clazz); // collect @Property values from top to bottom
					setDefaultValues(propertyValues); // if values end up being UNSET, then set them to default
					propertyValuesMap.put(field, propertyValues);
					
					fieldsForTagHandlerClass.put(field.getName(), field);
				}
            }
        } 
  }
}
