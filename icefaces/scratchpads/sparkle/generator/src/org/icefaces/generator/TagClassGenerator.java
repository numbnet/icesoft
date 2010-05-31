package org.icefaces.generator;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

public class TagClassGenerator {
    static StringBuilder generatedTagClass;
    static Map<String, Field> generatedTagProperties;


    static void startComponentClass(Component component) {
        generatedTagClass = new StringBuilder();
        generatedTagProperties = new HashMap<String, Field>();
        int classIndicator = Generator.getTagClassName(component).lastIndexOf(".");
        
        generatedTagClass.append("package ");
        generatedTagClass.append(Generator.getTagClassName(component).substring(0, classIndicator));
        generatedTagClass.append(";\n\n");
        
        generatedTagClass.append("import com.sun.faces.util.Util;\n");
        generatedTagClass.append("import java.io.IOException;\n");
        generatedTagClass.append("import javax.el.*;\n");
        
        generatedTagClass.append("import javax.faces.*;\n");
        generatedTagClass.append("import javax.faces.component.*;\n");
        generatedTagClass.append("import javax.faces.context.*;\n");
        generatedTagClass.append("import javax.faces.convert.*;\n");
        
        generatedTagClass.append("import javax.faces.el.*;\n");
        generatedTagClass.append("import javax.faces.event.*;\n");
        generatedTagClass.append("import javax.faces.validator.*;\n");
        generatedTagClass.append("import javax.faces.webapp.*;\n");
        generatedTagClass.append("import javax.servlet.jsp.JspException;\n\n");  
        generatedTagClass.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");        
        generatedTagClass.append("public class ");
        generatedTagClass.append(Generator.getTagClassName(component).substring(classIndicator+1));
        generatedTagClass.append("Tag extends UIComponentELTag {\n");
        generatedTagClass.append("\tpublic String getRendererType() {\n\t\treturn ");
        String rendererType = null;
        if (!"".equals(component.rendererType())) {
            rendererType = "\""+ component.rendererType() + "\"";
        }
        
        generatedTagClass.append(rendererType);   
        generatedTagClass.append(";\n\t}\n");
        generatedTagClass.append("\tpublic String getComponentType() {\n\t\treturn \"");
        generatedTagClass.append(Generator.getComponentType(component)); 
        generatedTagClass.append("\";\n\t}\n");
    }
    
    static void endComponentClass() {
        addDoTags("Start");
        addDoTags("End");
        addRelease();
        generatedTagClass.append("\n}");
        createJavaFile();

    }
    
    static void createJavaFile() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        String componentClass = Generator.getTagClassName(component);
        String fileName = componentClass.substring(componentClass.lastIndexOf('.')+1) + "Tag.java";
        String pack = componentClass.substring(0, componentClass.lastIndexOf('.'));
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
  // comment out this so UICommand classes will compile **TEMPORARY
        FileWriter.write("support", path, fileName, generatedTagClass);        
    }
    
    static void addProperties(Class clazz, Component component) {
        Generator.tldBuilder.addTagInfo(clazz, component);
        addSetters();
        addSetProperties(Generator.getClassName(component));
    }
    
    static void updateFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            Field field = fields[i];
            boolean addToTagClass = false;
            addToTagClass = field.isAnnotationPresent(Property.class);
            if(!addToTagClass) {
                if (Generator.propertyTemplate.containsKey(clazz.getSimpleName())) {
                    if (((List)Generator.propertyTemplate.get(clazz.getSimpleName())).contains(field.getName())) {
                        addToTagClass = true;
                    }
                }
            }
            
            if(addToTagClass ){
                if (!generatedTagProperties.containsKey(field.getName()))
                    generatedTagProperties.put(field.getName(), field);
            }
        }  
    }
    
    static void addSetters() {
        //set
        Iterator<String> iterator = Generator.fieldsForTagClass.keySet().iterator();
        while (iterator.hasNext()){
            Field field = Generator.fieldsForTagClass.get(iterator.next());
            Property property = field.getAnnotation(Property.class);
            //must be inherited property from non-icefaces class
            if (property == null || property.useTemplate()) {
            	
                Field o = (Field)Generator.propertyTemplate.get(field.getName());
                if (null==o){
                	System.out.println("Template field not found: "+field.getName());
                }
                property = (Property) o.getAnnotation(Property.class);
            }
            Generator.tldBuilder.addAttributeInfo(field, property);
            
            String type = (property.isMethodExpression()) ?"javax.el.MethodExpression " :"javax.el.ValueExpression ";

            generatedTagClass.append("\tprivate ");
            generatedTagClass.append(type);
            generatedTagClass.append(field.getName());
            generatedTagClass.append(";\n\tpublic void set");
            generatedTagClass.append(field.getName().substring(0,1).toUpperCase());
            generatedTagClass.append(field.getName().substring(1));  
            generatedTagClass.append("(");
            generatedTagClass.append(type);
            generatedTagClass.append(field.getName());
            generatedTagClass.append(") {\n");
            generatedTagClass.append("\t\tthis."); 
            generatedTagClass.append(field.getName());
            generatedTagClass.append(" = "); 
            generatedTagClass.append(field.getName()); 
            generatedTagClass.append(";\n\t}\n");
        }
        
        
    }
    
    static void addRelease() {
        generatedTagClass.append("\t/**\n\t * <p>Release any allocated tag handler attributes.</p>\n \t */\n");
        generatedTagClass.append("\tpublic void release() {\n");
        generatedTagClass.append("\t\tsuper.release();\n");        
        
        Iterator<String> iterator = Generator.fieldsForTagClass.keySet().iterator();
        while (iterator.hasNext()){
            Field field = Generator.fieldsForTagClass.get(iterator.next());
            generatedTagClass.append("\t\t"); 
            generatedTagClass.append(field.getName()); 
            generatedTagClass.append(" = null;\n"); 
        }
        generatedTagClass.append("\t}");        
    }
    
    static void addDoTags(String tagName) {
        generatedTagClass.append("\n\tpublic int do");
        generatedTagClass.append(tagName);
        generatedTagClass.append("Tag() throws JspException {\n");
        generatedTagClass.append("\t\ttry {\n\t\t\treturn super.do");
        generatedTagClass.append(tagName);
        generatedTagClass.append("Tag();\n");
        generatedTagClass.append("\t\t} catch (Exception e) {\n\t\t\tThrowable root = e;\t\t\t\n\t\t\twhile (root.getCause() != null) {\n");
        generatedTagClass.append("\t\t\t\troot = root.getCause();\n\t\t\t}\n\t\t\tthrow new JspException(root);\n\t\t}\n\t}\n") ; 
    }
    
    static void addSetProperties(String componentClass) {
        generatedTagClass.append("\n\tprotected void setProperties(UIComponent component) {\n\t\tsuper.setProperties(component);\n\t\t");
        generatedTagClass.append(componentClass);
        generatedTagClass.append(" _component = null;\n\t\ttry {\n\t\t\t_component = (");
        generatedTagClass.append(componentClass);
        generatedTagClass.append(") component;\n\t\t} catch (ClassCastException cce) {");
        generatedTagClass.append("\n\t\t\tthrow new IllegalStateException(\"Component \" + component.toString() + \" not expected type.  Expected:"); 
        generatedTagClass.append(componentClass);
        generatedTagClass.append("\");\n");
        generatedTagClass.append("\t\t}\n");        
        Iterator<String> iterator = Generator.fieldsForTagClass.keySet().iterator();
        while (iterator.hasNext()){
            Field field = Generator.fieldsForTagClass.get(iterator.next());
            generatedTagClass.append("\t\tif ("); 
            generatedTagClass.append(field.getName()); 
            generatedTagClass.append(" != null) {\n\t\t\t");
            Property property = (Property) field.getAnnotation(Property.class);            
            if (property.isMethodExpression() && "actionListener".equals(field.getName())) {
            	generatedTagClass.append("_component.addActionListener(new MethodExpressionActionListener(actionListener)");
            } else if (property.isMethodExpression() && "action".equals(field.getName())) {
            	generatedTagClass.append("_component.setActionExpression(action");
            } else {
	            generatedTagClass.append("_component.set");

	            if (property.isMethodExpression()) {
	                generatedTagClass.append(field.getName().substring(0,1).toUpperCase());
	                generatedTagClass.append(field.getName().substring(1));  
	            } else {
	                generatedTagClass.append("ValueExpression");            
	            }
	            generatedTagClass.append("(");
	            if (!property.isMethodExpression()) {
	                generatedTagClass.append("\"");
	                generatedTagClass.append(field.getName());
	                generatedTagClass.append("\", ");
	            }
	            generatedTagClass.append(field.getName());  
            }
            generatedTagClass.append(");\n");    
            generatedTagClass.append("\t\t}\n");              
        }
        generatedTagClass.append("\t}\n");        
    }
    
    static void create() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        TagClassGenerator.startComponentClass(component);
        TagClassGenerator.addProperties(Generator.currentClass, component);  
        TagClassGenerator.endComponentClass();        
    }
}
