package org.icefaces.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Facet;

public class ComponentClassGenerator {
    static StringBuilder generatedComponentClass;
    static List<Field> generatedComponentProperties = new ArrayList<Field>();
  
    static void startComponentClass(Class clazz, Component component) {
        //initialize
        generatedComponentClass = new StringBuilder();
        generatedComponentProperties = new ArrayList<Field>();
        
        // add entry to faces-config
        Generator.facesConfigBuilder.addEntry(clazz, component);
        Generator.faceletTagLibBuilder.addTagInfo(clazz, component);
        int classIndicator = Generator.getClassName(component).lastIndexOf(".");
        generatedComponentClass.append("package ");
        generatedComponentClass.append(Generator.getClassName(component).substring(0, classIndicator));
        generatedComponentClass.append(";\n\n");
        generatedComponentClass.append("import java.io.IOException;\n");
        generatedComponentClass.append("import java.util.List;\n");
        generatedComponentClass.append("import java.util.ArrayList;\n");
        generatedComponentClass.append("import java.util.Arrays;\n\n");
        generatedComponentClass.append("import javax.faces.context.FacesContext;\n");
        generatedComponentClass.append("import javax.el.MethodExpression;\n");
        generatedComponentClass.append("import javax.el.ValueExpression;\n\n");
        generatedComponentClass.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
        
        
        generatedComponentClass.append("public class ");
        generatedComponentClass.append(Generator.getClassName(component).substring(classIndicator+1));
        generatedComponentClass.append(" extends ");
        generatedComponentClass.append(component.extendsClass());
        generatedComponentClass.append("{\n");

        generatedComponentClass.append("\n\tpublic static final String COMPONENT_TYPE = \""+ component.componentType() + "\";");
        String rendererType = null;
        if (!"".equals(component.rendererType())) {
            rendererType = "\""+ component.rendererType() + "\"";
        }
        
        generatedComponentClass.append("\n\tpublic static final String RENDERER_TYPE = "+ rendererType + ";\n");
        
        generatedComponentClass.append("\n\tpublic String getFamily() {\n\t\treturn \"");
        generatedComponentClass.append(Generator.getFamily(component));
        generatedComponentClass.append("\";\n\t}\n\n");
    }

    
    static void endComponentClass() {
        generatedComponentClass.append("\n}");
        createJavaFile();

    }
    
    static void createJavaFile() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        String componentClass = Generator.getClassName(component);
        String fileName = componentClass.substring(componentClass.lastIndexOf('.')+1) + ".java";
        String pack = componentClass.substring(0, componentClass.lastIndexOf('.'));
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        FileWriter.write(fileName, path, generatedComponentClass);        
    }

    static void addProperties(Map<String, Field> properties) {
      Iterator<Field> fields = properties.values().iterator();
      while(fields.hasNext()) {
          Field field = fields.next();
          if(field.isAnnotationPresent(Property.class)){
              generatedComponentProperties.add(field);
          }
      }
      addPropertyEnum();
      addGetterSetter();
    }
       
    static void addPropertyEnum() {

        generatedComponentClass.append("\n\tprotected enum PropertyKeys {\n");        
        for (int i = 0; i < generatedComponentProperties.size(); i++){
            generatedComponentClass.append("\t\t");     
            generatedComponentClass.append(generatedComponentProperties.get(i).getName()); 
            generatedComponentClass.append(",\n");            
        }
        generatedComponentClass.append("\t\t;\n");
        generatedComponentClass.append("\t\tString toString;\n"); 
        generatedComponentClass.append("\t\tPropertyKeys(String toString) { this.toString = toString; }\n");        
        generatedComponentClass.append("\t\tPropertyKeys() { }\n"); 
        generatedComponentClass.append("\t\tpublic String toString() {\n");       
        generatedComponentClass.append("\t\t\treturn ((toString != null) ? toString : super.toString());\n");       
        generatedComponentClass.append("\t\t}\n\t}\n");   
    }
    
    static void addGetterSetter() {
        for (int i = 0; i < generatedComponentProperties.size(); i++) {
                  Field field = generatedComponentProperties.get(i);
                  Property prop = (Property)field.getAnnotation(Property.class);
                 
                  //set
 
                  
                  addJavaDoc(field.getName(), true, prop.javadocSet());
                  generatedComponentClass.append("\tpublic void set");
                  generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
                  generatedComponentClass.append(field.getName().substring(1));
  
                  generatedComponentClass.append("(");
                  generatedComponentClass.append(field.getType().getName());
                  generatedComponentClass.append(" ");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(") {\n\t\tgetStateHelper().put(PropertyKeys.");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(", ");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(");\n");
                  generatedComponentClass.append("\t\t//handleAttribute(\"");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append("\", ");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(");\n");
                  generatedComponentClass.append("\t}\n");
                  
                  
                  
                  //get
   
                  
                  addJavaDoc(field.getName(), false, prop.javadocGet());
                  generatedComponentClass.append("\tpublic ");
                  generatedComponentClass.append(field.getType().getName());
                  generatedComponentClass.append(" ");
                  if (field.getType().getName().endsWith("boolean")||
                          field.getType().getName().endsWith("java.lang.Boolean")) {
                      generatedComponentClass.append("is");
                  } else {
                      generatedComponentClass.append("get");                    
                  }
                  generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
                  generatedComponentClass.append(field.getName().substring(1));
                  generatedComponentClass.append("() {\n");
                  generatedComponentClass.append("\t\t return (");
                  generatedComponentClass.append(field.getType().getName());
                  generatedComponentClass.append(") getStateHelper().eval(PropertyKeys.");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(");\n\t}\n");
        }
    }
    
    static void addJavaDoc(String name, boolean isSetter, String doc) {
        generatedComponentClass.append("\n\t/**\n");
        if (isSetter) {
            generatedComponentClass.append("\t* <p>Set the value of the <code>");
        } else {
            generatedComponentClass.append("\t* <p>Return the value of the <code>");            
        }
        generatedComponentClass.append(name);
        generatedComponentClass.append("</code> property.</p>");                
        if (doc != null && !"".equals(doc)) {
            String[] lines = doc.split("\n");
            generatedComponentClass.append("\n\t* <p>Contents:");
             
            for (int j=0; j < lines.length; j++){
                if (j>0) {
                    generatedComponentClass.append("\n\t* ");
                }
                generatedComponentClass.append(lines[j]);
                if (j == (lines.length-1)) {
                    generatedComponentClass.append("</p>");
                }                        
            }
        } 
        generatedComponentClass.append("\n\t*/\n");         
    }
    
  
    
    static void addFacet(Class clazz, Component component) {
        Iterator<Field> iterator = Generator.fieldsForFacet.values().iterator();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            Facet facet = (Facet)field.getAnnotation(Facet.class);
            String facetName = field.getName();
            addJavaDoc(field.getName() + " facet", true, facet.javadocSet());
            generatedComponentClass.append("\tpublic void set");
            generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
            generatedComponentClass.append(field.getName().substring(1));
            generatedComponentClass.append("Facet");

            generatedComponentClass.append("(");
            generatedComponentClass.append(field.getType().getName());
            generatedComponentClass.append(" ");
            generatedComponentClass.append(field.getName());
            generatedComponentClass.append(") {\n\t\tgetFacets().put(\"");
            generatedComponentClass.append(facetName);
            generatedComponentClass.append("\", ");
            generatedComponentClass.append(field.getName());
            generatedComponentClass.append(");\n");
            generatedComponentClass.append("\t}\n");
            
            
            //getter
            addJavaDoc(field.getName() + " facet", false, facet.javadocGet());
            generatedComponentClass.append("\tpublic ");
            generatedComponentClass.append(field.getType().getName());
            generatedComponentClass.append(" ");
            generatedComponentClass.append("get");                    
            generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
            generatedComponentClass.append(field.getName().substring(1));
            generatedComponentClass.append("Facet");
            generatedComponentClass.append("() {\n");
            generatedComponentClass.append("\t\t return getFacet(\"");
            generatedComponentClass.append(facetName);
            generatedComponentClass.append("\");\n\t}\n");            
            
        }

    }
    
    static void create() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        ComponentClassGenerator.startComponentClass(Generator.currentClass, component);
        ComponentClassGenerator.addProperties(Generator.fieldsForComponentClass);  
        ComponentClassGenerator.addFacet(Generator.currentClass, component);
        ComponentClassGenerator.endComponentClass();
    }
}
