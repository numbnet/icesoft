/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

public abstract class MetaContext {
    protected Map<String, Artifact> artifacts = new HashMap<String, Artifact>();
    protected Class activeClass;
	protected Map<Field, PropertyValues> propertyValuesMap = new HashMap<Field, PropertyValues>();

	protected boolean hasMethodExpression;
	
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

	public MetaContext(Class clazz) {
		GeneratorContext.getInstance().setActiveMetaContext(this);
		setActiveClass(clazz);
	}
	
	protected static PropertyValues collectPropertyValues(String fieldName, Class clazz) {
		return collectPropertyValues(fieldName, clazz, new PropertyValues(), true);
	}
	
	protected static PropertyValues collectPropertyValues(String fieldName, Class clazz, PropertyValues propertyValues, boolean isBaseClass) {
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
	
	protected static void setDefaultValues(PropertyValues propertyValues) {
	
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
			propertyValues.javadocGet = propertyValues.tlddoc;
		}
		if (propertyValues.javadocSet.equals(Property.Null)) {
			propertyValues.javadocSet = propertyValues.tlddoc;
		}
		if (propertyValues.required == Required.UNSET) {
			propertyValues.required = Required.DEFAULT;
		}
		if (propertyValues.implementation == Implementation.UNSET) {
			propertyValues.implementation = Implementation.DEFAULT;
		}
	}
	
	protected static Field[] getDeclaredFields(Class clazz) {
		return getDeclaredFields(clazz, new HashMap<String, Field>());
	}
	
	// collect all declared fields of a class and all its ancestor classes
	protected static Field[] getDeclaredFields(Class clazz, Map<String, Field> fields) {
		
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
