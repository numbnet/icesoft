package org.icefaces.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

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
        
        int classIndicator = component.component_class().lastIndexOf(".");
        generatedComponentClass.append("package ");
        generatedComponentClass.append(component.component_class().substring(0, classIndicator));
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
        generatedComponentClass.append(component.component_class().substring(classIndicator+1));
        generatedComponentClass.append(" extends ");
        generatedComponentClass.append(clazz.getSimpleName());
        generatedComponentClass.append("{\n");
        
    }
    
    static void endComponentClass() {
        addSaveRestoreState();
        generatedComponentClass.append("\n}");
        createJavaFile();

    }
    
    static void createJavaFile() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        String componentClass = component.component_class();
        String fileName = componentClass.substring(componentClass.lastIndexOf('.')+1) + ".java";
        System.out.println(">>>>>>>>>>> "+ fileName);
        String pack = componentClass.substring(0, componentClass.lastIndexOf('.'));
        System.out.println(">>>>>>>>>>> pack "+ pack);        
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        FileWriter.write(fileName, path, generatedComponentClass);        
    }

    static void addProperties(Map<String, Field> properties) {
        Iterator<Field> fields = properties.values().iterator();
        while(fields.hasNext()) {
            Field field = fields.next();
            if(field.isAnnotationPresent(Property.class)){
                Property prop = (Property)field.getAnnotation(Property.class);
                generatedComponentProperties.add(field);
                
                //create a private member if not defined already, it will usually
                //happen with includeProperties
                try {
                    System.out.println(field.getName() + " : "+ Generator.currentClass.getDeclaredField(field.getName()));
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    generatedComponentClass.append("\n\tprivate ");   
                    generatedComponentClass.append(field.getType().getName());
                    generatedComponentClass.append(" ");                    
                    generatedComponentClass.append(field.getName());
                    generatedComponentClass.append(";");                      
                }
                
                
                //set
                generatedComponentClass.append("\t/**\n"); 
                generatedComponentClass.append("\t* <p>Set the value of the <code>");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append("</code> property.</p>");                
                if (prop.javadocSet() != null && !"".equals(prop.javadocSet())) {
                    String[] lines = prop.javadocSet().split("\n");
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
                
                
                generatedComponentClass.append("\tpublic void set");
                generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
                generatedComponentClass.append(field.getName().substring(1));

                generatedComponentClass.append("(");
                generatedComponentClass.append(field.getType().getName());
                generatedComponentClass.append(" ");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append(") {\n");
                generatedComponentClass.append("\t\tthis."); 
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append(" = "); 
                generatedComponentClass.append(field.getName()); 
                generatedComponentClass.append(";\n\t}\n\n");
                
                
                
                //get
                generatedComponentClass.append("\t/**\n"); 
                generatedComponentClass.append("\t* <p>Return the value of the <code>");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append("</code> property.</p>");                
                if (prop.javadocGet() != null && !"".equals(prop.javadocGet())) {
                    String[] lines = prop.javadocGet().split("\n");
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
                generatedComponentClass.append("\t\tif (null != this.");
                generatedComponentClass.append(field.getName()); 
                generatedComponentClass.append(") {\n"); 
                generatedComponentClass.append("\t\t\treturn this.");
                generatedComponentClass.append(field.getName()); 
                generatedComponentClass.append(";\n\t\t}\n");
                generatedComponentClass.append("\t\tValueExpression _ve = getValueExpression(\"");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append("\");\n");    
                generatedComponentClass.append("\t\tif (_ve != null) {\n");                
                generatedComponentClass.append("\t\t\treturn (");
                generatedComponentClass.append(field.getType().getName());
                generatedComponentClass.append(") _ve.getValue(getFacesContext().getELContext());\n"); 
                generatedComponentClass.append("\t\t} else {\n\t\t\treturn null;\n\t\t}\n\t}\n\n");

              
            }
        }        
    }
    
    
    static void addProperties(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            Field field = fields[i];
            if(field.isAnnotationPresent(Property.class)){
                Property prop = (Property)field.getAnnotation(Property.class);
                generatedComponentProperties.add(field);
                //set
                if (prop.javadocSet() != null && !"".equals(prop.javadocSet())) {
                    generatedComponentClass.append("\t");    
                    generatedComponentClass.append(prop.javadocSet().replaceAll("\n", "\n\t"));
                    generatedComponentClass.append("\n");                    
                } else {
                    generatedComponentClass.append("\n\t/**\n\t * <p>Set the value of the <code>");  
                }

                generatedComponentClass.append(field.getName());
                generatedComponentClass.append("</code> property.</p>\n\t */\n");
                generatedComponentClass.append("\tpublic void set");
                generatedComponentClass.append(field.getName().substring(0,1).toUpperCase());
                generatedComponentClass.append(field.getName().substring(1));

                generatedComponentClass.append("(");
                generatedComponentClass.append(field.getType().getName());
                generatedComponentClass.append(" ");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append(") {\n");
                generatedComponentClass.append("\t\tthis."); 
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append(" = "); 
                generatedComponentClass.append(field.getName()); 
                generatedComponentClass.append(";\n\t}\n\n");
                
                
                
                //get
                generatedComponentClass.append("\t/**\n"); 
                generatedComponentClass.append("\t* <p>Return the value of the <code>");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append("</code> property.</p>");                
                if (prop.javadocGet() != null && !"".equals(prop.javadocGet())) {
                    String[] lines = prop.javadocGet().split("\n");
                    for (int j=0; j < lines.length; j++){
                        generatedComponentClass.append("\t* ");
                        generatedComponentClass.append(lines[j]);
                        generatedComponentClass.append("\n");   
                    }
                } 
                generatedComponentClass.append("\t*/\n");                

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
                generatedComponentClass.append("\t\tif (null != this.");
                generatedComponentClass.append(field.getName()); 
                generatedComponentClass.append(") {\n"); 
                generatedComponentClass.append("\t\t\treturn this.");
                generatedComponentClass.append(field.getName()); 
                generatedComponentClass.append(";\n\t\t}\n");
                generatedComponentClass.append("\t\tValueExpression _ve = getValueExpression(\"");
                generatedComponentClass.append(field.getName());
                generatedComponentClass.append("\");\n");    
                generatedComponentClass.append("\t\tif (_ve != null) {\n");                
                generatedComponentClass.append("\t\t\treturn (");
                generatedComponentClass.append(field.getType().getName());
                generatedComponentClass.append(") _ve.getValue(getFacesContext().getELContext());\n"); 
                generatedComponentClass.append("\t\t} else {\n\t\t\treturn null;\n\t\t}\n\t}\n\n");

              
            }
        }        
    }
    
    static void addSaveRestoreState() {
        generatedComponentClass.append("\tprivate Object[] _values;\n\n");

        //savestate
        generatedComponentClass.append("\tpublic Object saveState(FacesContext _context) {\n");
        generatedComponentClass.append("\t\tif (_values == null) {\n\t\t\t_values = new Object[");
        generatedComponentClass.append(generatedComponentProperties.size()+1);
        generatedComponentClass.append("];\n\t\t}\n");
        generatedComponentClass.append("\t\t_values[0] = super.saveState(_context);\n");        
        for (int i = 0; i < generatedComponentProperties.size(); i++){
            generatedComponentClass.append("\t\t_values[");
            generatedComponentClass.append((i+1));
            generatedComponentClass.append("] = ");     
            generatedComponentClass.append(generatedComponentProperties.get(i).getName()); 
            generatedComponentClass.append(";\n");            
        }
        generatedComponentClass.append("\t\treturn _values;\n\t}\n\n");
        
        //restoretate
        generatedComponentClass.append("\tpublic void restoreState(FacesContext _context, Object _state) {\n");
        generatedComponentClass.append("\t\t_values = (Object[]) _state;\n\t\tsuper.restoreState(_context, _values[0]);\n");
        for (int i = 0; i < generatedComponentProperties.size(); i++){
            generatedComponentClass.append("\t\tthis.");
            generatedComponentClass.append(generatedComponentProperties.get(i).getName()); 
            generatedComponentClass.append(" = (");    
            generatedComponentClass.append(generatedComponentProperties.get(i).getType().getName()); 
            generatedComponentClass.append(") _values[");
            generatedComponentClass.append((i+1));
            generatedComponentClass.append("];\n");
        }
        generatedComponentClass.append("\t}");        
    }
    
    static void create() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        ComponentClassGenerator.startComponentClass(Generator.currentClass, component);
        ComponentClassGenerator.addProperties(Generator.fieldsForComponentClass);  
        ComponentClassGenerator.endComponentClass();
    }
}
