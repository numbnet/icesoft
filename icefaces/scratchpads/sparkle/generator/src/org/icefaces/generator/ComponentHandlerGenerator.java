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

public class ComponentHandlerGenerator {
    static StringBuilder generatedComponentHandlerClass;
  
    static void startComponentClass(Class clazz, Component component) {
        //initialize
        generatedComponentHandlerClass = new StringBuilder();
        
        int classIndicator = Generator.getClassName(component).lastIndexOf(".");
        generatedComponentHandlerClass.append("package ");
        generatedComponentHandlerClass.append(Generator.getClassName(component).substring(0, classIndicator));
        generatedComponentHandlerClass.append(";\n\n");
        generatedComponentHandlerClass.append("import javax.faces.view.facelets.ComponentHandler;\n");
        generatedComponentHandlerClass.append("import javax.faces.view.facelets.ComponentConfig;\n");
        generatedComponentHandlerClass.append("import javax.faces.view.facelets.MetaRuleset;\n");
        generatedComponentHandlerClass.append("import com.sun.faces.facelets.tag.MethodRule;\n\n");
        generatedComponentHandlerClass.append("import java.util.EventObject;\n");
        generatedComponentHandlerClass.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
        
        
        generatedComponentHandlerClass.append("public class ");
        generatedComponentHandlerClass.append(clazz.getSimpleName());
        generatedComponentHandlerClass.append("Handler extends ComponentHandler");
        generatedComponentHandlerClass.append("{\n\n");
        
        generatedComponentHandlerClass.append("\n\tpublic ");
        generatedComponentHandlerClass.append(clazz.getSimpleName());   
        generatedComponentHandlerClass.append("Handler(ComponentConfig componentConfig) {\n");
        generatedComponentHandlerClass.append("\t\tsuper(componentConfig);\n");
        generatedComponentHandlerClass.append("\t}\n\n\n");
    }

    
    static void endComponentClass() {
        generatedComponentHandlerClass.append("\n}");
        createJavaFile();

    }
    
    static void createJavaFile() {
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        String componentClass = Generator.getClassName(component);
        String fileName = Generator.currentClass.getSimpleName() + "Handler.java";
        String pack = Generator.currentClass.getPackage().getName();
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        System.out.println("_________________________________________________________________________");
        System.out.println("File name "+ fileName);
        System.out.println("path  "+ path);        
        FileWriter.write("support", path, fileName, generatedComponentHandlerClass);        
    }

    static void addRules(List<Field> fields) {
        generatedComponentHandlerClass.append("\tprotected MetaRuleset createMetaRuleset(Class type) {\n");  
        generatedComponentHandlerClass.append("\t\tMetaRuleset metaRuleset = super.createMetaRuleset(type);\n");  
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            Property prop = (Property)field.getAnnotation(Property.class);
            if (prop.isMethodExpression()) {
                generatedComponentHandlerClass.append("\t\tmetaRuleset.addRule( new MethodRule(\"");
                generatedComponentHandlerClass.append(field.getName());
                generatedComponentHandlerClass.append("\", null, new Class[");
                if (prop.methodExpressionArgument().length() > 0) {
                    generatedComponentHandlerClass.append("] {");
                    generatedComponentHandlerClass.append(prop.methodExpressionArgument());
                    generatedComponentHandlerClass.append(".class}");   
                } else {
                    generatedComponentHandlerClass.append("0]");  
                }
                generatedComponentHandlerClass.append(") );\n");
            }

        }
        generatedComponentHandlerClass.append("\t\n\t\treturn metaRuleset;\n");
        generatedComponentHandlerClass.append("\t}");
    }
       

    
 
    static void create() {
        if (!Generator.currentClassHasMethodExpression) return;
        Component component = (Component) Generator.currentClass.getAnnotation(Component.class);
        startComponentClass(Generator.currentClass, component);
        addRules(ComponentClassGenerator.generatedComponentProperties);  
        endComponentClass();
    }
}
