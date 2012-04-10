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

package org.icefaces.generator.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


import org.icefaces.component.annotation.*;
import org.icefaces.generator.artifacts.Artifact;
import org.icefaces.generator.artifacts.ComponentArtifact;
import org.icefaces.generator.artifacts.ComponentHandlerArtifact;
import org.icefaces.generator.artifacts.TagArtifact;
import org.icefaces.generator.behavior.Behavior;
import org.icefaces.generator.utils.PropertyValues;

public class ComponentContext {
	private Map<String, Field> fieldsForComponentClass = new HashMap<String, Field>();
    private Map<String, Field> internalFieldsForComponentClass = new HashMap<String, Field>();
    private Map<String, Field> fieldsForFacet = new HashMap<String, Field>();    
    private Map<String, Field> fieldsForTagClass = new HashMap<String, Field>();
    private Map<String, Artifact> artifacts = new HashMap<String, Artifact>();
    private Class activeClass;
    private List<Behavior> behaviors = new ArrayList<Behavior>();
	private Map<Field, PropertyValues> propertyValuesMap = new HashMap<Field, PropertyValues>();
    
    public List<Behavior> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(List<Behavior> behaviors) {
		this.behaviors = behaviors;
	}

	private boolean hasMethodExpression;
    
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
	
    public Map<Field, PropertyValues> getPropertyValuesMap() {
		return propertyValuesMap;
	}

	public boolean isHasMethodExpression() {
		return hasMethodExpression;
	}

	public void setHasMethodExpression(boolean hasMethodExpression) {
		this.hasMethodExpression = hasMethodExpression;
	}

	public Artifact getArtifact(Class<? extends Artifact> artifact ) {
		return artifacts.get(artifact.getSimpleName());
	}
	
	public Class getActiveClass() {
		return activeClass;
	}

	public void setActiveClass(Class activeClass) {
		this.activeClass = activeClass;
	}

	public Iterator<Artifact> getArtifacts() {
		return artifacts.values().iterator();
	}

	public ComponentContext(Class clazz) {
		GeneratorContext.getInstance().setActiveComponentContext(this);
		setActiveClass(clazz);
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
                }  else if (field.isAnnotationPresent(org.icefaces.component.annotation.Field.class)) {
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
	
	private static PropertyValues collectPropertyValues(String fieldName, Class clazz) {
		return collectPropertyValues(fieldName, clazz, new PropertyValues(), true);
	}
	
	private static PropertyValues collectPropertyValues(String fieldName, Class clazz, PropertyValues propertyValues, boolean isBaseClass) {
		Class superClass = clazz.getSuperclass();
		if (superClass != null) {
			boolean inherit = true;
			try {
				// if isBaseClass check for implementation()... otherwise, always go up
				if (isBaseClass) {
					Field field = clazz.getDeclaredField(fieldName);
					if (field.isAnnotationPresent(Property.class)) {
						Property property = (Property) field.getAnnotation(Property.class);
						inherit = property.implementation() != Implementation.GENERATE;
					}
				}
			} catch (NoSuchFieldException e) {
				// do nothing
			}
			
			if (inherit) {
				collectPropertyValues(fieldName, superClass, propertyValues, false);
			}
		}
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if (field.isAnnotationPresent(Property.class)) {
				if (!isBaseClass) {
					propertyValues.overrides = true;
				}
				Property property = (Property) field.getAnnotation(Property.class);
				if (property.expression() != Expression.UNSET) {
					propertyValues.expression = property.expression();
				}
				if (!property.methodExpressionArgument().equals(Property.Null)) {
					propertyValues.methodExpressionArgument = property.methodExpressionArgument();
				}
				if (!property.defaultValue().equals(Property.Null)) {
					propertyValues.defaultValue = property.defaultValue();
				}
				if (property.defaultValueType() != DefaultValueType.UNSET) {
					propertyValues.defaultValueType = property.defaultValueType();
				}
				if (!property.tlddoc().equals(Property.Null)) {
					propertyValues.tlddoc = property.tlddoc();
				}
				if (!property.javadocGet().equals(Property.Null)) {
					propertyValues.javadocGet = property.javadocGet();
				}
				if (!property.javadocSet().equals(Property.Null)) {
					propertyValues.javadocSet = property.javadocSet();
				}
				if (property.required() != Required.UNSET) {
					propertyValues.required = property.required();
				}
				if (property.implementation() != Implementation.UNSET) {
					propertyValues.implementation = property.implementation();
				}
                if (!property.name().equals(Property.Null)) {
                    propertyValues.name = property.name();
                } 
			}
		} catch (NoSuchFieldException e) {
			// do nothing
		}
		return propertyValues;
	}
	
	private static void setDefaultValues(PropertyValues propertyValues) {
	
		if (propertyValues.expression == Expression.UNSET) {
			propertyValues.expression = Expression.DEFAULT;
		}
		if (propertyValues.methodExpressionArgument.equals(Property.Null)) {
			propertyValues.methodExpressionArgument = "";
		}
		if (propertyValues.defaultValue.equals(Property.Null)) {
			propertyValues.defaultValue = "null";
		}
		if (propertyValues.defaultValueType == DefaultValueType.UNSET) {
			propertyValues.defaultValueType = DefaultValueType.DEFAULT;
		}
		if (propertyValues.tlddoc.equals(Property.Null)) {
			propertyValues.tlddoc = "";
		}
		if (propertyValues.javadocGet.equals(Property.Null)) {
			propertyValues.javadocGet = "";
		}
		if (propertyValues.javadocSet.equals(Property.Null)) {
			propertyValues.javadocSet = "";
		}
		if (propertyValues.required == Required.UNSET) {
			propertyValues.required = Required.DEFAULT;
		}
		if (propertyValues.implementation == Implementation.UNSET) {
			propertyValues.implementation = Implementation.DEFAULT;
		}
	}
	
	private static Field[] getDeclaredFields(Class clazz) {
		return getDeclaredFields(clazz, new HashMap<String, Field>());
	}
	
	// collect all declared fields of a class and all its ancestor classes
	private static Field[] getDeclaredFields(Class clazz, Map<String, Field> fields) {
		
		if (fields == null) {
			fields = new HashMap<String, Field>();
		}
		
		// add fields to map
		Field[] localFields = clazz.getDeclaredFields();
		for (int i = 0; i < localFields.length; i++) {
			if (!fields.containsKey(localFields[i].getName())) {
				fields.put(localFields[i].getName(), localFields[i]);
			}
		}
	
		Class superClass = clazz.getSuperclass();
		if (superClass != null) {
			return getDeclaredFields(superClass, fields);
		} else {
			Object[] values = fields.values().toArray();
			Field[] result = new Field[values.length];
			for (int i = 0; i < values.length; i++) {
				result[i] = (Field) values[i];
			}
			return result;
		}
	}
}
