package org.icefaces.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.icefaces.generator.xmlbuilder.FaceletTagLibBuilder;
import org.icefaces.generator.xmlbuilder.FacesConfigBuilder;
import org.icefaces.generator.xmlbuilder.TLDBuilder;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Facet;
import org.icefaces.component.annotation.Facets;
import org.icefaces.component.annotation.PropertyTemplate;

public class Generator {
    static List<Class> components = null;
    static Class currentClass;
    
    //will be set by the Generator.processAnnotation(), will help in identifying
    //whether the componentHandler is required or not.
    public static boolean currentClassHasMethodExpression;
    
    static Map< Field, Property> currentFields = new HashMap<Field, Property>();
    static Map<String, Property> inheritedProperties = new HashMap<String, Property>();
    static TLDBuilder tldBuilder = new TLDBuilder();
    static FacesConfigBuilder facesConfigBuilder = new FacesConfigBuilder(); 
    static FaceletTagLibBuilder faceletTagLibBuilder = new FaceletTagLibBuilder();
    static Map propertyTemplate = new HashMap();
    static Map<String, Field> fieldsForComponentClass = new HashMap<String, Field>();
    static Map<String, Field> internalFieldsForComponentClass = new HashMap<String, Field>();
    static Map<String, Field> fieldsForFacet = new HashMap<String, Field>();    
    static Map<String, Field> fieldsForTagClass = new HashMap<String, Field>();
    static Map<String,String> WrapperTypes= new HashMap<String, String>();
    public final static String shortName = "ann";
    public final static String namespace = "http://www.icesoft.com/icefaces/component/annotated";
    static {
        components = FileWriter.getAnnotatedCompsList();
        WrapperTypes.put("java.lang.Boolean", "boolean");
        WrapperTypes.put("java.lang.Byte", "byte");
        WrapperTypes.put("java.lang.Character", "char");
        WrapperTypes.put("java.lang.Double", "double");
        WrapperTypes.put("java.lang.Float", "float");
        WrapperTypes.put("java.lang.Integer", "int");
        WrapperTypes.put("java.lang.Long", "long");
        WrapperTypes.put("java.lang.Short", "short");
        
    }
    

    public static void main(String[] a) {
        System.out.println("Generator starts.....");        
        loadPropertyTemplate();
        Iterator<Class> iterator = components.iterator();
        while (iterator.hasNext()) {
            processClass(iterator.next());
        }
        tldBuilder.write();
        facesConfigBuilder.write();
        faceletTagLibBuilder.write();
    }
    
    static void processClass(Class clazz) {

        currentClass = clazz;
        processAnnotation(currentClass, true);
        //by now all properties should be set into the "fieldsForTagAndComponentClasses",
        //so lets create component and tag file
        ComponentClassGenerator.create();
        ComponentHandlerGenerator.create();
        TagClassGenerator.create();
        
        //clean the attribute list up, for next available component 
        cleanup();
        
        System.out.println(ComponentClassGenerator.generatedComponentClass.toString());
        System.out.println();
        System.out.println(TagClassGenerator.generatedTagClass.toString());            

    }
    
    static void cleanup() {
        fieldsForComponentClass.clear();
        internalFieldsForComponentClass.clear();
        fieldsForTagClass.clear(); 
        fieldsForFacet.clear();
        currentClassHasMethodExpression = false;
    }
    /**
     * This method should prepare the properties:
     * 1- Check if the class has annotation, if yes then
     *    - add the includeProperties into the property list. that will be used for component class as well as tagClass.
     *    - add annotated properties into the propert list.
     * else (it means its Sun's class):
     *       - add all properties to the baseclass property list 
     *       
     *  
     * @param clazz
     * @param isBaseClass
     */
    static void processAnnotation(Class clazz, boolean isBaseClass) {
          //This is annotated class 
          if (clazz.isAnnotationPresent(Component.class)) {
              Component component = (Component) clazz.getAnnotation(Component.class);
              System.out.println(clazz.getDeclaredClasses());
              //first get all properties this annotated component wants to include
              //these properties should go to the component as well as tag class
              String[] props = component.includeProperties();
              
              for (int i=0; i < props.length; i++) {
                  //check if any included property is defined in the property template
                   Object prop = propertyTemplate.get(props[i]);
                   
                   //include property is referencing list of properties add all
                   if (prop instanceof List) {
                       Iterator<String> iterator = ((List)prop).iterator();
                       while(iterator.hasNext()) {
                           String propName = iterator.next();
                           if (!fieldsForComponentClass.containsKey(propName)) {
                               fieldsForComponentClass.put(propName, (Field)propertyTemplate.get(propName));
                           }
                           if (!fieldsForTagClass.containsKey(propName)) {                       
                               fieldsForTagClass.put(propName, (Field)propertyTemplate.get(propName));
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
                                  currentClassHasMethodExpression = true;
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
    
    //this method loads predefine properties 
    static void loadPropertyTemplate() {
        Field[] fields = PropertyTemplate.class.getDeclaredFields();
        for (int i=0; i < fields.length; i++) {
            //its mean "public static", must be sun class name and its properties
            if (fields[i].getModifiers() == 9) {
                try {
                    
                    propertyTemplate.put(fields[i].getName(), fields[i].get(fields[i]));
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }            
            } else {
                propertyTemplate.put(fields[i].getName(), fields[i]);   
            }
        }
    }
    
    public static String getClassName(Component component) {
        String generatedClass = component.generatedClass();
        if (generatedClass.equals("")) {
            generatedClass = component.componentClass();
        } 
        return generatedClass;
    }
    
    public static String getTagClassName(Component component) {
        return component.componentClass();
    } 
    
    public static String getComponentType(Component component) {
        String componentType = component.componentType();
        if ("".equals(componentType)) {
            try {
                Class extended = Class.forName(component.extendsClass());
                Field comp_type = extended.getField("COMPONENT_TYPE");
                componentType = String.valueOf(comp_type.get(comp_type));            
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return componentType;
    }
    
    public static String getRendererType(Component component) {
        String rendererType = component.rendererType();
        if ("".equals(rendererType)) {
            try {
                Class extended = Class.forName(component.extendsClass());
                Field renderer_type = extended.getDeclaredField("RENDERER_TYPE");
                rendererType = String.valueOf(renderer_type.get(renderer_type));            
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return rendererType;
    }
    
    public static String getFamily(Component component) {
        String componentFamily = component.componentFamily();
        if ("".equals(componentFamily)) {
            try {
                Class extended = Class.forName(component.extendsClass());
                Field comp_family = extended.getField("COMPONENT_FAMILY");
                componentFamily = String.valueOf(comp_family.get(comp_family));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return componentFamily;
    }
    
    private static void processFacets(Class clazz){
        Class[] classes = clazz.getDeclaredClasses();
        for (int i=0; i < classes.length; i++) {
            if (classes[i].isAnnotationPresent(Facets.class)) {
                Field[] fields = classes[i].getDeclaredFields();
                for (int f=0; f < fields.length; f++) {
                    Field field = fields[f];
                    if (field.isAnnotationPresent(Facet.class)) {
                        fieldsForFacet.put(field.getName(), field);
                        System.out.println("Facet property"+ fields[f].getName());
                    }

                }

            }
        }
    }
}
