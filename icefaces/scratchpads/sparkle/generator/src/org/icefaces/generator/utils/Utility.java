package org.icefaces.generator.utils;

import java.lang.reflect.Field;

import org.icefaces.component.annotation.Component;

public class Utility {
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
}
