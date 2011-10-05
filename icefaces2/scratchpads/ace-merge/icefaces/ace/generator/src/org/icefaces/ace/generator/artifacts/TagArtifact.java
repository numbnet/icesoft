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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.icefaces.ace.generator.context.ComponentContext;
import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.utils.FileWriter;
import org.icefaces.ace.generator.utils.Utility;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;

public class TagArtifact extends Artifact{
	private StringBuilder generatedTagClass;
	private Map<String, Field> generatedTagProperties;

	public TagArtifact(ComponentContext componentContext) {
		super(componentContext);
	}

	private void startComponentClass(Component component) {
		generatedTagClass = new StringBuilder();
		generatedTagProperties = new HashMap<String, Field>();
		int classIndicator = Utility.getTagClassName(component).lastIndexOf(".");

		generatedTagClass.append("package ");
		generatedTagClass.append(Utility.getTagClassName(component).substring(0, classIndicator));
		generatedTagClass.append(";\n\n");

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
		generatedTagClass.append(Utility.getTagClassName(component).substring(classIndicator+1));
		generatedTagClass.append("Tag extends UIComponentELTag {\n");
		generatedTagClass.append("\tpublic String getRendererType() {\n\t\treturn ");
		String rendererType = null;
		if (!"".equals(component.rendererType())) {
			rendererType = "\""+ component.rendererType() + "\"";
		}

		generatedTagClass.append(rendererType);   
		generatedTagClass.append(";\n\t}\n");
		generatedTagClass.append("\tpublic String getComponentType() {\n\t\treturn \"");
		generatedTagClass.append(Utility.getComponentType(component)); 
		generatedTagClass.append("\";\n\t}\n");
	}

	private void endComponentClass() {
		addDoTags("Start");
		addDoTags("End");
		addRelease();
		generatedTagClass.append("\n}");
		createJavaFile();

	}

	private void createJavaFile() {
		Component component = (Component) getComponentContext().getActiveClass().getAnnotation(Component.class);
		String componentClass = Utility.getTagClassName(component);
		String fileName = componentClass.substring(componentClass.lastIndexOf('.')+1) + "Tag.java";
		String pack = componentClass.substring(0, componentClass.lastIndexOf('.'));
		String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
		// comment out this so UICommand classes will compile **TEMPORARY
		FileWriter.write("support", path, fileName, generatedTagClass);        
	}

	private void addProperties(Class clazz, Component component) {
		GeneratorContext.getInstance().getTldBuilder().addTagInfo(clazz, component);
		addSetters();
		addSetProperties(Utility.getClassName(component));
	}

	private void updateFields(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			boolean addToTagClass = false;
			addToTagClass = field.isAnnotationPresent(Property.class);

			if(addToTagClass ){
				if (!generatedTagProperties.containsKey(field.getName()))
					generatedTagProperties.put(field.getName(), field);
			}
		}  
	}

	private void addSetters() {
		//set
		Iterator<String> iterator = getComponentContext().getFieldNamesForTagClass();
		while (iterator.hasNext()){
			Field field = getComponentContext().getFieldsForTagClass().get(iterator.next());
			PropertyValues prop = getComponentContext().getPropertyValuesMap().get(field);
			
			GeneratorContext.getInstance().getTldBuilder().addAttributeInfo(field);

			String type = (prop.expression == Expression.METHOD_EXPRESSION) ?"javax.el.MethodExpression " :"javax.el.ValueExpression ";

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

	private void addRelease() {
		generatedTagClass.append("\t/**\n\t * <p>Release any allocated tag handler attributes.</p>\n \t */\n");
		generatedTagClass.append("\tpublic void release() {\n");
		generatedTagClass.append("\t\tsuper.release();\n");        

		Iterator<String> iterator = getComponentContext().getFieldsForTagClass().keySet().iterator();
		while (iterator.hasNext()){
			Field field = getComponentContext().getFieldsForTagClass().get(iterator.next());
			generatedTagClass.append("\t\t"); 
			generatedTagClass.append(field.getName()); 
			generatedTagClass.append(" = null;\n"); 
		}
		generatedTagClass.append("\t}");        
	}

	private void addDoTags(String tagName) {
		generatedTagClass.append("\n\tpublic int do");
		generatedTagClass.append(tagName);
		generatedTagClass.append("Tag() throws JspException {\n");
		generatedTagClass.append("\t\ttry {\n\t\t\treturn super.do");
		generatedTagClass.append(tagName);
		generatedTagClass.append("Tag();\n");
		generatedTagClass.append("\t\t} catch (Exception e) {\n\t\t\tThrowable root = e;\t\t\t\n\t\t\twhile (root.getCause() != null) {\n");
		generatedTagClass.append("\t\t\t\troot = root.getCause();\n\t\t\t}\n\t\t\tthrow new JspException(root);\n\t\t}\n\t}\n") ; 
	}

	private void addSetProperties(String componentClass) {
		generatedTagClass.append("\n\tprotected void setProperties(UIComponent component) {\n\t\tsuper.setProperties(component);\n\t\t");
		generatedTagClass.append(componentClass);
		generatedTagClass.append(" _component = null;\n\t\ttry {\n\t\t\t_component = (");
		generatedTagClass.append(componentClass);
		generatedTagClass.append(") component;\n\t\t} catch (ClassCastException cce) {");
		generatedTagClass.append("\n\t\t\tthrow new IllegalStateException(\"Component \" + component.toString() + \" not expected type.  Expected:"); 
		generatedTagClass.append(componentClass);
		generatedTagClass.append("\");\n");
		generatedTagClass.append("\t\t}\n");        
		Iterator<String> iterator = getComponentContext().getFieldNamesForTagClass();
		while (iterator.hasNext()){
			Field field = getComponentContext().getFieldsForTagClass().get(iterator.next());
			generatedTagClass.append("\t\tif ("); 
			generatedTagClass.append(field.getName()); 
			generatedTagClass.append(" != null) {\n\t\t\t");
			PropertyValues property = getComponentContext().getPropertyValuesMap().get(field);
			if (property.expression == Expression.METHOD_EXPRESSION &&
                "actionListener".equals(field.getName())) {
				generatedTagClass.append("_component.addActionListener(new MethodExpressionActionListener(actionListener)");
			} else if (property.expression == Expression.METHOD_EXPRESSION &&
                "action".equals(field.getName())) {
				generatedTagClass.append("_component.setActionExpression(action");
            } else if (property.expression == Expression.METHOD_EXPRESSION &&
                "valueChangeListener".equals(field.getName()) &&
                // Any UIInput inherits valueChangeListener, so should use
                // addValueChangeListener, but any component not inheriting it,
                // and just implementing it's own MethodExpression property
                // named valueChangeListener should just use setValueChangeListener
                !getComponentContext().getFieldsForComponentClass().containsKey("valueChangeListener")) {
				generatedTagClass.append("_component.addValueChangeListener(new MethodExpressionValueChangeListener(valueChangeListener)");
            } else if (property.expression == Expression.METHOD_EXPRESSION &&
                "validator".equals(field.getName())) {
				generatedTagClass.append("_component.addValidator(new MethodExpressionValidator(valueChangeListener)");
			} else {
				generatedTagClass.append("_component.set");

				if (property.expression == Expression.METHOD_EXPRESSION) {
					generatedTagClass.append(field.getName().substring(0,1).toUpperCase());
					generatedTagClass.append(field.getName().substring(1));  
				} else {
					generatedTagClass.append("ValueExpression");            
				}
				generatedTagClass.append("(");
				if (property.expression == Expression.VALUE_EXPRESSION) {
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

	public void build() {
		Component component = (Component) getComponentContext().getActiveClass().getAnnotation(Component.class);
		startComponentClass(component);
		addProperties(getComponentContext().getActiveClass(), component);  
		endComponentClass();        
	}
}
