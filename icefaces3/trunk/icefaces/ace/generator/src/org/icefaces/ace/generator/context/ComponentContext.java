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

public class ComponentContext extends MetaContext {
	private Map<String, Field> fieldsForComponentClass = new HashMap<String, Field>();
    private Map<String, Field> internalFieldsForComponentClass = new HashMap<String, Field>();
    private Map<String, Field> fieldsForFacet = new HashMap<String, Field>();    
    private Map<String, Field> fieldsForTagClass = new HashMap<String, Field>();
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    
    public List<Behavior> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(List<Behavior> behaviors) {
		this.behaviors = behaviors;
	}
    
    public Map<String, Field> getFieldsForComponentClass() {
		return fieldsForComponentClass;
	}
    
    public List<Field> getPropertyFieldsForComponentClassAsList() {
        ArrayList<Field> ret = new ArrayList<Field>(fieldsForComponentClass.size()+1);
		Iterator<Field> fields = fieldsForComponentClass.values().iterator();
		while(fields.hasNext()) {
			Field field = fields.next();
			if(field.isAnnotationPresent(Property.class)){
				ret.add(field);
			}
		}
        return ret;
    }

	public void setFieldsForComponentClass(
			Map<String, Field> fieldsForComponentClass) {
		this.fieldsForComponentClass = fieldsForComponentClass;
	}

	public Map<String, Field> getInternalFieldsForComponentClass() {
		return internalFieldsForComponentClass;
	}

	public void setInternalFieldsForComponentClass(
			Map<String, Field> internalFieldsForComponentClass) {
		this.internalFieldsForComponentClass = internalFieldsForComponentClass;
	}

	public Map<String, Field> getFieldsForFacet() {
		return fieldsForFacet;
	}

	public void setFieldsForFacet(Map<String, Field> fieldsForFacet) {
		this.fieldsForFacet = fieldsForFacet;
	}

	public Map<String, Field> getFieldsForTagClass() {
		return fieldsForTagClass;
	}

    /**
     * Alphabetically sorted keys from fieldsForTagClass
     */
    public Iterator<String> getFieldNamesForTagClass() {
        TreeSet<String> treeSet = new TreeSet<String>(fieldsForTagClass.keySet());
        return treeSet.iterator();
    }

	public void setFieldsForTagClass(Map<String, Field> fieldsForTagClass) {
		this.fieldsForTagClass = fieldsForTagClass;
	}

	public ComponentContext(Class clazz) {
		super(clazz);
    	processAnnotation(clazz, true);
    	
		artifacts.put(ComponentArtifact.class.getSimpleName(), new ComponentArtifact(this));
		artifacts.put(TagArtifact.class.getSimpleName(), new TagArtifact(this));
		artifacts.put(ComponentHandlerArtifact.class.getName(), new ComponentHandlerArtifact(this));

    	for (Behavior behavior: GeneratorContext.getInstance().getBehaviors()) {
    		if (behavior.hasBehavior(clazz)) {
    			System.out.println("Behavior found ");
    			//attach behavior to the component context
    			getBehaviors().add(behavior);
    			behavior.addProperties(this);
    		}
    	}
	}
    
    private void processAnnotation(Class clazz, boolean isBaseClass) {

        //This is annotated class 
        if (clazz.isAnnotationPresent(Component.class)) {
            Component component = (Component) clazz.getAnnotation(Component.class);
            System.out.println(clazz.getDeclaredClasses());
			
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
					
                   //inherited properties should go to the tag class only
                    if (propertyValues.implementation == Implementation.EXISTS_IN_SUPERCLASS) {
                        if (!fieldsForTagClass.containsKey(field.getName())) {                       
                            fieldsForTagClass.put(field.getName(), field);
                        }                              
                    } else {//annotated properties defined on the component should 
                        //go to the component as well as tag class
                        
						// if it's a local field... 
                        if (localFieldsSet.contains(field)) {
							boolean modifiesDefaultValueOrMethodExpression = false;
							boolean modifiesJavadoc = false;
							if (property.expression() != Expression.UNSET
								|| !property.methodExpressionArgument().equals(Property.Null)
								|| !property.defaultValue().equals(Property.Null)
								|| property.defaultValueType() != DefaultValueType.UNSET) {
								modifiesDefaultValueOrMethodExpression = true;
							}
							if (!property.javadocGet().equals(Property.Null) || !property.javadocSet().equals(Property.Null)) {
								modifiesJavadoc = true;
							}
							
							// if property doesn't exist in ancestor classes or if one of the 6 fields above was modified, then add to component class
							if (!propertyValues.overrides || modifiesDefaultValueOrMethodExpression || modifiesJavadoc) {
								if (!fieldsForComponentClass.containsKey(field.getName())) { 
									if (propertyValues.expression == Expression.METHOD_EXPRESSION) {
										hasMethodExpression = true;
									}
									fieldsForComponentClass.put(field.getName(), field);
								} 
							}
							// if only javadocGet or javadocSet were specified, then simply create delegating getter/setter and do not include in save state
							if (modifiesDefaultValueOrMethodExpression == false && modifiesJavadoc == true) {
								propertyValues.isDelegatingProperty = true;
							}
						}
                        if (!fieldsForTagClass.containsKey(field.getName())) {                       
                            fieldsForTagClass.put(field.getName(), field);
                        }                           
                    }
                }  else if (field.isAnnotationPresent(org.icefaces.ace.meta.annotation.Field.class)) {
                    internalFieldsForComponentClass.put(field.getName(), field);
                }
            }
        } 

      processFacets(clazz);
  }    
    
    private void processFacets(Class clazz){
        Class[] classes = clazz.getDeclaredClasses();
        for (int i=0; i < classes.length; i++) {
            if (classes[i].isAnnotationPresent(Facets.class)) {
                Field[] fields = classes[i].getDeclaredFields();
                for (int f=0; f < fields.length; f++) {
                    Field field = fields[f];
                    if (field.isAnnotationPresent(Facet.class)) {
                        fieldsForFacet.put(field.getName(), field);
//                        System.out.println("Facet property"+ fields[f].getName());
                    }

                }

            }
        }
    } 
}
