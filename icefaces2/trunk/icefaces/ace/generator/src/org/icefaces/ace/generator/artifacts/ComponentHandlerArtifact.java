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

package org.icefaces.ace.generator.artifacts;

import java.lang.reflect.Field;
import java.util.List;

import org.icefaces.ace.generator.context.ComponentContext;
import org.icefaces.ace.generator.utils.FileWriter;
import org.icefaces.ace.generator.utils.Utility;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;

public class ComponentHandlerArtifact extends Artifact{
    private StringBuilder generatedComponentHandlerClass;

	public ComponentHandlerArtifact(ComponentContext componentContext) {
		super(componentContext);
	}

	@Override
	public void build() {
        if (!getComponentContext().isHasMethodExpression()) return;
        Component component = (Component) getComponentContext().getActiveClass().getAnnotation(Component.class);
        startComponentClass(getComponentContext().getActiveClass(), component);
        addRules(getComponentContext().getPropertyFieldsForComponentClassAsList());
        endComponentClass();
		
	}
    
    private void startComponentClass(Class clazz, Component component) {
        //initialize
        generatedComponentHandlerClass = new StringBuilder();
        
        int classIndicator = Utility.getClassName(component).lastIndexOf(".");
        generatedComponentHandlerClass.append("package ");
        generatedComponentHandlerClass.append(Utility.getClassName(component).substring(0, classIndicator));
        generatedComponentHandlerClass.append(";\n\n");
        generatedComponentHandlerClass.append("import javax.faces.view.facelets.ComponentHandler;\n");
        generatedComponentHandlerClass.append("import javax.faces.view.facelets.ComponentConfig;\n");
        generatedComponentHandlerClass.append("import javax.faces.view.facelets.MetaRuleset;\n");

        //The MethodRule class is specific to the JSF implementation in use so we check to see
        //which version, Mojarra or MyFaces is available, to determine which we should be importing.
        try {
            Class.forName("com.sun.faces.facelets.tag.MethodRule");
            generatedComponentHandlerClass.append("import com.sun.faces.facelets.tag.MethodRule;\n\n");
            System.out.println("Mojarra version of MethodRule found");
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("org.apache.myfaces.view.facelets.tag.MethodRule");
                generatedComponentHandlerClass.append("import org.apache.myfaces.view.facelets.tag.MethodRule;\n\n");
                System.out.println("MyFaces version of MethodRule found");
            } catch (ClassNotFoundException e2) {
                System.out.println("cannot find a valid (Mojarra or MyFaces) MethodRule class " + e2);
            }
        }

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

    
    private void endComponentClass() {
        generatedComponentHandlerClass.append("\n}");
        createJavaFile();

    }

    private void createJavaFile() {
        Component component = (Component) getComponentContext().getActiveClass().getAnnotation(Component.class);
        String componentClass = Utility.getClassName(component);
        String fileName = getComponentContext().getActiveClass().getSimpleName() + "Handler.java";
        String pack = getComponentContext().getActiveClass().getPackage().getName();
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        System.out.println("_________________________________________________________________________");
        System.out.println("File name "+ fileName);
        System.out.println("path  "+ path);        
        FileWriter.write("support", path, fileName, generatedComponentHandlerClass);        
    }

    private void addRules(List<Field> fields) {
        generatedComponentHandlerClass.append("\tprotected MetaRuleset createMetaRuleset(Class type) {\n");  
        generatedComponentHandlerClass.append("\t\tMetaRuleset metaRuleset = super.createMetaRuleset(type);\n");  
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
			PropertyValues prop = getComponentContext().getPropertyValuesMap().get(field);
			
            if (prop.expression == Expression.METHOD_EXPRESSION) {
                generatedComponentHandlerClass.append("\t\tmetaRuleset.addRule( new MethodRule(\"");
                generatedComponentHandlerClass.append(field.getName());
                generatedComponentHandlerClass.append("\", null, new Class[");
                if (prop.methodExpressionArgument.length() > 0) {
                    generatedComponentHandlerClass.append("] {");
                    generatedComponentHandlerClass.append(prop.methodExpressionArgument);
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
}
