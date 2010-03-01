package org.icefaces.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

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
        generatedComponentClass.append("\n\tpublic static final String OPTIMIZED_PACKAGE = \""+ component.optimizedPackage() + "\";");        
        String rendererType = null;
        if (!"null".equals(component.rendererType())) {
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
        System.out.println("____________________________Creating component class_________________________");
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        String componentClass = Generator.getClassName(component);
        String fileName = componentClass.substring(componentClass.lastIndexOf('.')+1) + ".java";
        System.out.println("____FileName "+ fileName);
        String pack = componentClass.substring(0, componentClass.lastIndexOf('.'));
        System.out.println("____package "+ pack);        
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        System.out.println("____path "+ path);         
        FileWriter.write(fileName, path, generatedComponentClass);        
        System.out.println("____________________________Creating component class ends_________________________");
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
                 
                  boolean isBoolean = field.getType().getName().endsWith("boolean")||
                  field.getType().getName().endsWith("Boolean");
                                    
                  //set
                  addJavaDoc(field.getName(), true, prop.javadocSet());
                  generatedComponentClass.append("\tpublic void set");
                  generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
                  generatedComponentClass.append(field.getName().substring(1));
  
                  generatedComponentClass.append("(");
                  if (Generator.WrapperTypes.containsKey(field.getType().getName().trim())) {
                      System.out.println("TYPE FOUNDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                      generatedComponentClass.append(Generator.WrapperTypes.get(field.getType().getName()));
                  } else {System.out.println("TYPE NOOOOOOOOOOOOOOOOOOOOOOOOOOT");
                      generatedComponentClass.append(field.getType().getName());
                  }
                  generatedComponentClass.append(" ");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(") {\n\t\tgetStateHelper().put(PropertyKeys.");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(", ");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(");\n");
                  generatedComponentClass.append("\t\thandleAttribute(\"");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append("\", ");
                  generatedComponentClass.append(field.getName());
                  generatedComponentClass.append(");\n");
                  generatedComponentClass.append("\t}\n");
                  
                  
                  
                  //get   
                  
                  addJavaDoc(field.getName(), false, prop.javadocGet());

                  generatedComponentClass.append("\tpublic ");
                  if (isBoolean) {
                      generatedComponentClass.append("boolean");  
                  } else {
                      generatedComponentClass.append(field.getType().getName());
                  }
                  generatedComponentClass.append(" ");
           
                  
                  if (isBoolean) {
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
                  String defaultValue = prop.defaultValue();
                  if (!"null".equals(defaultValue)) {
                      generatedComponentClass.append(", ");
                      if (field.getType().getName().endsWith("String") &&
                              prop.defaultValueIsStringLiteral()) {
                          generatedComponentClass.append("\"");
                          generatedComponentClass.append(defaultValue);
                          generatedComponentClass.append("\"");
                      } else {
                          generatedComponentClass.append(defaultValue);
                      }
                  }                  
                  
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
    
    static void addInternalFields() {
        
        //write properties
        Iterator<Field> fields = Generator.internalFieldsForComponentClass.values().iterator();
        while (fields.hasNext()) {
            Field field = fields.next();
            generatedComponentClass.append("\tprotected ");
            generatedComponentClass.append(field.getType().getName());
            generatedComponentClass.append(" ");            
            generatedComponentClass.append(field.getName());
            String defaultValue = field.getAnnotation(org.icefaces.component.annotation.Field.class).defaultValue();
            boolean defaultValueIsStringLiteral = field.getAnnotation(org.icefaces.component.annotation.Field.class).defaultValueIsStringLiteral();            
            if (!"null".equals(defaultValue)) {
                generatedComponentClass.append(" = ");
                if (field.getType().getName().endsWith("String") &&
                                           defaultValueIsStringLiteral) {
                    generatedComponentClass.append("\"");
                    generatedComponentClass.append(defaultValue);
                    generatedComponentClass.append("\"");
                } else {
                    generatedComponentClass.append(defaultValue);
                }
            }
            generatedComponentClass.append(";\n");
         
        }
        
        //write saveState
        fields = Generator.internalFieldsForComponentClass.values().iterator();
        generatedComponentClass.append("\n\tprivate Object[] values;\n");
        generatedComponentClass.append("\n\tpublic Object saveState(FacesContext context) {\n");
        generatedComponentClass.append("\t\tif (context == null) {\n");
        generatedComponentClass.append("\t\t\tthrow new NullPointerException();\n\t\t}");
        generatedComponentClass.append("\n\t\tif (values == null) {\n");
        generatedComponentClass.append("\t\t\tvalues = new Object[");
        generatedComponentClass.append(Generator.internalFieldsForComponentClass.values().size()+1);
        generatedComponentClass.append("];\n\t\t}\n");
        generatedComponentClass.append("\t\tvalues[0] = super.saveState(context);\n");

            
        int i=1;
        while (fields.hasNext()) {
            Field field = fields.next();
            generatedComponentClass.append("\t\tvalues[");
            generatedComponentClass.append(i++);
            generatedComponentClass.append("] = ");            
            generatedComponentClass.append(field.getName());
            generatedComponentClass.append(";\n");
        }
        generatedComponentClass.append("\t\treturn (values);\n");   
        generatedComponentClass.append("\t}\n");
        
        

        //writer restoreState
        fields = Generator.internalFieldsForComponentClass.values().iterator();
        generatedComponentClass.append("\n\tpublic void restoreState(FacesContext context, Object state) {\n");
        generatedComponentClass.append("\t\tif (context == null) {\n");
        generatedComponentClass.append("\t\t\tthrow new NullPointerException();\n\t\t}");
        generatedComponentClass.append("\n\t\tif (state == null) {\n");
        generatedComponentClass.append("\t\t\treturn;\n\t\t}\n");
        generatedComponentClass.append("\t\tvalues = (Object[]) state;\n");
        generatedComponentClass.append("\t\tsuper.restoreState(context, values[0]);\n");

            
        i=1;
        while (fields.hasNext()) {
            Field field = fields.next();
            generatedComponentClass.append("\t\t");
            generatedComponentClass.append(field.getName());
            generatedComponentClass.append(" = ");
            generatedComponentClass.append("(");
            generatedComponentClass.append(field.getType().getName());
            generatedComponentClass.append(") values["); 
            generatedComponentClass.append(i++);
            generatedComponentClass.append("];\n");
        }
        generatedComponentClass.append("\t}\n");     
        
    }
    
    static void handleAttribute() {
        generatedComponentClass.append("\t\tprivate void handleAttribute(String name, Object value) {\n");
        generatedComponentClass.append("\t\t\tList<String> setAttributes = (List<String>) this.getAttributes().get(\"javax.faces.component.UIComponentBase.attributesThatAreSet\");\n");
        generatedComponentClass.append("\t\t\tif (setAttributes == null) {\n");
        generatedComponentClass.append("\t\t\t\tString cname = this.getClass().getName();\n");
        generatedComponentClass.append("\t\t\t\tif (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {\n");
        generatedComponentClass.append("\t\t\t\t\tsetAttributes = new ArrayList<String>(6);\n");
        generatedComponentClass.append("\t\t\t\t\tthis.getAttributes().put(\"javax.faces.component.UIComponentBase.attributesThatAreSet\", setAttributes);\n");
        generatedComponentClass.append("\t\t\t}\n\t\t}\n");
        generatedComponentClass.append("\t\tif (setAttributes != null) {\n");
        generatedComponentClass.append("\t\t\tif (value == null) {\n");
        generatedComponentClass.append("\t\t\t\tValueExpression ve = getValueExpression(name);\n");
        generatedComponentClass.append("\t\t\t\tif (ve == null) {\n");
        generatedComponentClass.append("\t\t\t\t\tsetAttributes.remove(name);\n");
        generatedComponentClass.append("\t\t\t}\n");
        generatedComponentClass.append("\t\t\t\t\t} else if (!setAttributes.contains(name)) {\n");
        generatedComponentClass.append("\t\t\t\t\t\tsetAttributes.add(name);\n");
        generatedComponentClass.append("\t\t\t}\n");
        generatedComponentClass.append("\t\t}\n");
        generatedComponentClass.append("\t}\n");       
    }

    static void create() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        ComponentClassGenerator.startComponentClass(Generator.currentClass, component);
        ComponentClassGenerator.addProperties(Generator.fieldsForComponentClass);  
        ComponentClassGenerator.addFacet(Generator.currentClass, component);
        ComponentClassGenerator.addInternalFields();
        ComponentClassGenerator.handleAttribute();        
        ComponentClassGenerator.endComponentClass();
    }
}
