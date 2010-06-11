package org.icefaces.generator.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.icefaces.component.annotation.ActionSource;
import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Facet;
import org.icefaces.component.annotation.Facets;
import org.icefaces.component.annotation.Property;
import org.icefaces.generator.artifacts.Artifact;
import org.icefaces.generator.artifacts.ComponentArtifact;
import org.icefaces.generator.artifacts.ComponentHandlerArtifact;
import org.icefaces.generator.artifacts.TagArtifact;
import org.icefaces.generator.behavior.ActionSourceBehavior;
import org.icefaces.generator.behavior.Behavior;

public class ComponentContext {
	private Map<String, Field> fieldsForComponentClass = new HashMap<String, Field>();
    private Map<String, Field> internalFieldsForComponentClass = new HashMap<String, Field>();
    private Map<String, Field> fieldsForFacet = new HashMap<String, Field>();    
    private Map<String, Field> fieldsForTagClass = new HashMap<String, Field>();
    private Map<String, Artifact> artifacts = new HashMap<String, Artifact>();
    private Class activeClass;
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    
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

	public void setFieldsForTagClass(Map<String, Field> fieldsForTagClass) {
		this.fieldsForTagClass = fieldsForTagClass;
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
            //first get all properties this annotated component wants to include
            //these properties should go to the component as well as tag class
            String[] props = component.includeProperties();
            
            for (int i=0; i < props.length; i++) {
                //check if any included property is defined in the property template
                 Object prop = GeneratorContext.getInstance().getPropertyTemplate().get(props[i]);
                 
                 //include property is referencing list of properties add all
                 if (prop instanceof List) {
                     Iterator<String> iterator = ((List)prop).iterator();
                     while(iterator.hasNext()) {
                         String propName = iterator.next();
                         if (!fieldsForComponentClass.containsKey(propName)) {
                             fieldsForComponentClass.put(propName, (Field)GeneratorContext.getInstance().getPropertyTemplate().get(propName));
                         }
                         if (!fieldsForTagClass.containsKey(propName)) {                       
                             fieldsForTagClass.put(propName, (Field)GeneratorContext.getInstance().getPropertyTemplate().get(propName));
                         }                              
                         
                     }
                  //include properties are defining single property   
                 } else {
                     Field field = ((Field)prop);
                     if (!fieldsForComponentClass.containsKey(field.getName())) {                       
                         fieldsForComponentClass.put(field.getName(), field);
                     }                         
                     if (!fieldsForTagClass.containsKey(field.getName())) {                       
                         fieldsForTagClass.put(field.getName(), field);
                     }                      
                 }
            }
            //now we have done with include properties, now get all properties which 
            //are define on the annotated component itself.
            
            Field[] fields = clazz.getDeclaredFields();
            for (int i=0; i<fields.length; i++) {
                Field field = fields[i];
                if(field.isAnnotationPresent(Property.class)){
                    Property property = (Property) field.getAnnotation(Property.class);
                   //inherited properties should go to the tag class only
                    if (property.inherit()) {
                        if (!fieldsForTagClass.containsKey(field.getName())) {                       
                            fieldsForTagClass.put(field.getName(), field);
                        }                              
                    } else {//annotated properties defined on the component should 
                        //go to the component as well as tag class
                        
                        
                        if (!fieldsForComponentClass.containsKey(field.getName())) { 
                            if (property.isMethodExpression()) {
                                hasMethodExpression = true;
                            }
                            fieldsForComponentClass.put(field.getName(), field);
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
        
      if (clazz.getSuperclass() != null) {
          processAnnotation(clazz.getSuperclass(), false);
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
