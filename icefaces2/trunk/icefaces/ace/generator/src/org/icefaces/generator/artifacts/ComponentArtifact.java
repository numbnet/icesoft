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

package org.icefaces.generator.artifacts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Facet;
import org.icefaces.generator.behavior.Behavior;
import org.icefaces.generator.context.ComponentContext;
import org.icefaces.generator.context.GeneratorContext;
import org.icefaces.generator.utils.FileWriter;
import org.icefaces.generator.utils.Utility;
import java.util.logging.Logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.generator.utils.PropertyValues;

public class ComponentArtifact extends Artifact{

    private StringBuilder writer = new StringBuilder();

	private List<Field> generatedComponentProperties = new ArrayList<Field>();
    private final static Logger Log = Logger.getLogger(ComponentArtifact.class.getName());

    public ComponentArtifact(ComponentContext componentContext) {
		super(componentContext);
	}

	public List<Field> getGeneratedComponentProperties() {
		return generatedComponentProperties;
	}

	private void startComponentClass(Class clazz, Component component) {
		//initialize
		// add entry to faces-config
		GeneratorContext.getInstance().getFacesConfigBuilder().addEntry(clazz, component);
		GeneratorContext.getInstance().getFaceletTagLibBuilder().addTagInfo(clazz, component);

		int classIndicator =  Utility.getClassName(component).lastIndexOf(".");

		writer.append("package ");
		writer.append(Utility.getClassName(component).substring(0, classIndicator));
		writer.append(";\n\n");
		writer.append("import java.io.IOException;\n");
		writer.append("import java.util.List;\n");
		writer.append("import java.util.ArrayList;\n");
		writer.append("import java.util.Map;\n");
		writer.append("import java.util.HashMap;\n");
		writer.append("import java.util.Arrays;\n\n");
		writer.append("import javax.faces.context.FacesContext;\n");
		writer.append("import javax.el.MethodExpression;\n");
		writer.append("import javax.el.ValueExpression;\n\n");
		writer.append("import javax.faces.event.PhaseId;\n");
		writer.append("import javax.faces.component.StateHelper;\n\n");
//        writer.append("import org.icefaces.component.utils.PartialStateHolderImpl;\n\n");

		writer.append("import javax.faces.application.ResourceDependencies;\n");
		writer.append("import javax.faces.application.ResourceDependency;\n\n");


		for (Behavior behavior: getComponentContext().getBehaviors()) {
			behavior.addImportsToComponent(writer);
		}
		writer.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
		
		// copy @ResourceDependency annotations
		if (clazz.isAnnotationPresent(ResourceDependencies.class)) {
			writer.append("\n");
			writer.append("@ResourceDependencies({\n");
			
			ResourceDependencies rd = (ResourceDependencies) clazz.getAnnotation(ResourceDependencies.class);
			ResourceDependency[] rds = rd.value();
			int rdsLength = rds.length;
			for (int i = 0; i < rdsLength; i++) {
				writer.append("\t@ResourceDependency(name=\"" + rds[i].name() + "\",library=\"" + rds[i].library() + "\",target=\"" + rds[i].target() + "\")");
				if (i < (rdsLength-1)) {
					writer.append(",");
				}
				writer.append("\n");
			}
			
			writer.append("})");
			writer.append("\n\n");
		} else if (clazz.isAnnotationPresent(ResourceDependency.class)) {
			ResourceDependency rd = (ResourceDependency) clazz.getAnnotation(ResourceDependency.class);
			writer.append("@ResourceDependency(name=\"" + rd.name() + "\",library=\"" + rd.library() + "\",target=\"" + rd.target() + "\")\n\n");
		}

		writer.append("public class ");
		writer.append(Utility.getClassName(component).substring(classIndicator+1));
		writer.append(" extends ");
		writer.append(component.extendsClass());
		String interfaceNames = "";
		for (Behavior behavior: getComponentContext().getBehaviors()) {
			interfaceNames+=behavior.getInterfaceName()+",";
		}
		if (!"".equals(interfaceNames)) {
			writer.append(" implements ");
			writer.append(interfaceNames.subSequence(0, interfaceNames.length()-1));
		}
		
		writer.append("{\n");

		writer.append("\n\tpublic static final String COMPONENT_TYPE = \""+ component.componentType() + "\";");
		String rendererType = null;
		if (!"null".equals(component.rendererType())) {
			rendererType = "\""+ component.rendererType() + "\"";
		}

		writer.append("\n\tpublic static final String RENDERER_TYPE = "+ rendererType + ";\n");

		writer.append("\n\tpublic String getFamily() {\n\t\treturn \"");
		writer.append(Utility.getFamily(component));
		writer.append("\";\n\t}\n\n");
	}


	private void endComponentClass() {
		for (Behavior behavior: getComponentContext().getBehaviors()) {
			behavior.addCodeToComponent(writer);
		}		
		writer.append("\n}");
		createJavaFile();

	}

	private void createJavaFile() {
		System.out.println("____________________________Creating component class_________________________");
		Component component = (Component) getComponentContext().getActiveClass().getAnnotation(Component.class);
		String componentClass =Utility.getClassName(component);
		String fileName = componentClass.substring(componentClass.lastIndexOf('.')+1) + ".java";
		System.out.println("____FileName "+ fileName);
		String pack = componentClass.substring(0, componentClass.lastIndexOf('.'));
		System.out.println("____package "+ pack);        
		String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
		System.out.println("____path "+ path);         
		FileWriter.write("base", path, fileName, writer);
		System.out.println("____________________________Creating component class ends_________________________");
	}
    
    
	private void addProperties(List<Field> properties) {
        generatedComponentProperties.addAll(properties);
		addPropertyEnum();
		addGetterSetter();
	}

	private void addPropertyEnum() {

		writer.append("\n\tprotected enum PropertyKeys {\n");
		for (Behavior behavior: getComponentContext().getBehaviors()) {
			behavior.addPropertiesEnumToComponent(writer);
		}
        String propertyName; 
		Map<Field, PropertyValues> propertyValuesMap = getComponentContext().getPropertyValuesMap();
		for (int i = 0; i < generatedComponentProperties.size(); i++){
			PropertyValues propertyValues = propertyValuesMap.get(generatedComponentProperties.get(i));

            if (!"null".equalsIgnoreCase( propertyName = propertyValues.name))  {
                propertyName += "Val(\"" + propertyName + "\")";
            } else {
                propertyName = generatedComponentProperties.get(i).getName();
            } 
            System.out.println("Processing property " + propertyName );

			if (!propertyValues.isDelegatingProperty) {
				writer.append("\t\t");
				writer.append( propertyName );
				writer.append(",\n");
			}
		}
		Iterator<Field> fields = getComponentContext().getInternalFieldsForComponentClass().values().iterator();
		while (fields.hasNext()) {
			Field field = fields.next();
			writer.append("\t\t");
			writer.append( field.getName() );
			writer.append(",\n");
		}
		writer.append("\t\t;\n");
		writer.append("\t\tString toString;\n");
		writer.append("\t\tPropertyKeys(String toString) { this.toString = toString; }\n");
		writer.append("\t\tPropertyKeys() { }\n");
		writer.append("\t\tpublic String toString() {\n");
		writer.append("\t\t\treturn ((toString != null) ? toString : super.toString());\n");
		writer.append("\t\t}\n\t}\n");
	}

	private void addGetterSetter() {
		for (Behavior behavior: getComponentContext().getBehaviors()) {
			behavior.addGetterSetter(this, writer);
		}
		for (int i = 0; i < generatedComponentProperties.size(); i++) {
			Field field = generatedComponentProperties.get(i);
			addGetterSetter(field);			
		}
		//since generatedComponentProperties doesn't include inherited properties,
		//here FieldsForTagClass is used. This part may need re-factor-ed later.
		Iterator<String> iterator = getComponentContext().getFieldsForTagClass().keySet().iterator();
		while (iterator.hasNext()){
			Field field = getComponentContext().getFieldsForTagClass().get(iterator.next());
			GeneratorContext.getInstance().getFaceletTagLibBuilder().addAttributeInfo(field);
		}

	}

    /**
     * Add a getter/setter for the property for a given field. This method writes the
     * getters to return a Wrapper class for most primitive types, the exceptions being
     * if the method name has a signature from EditableValueHolder that can't be changed. 
     * @param field
     */
	public void addGetterSetter(Field field) {
		PropertyValues prop = getComponentContext().getPropertyValuesMap().get(field);

		boolean isBoolean = field.getType().getName().endsWith("boolean")||
		field.getType().getName().endsWith("Boolean");

        
        // primitive properties are supported (ones with values
        // even if no default is specified in the Meta). Wrapper properties
        // (null default and settable values) are also supported
        // There are four property names which must be forced to be primitive                                L
        // or else the generated signiture clashes with the property names in
        // EditableValueHolder
        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey( field.getName().toString().trim() );

          String returnAndArgumentType = field.getType().getName();

        // If primitive property, get the primitive return type
        // otherwise leave it as is.
        if (isPrimitive) {
            if (GeneratorContext.WrapperTypes.containsKey( field.getType().getName() )) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get( field.getType().getName() );
            }
        }

		String methodName;
        // The publicly exposed property name. Will differ from the field name
        // if the field name is a java keyword
		String pseudoFieldName;
        String camlCaseMethodName;
        if (!"null".equals(prop.name) ) {
            methodName = prop.name;
            pseudoFieldName = methodName + "Val";
        } else {
            methodName = field.getName();
            pseudoFieldName = methodName;
        }
        camlCaseMethodName = methodName.substring(0,1).toUpperCase() + methodName.substring(1);

		//-------------------------------------
		// writing Setter
        //-------------------------------------

		addJavaDoc(pseudoFieldName, true, prop.javadocSet);
		writer.append("\tpublic void set");
		writer.append(camlCaseMethodName);

        // Allow java autoconversion to deal with most of the conversion between
        // primitive types and Wrapper classes
		writer.append("(");
        writer.append( returnAndArgumentType );
		writer.append(" ");
		writer.append(field.getName());
		writer.append(") {");

		if (!prop.isDelegatingProperty) {
			writer.append("\n\t\tValueExpression ve = getValueExpression(PropertyKeys.");
			writer.append(pseudoFieldName);
			writer.append(".name() );");
//			writer.append("\n\t\tMap clientValues = null;");
			writer.append("\n\t\tif (ve != null) {");
			writer.append("\n\t\t\t// map of style values per clientId");
			writer.append("\n\t\t\tve.setValue(getFacesContext().getELContext(), ");
			writer.append( field.getName() );
			writer.append(" );");

			writer.append("\n\t\t} else { ");

//		    writer.append("\n\t\t\tMap clientValues = (Map) sh.get(valuesKey); ");
//
//			writer.append("\n\t\t\tif (clientValues == null) {" );
//            writer.append("\n\t\t\t\tclientValues = new HashMap(); ");
////            writer.append("\n\t\t\t\tmarkInitialState();");
//            writer.append("\n\t\t\t\tsh.put(PropertyKeys.");
//			writer.append(field.getName());
//			writer.append(", clientValues ); ");
//			writer.append("\n\t\t\t}");
			writer.append("\n\t\t\tPhaseId pi = getFacesContext().getCurrentPhaseId();");
            writer.append("\n\t\t\tStateHelper sh = getStateHelper(); ");
            
			writer.append("\n\t\t\tif (pi.equals(PhaseId.RENDER_RESPONSE) || pi.equals(PhaseId.RESTORE_VIEW))  {");
            // Here,

            writer.append("\n\t\t\t\tString defaultKey = PropertyKeys.").append( pseudoFieldName ).
                                append(".name() + \"_defaultValues\";" );
            writer.append("\n\t\t\t\tMap clientDefaults = (Map) sh.get(defaultKey);");

            writer.append("\n\t\t\t\tif (clientDefaults == null) { ");
            writer.append("\n\t\t\t\t\tclientDefaults = new HashMap(); ");
            writer.append("\n\t\t\t\t\tclientDefaults.put(\"defValue\"," ).append( field.getName().toString() ).append(");");
            writer.append("\n\t\t\t\t\tsh.put(defaultKey, clientDefaults); "); 
            writer.append("\n\t\t\t\t} "); 


			writer.append("\n\t\t\t} else {");
			writer.append("\n\t\t\t\tString clientId = getClientId();");
            writer.append("\n\t\t\t\tString valuesKey = PropertyKeys.").append( pseudoFieldName ).
                    append(".name() + \"_rowValues\"; ");
            writer.append("\n\t\t\t\tMap clientValues = (Map) sh.get(valuesKey); ");
            writer.append("\n\t\t\t\tif (clientValues == null) {");
            writer.append("\n\t\t\t\t\tclientValues = new HashMap(); ");
            writer.append("\n\t\t\t\t}");
            writer.append("\n\t\t\t\tclientValues.put(clientId, " ).append( field.getName().toString()).
                    append(");");

            writer.append("\n\t\t\t\t//Always re-add the delta values to the map. JSF merges the values into the main map" );
            writer.append("\n\t\t\t\t//and values are not state saved unless they're in the delta map. " );

            writer.append("\n\t\t\t\tsh.put(valuesKey, clientValues);" );


			writer.append("\n\t\t\t}" );
			writer.append("\n\t\t}" );
		} else {
			writer.append("\n\t\tsuper.set" + methodName + "(" + field.getName() + ");");
		}
        writer.append("\n\t}\n" );

        //-----------------
		//getter
        //-----------------

		addJavaDoc(pseudoFieldName, false, prop.javadocGet);

        // Internal value representation is always Wrapper type
        String internalType = returnAndArgumentType;
        if (GeneratorContext.InvWrapperTypes.containsKey( returnAndArgumentType ) ) {
            internalType = GeneratorContext.InvWrapperTypes.get( returnAndArgumentType );
        }

		writer.append("\tpublic ");
        writer.append( returnAndArgumentType );
		writer.append(" ");

		if (isBoolean) {
			writer.append("is");
		} else {
			writer.append("get");
		}
		writer.append(camlCaseMethodName);
		writer.append("() {");

        if (!prop.isDelegatingProperty) {
			// start of the code
			writer.append("\n\t\t").append(internalType).append(" retVal = ");
																																  
			// No defined default value is returned as the string "null". This has to
			// be handled for various cases. primitives must have a default of some kind
			// and Strings have to return null (not "null") to work.
			String defaultValue = prop.defaultValue;
			Log.fine("Evaluating field name: " + field.getName().toString().trim() + ", isPRIMITIVE " +
							   isPrimitive + ", defaultValue:[" + defaultValue + "], isNull:" + (defaultValue == null));

			if (isPrimitive && (defaultValue == null || defaultValue.equals("") || defaultValue.equals("null"))) {
				defaultValue = GeneratorContext.PrimitiveDefaults.get( field.getType().toString().trim() );
			}

            boolean needsQuotes = ((internalType.indexOf("String") > -1) || (internalType.indexOf("Object") > -1));
			if ( needsQuotes && (defaultValue != null ) && (!"null".equals(defaultValue)))  {
				writer.append("\"");
			}
			writer.append( defaultValue );
			if ( needsQuotes && (defaultValue != null ) && (!"null".equals(defaultValue)))  {
				writer.append("\"");
			}
			writer.append(";");

			// Start of Value Expression code
			writer.append("\n\t\tValueExpression ve = getValueExpression( PropertyKeys.");
			writer.append( pseudoFieldName );
			writer.append(".name() );");

			writer.append("\n\t\tif (ve != null) {" );
			// For primitives, don't overwrite a default value with a null value obtained from
			// the value expression. For the stateHelper, we're the ones putting those values
			// into the map, hence a value wont be found there.
			if (isPrimitive) {
				writer.append("\n\t\t\tObject o = ve.getValue( getFacesContext().getELContext() );");
				writer.append("\n\t\t\tif (o != null) { " );
				writer.append("\n\t\t\t\tretVal = (").append( internalType ).append
										   (") o; ");
				writer.append("\n\t\t\t}"); 
			} else {
				writer.append("\n\t\t\t\tretVal = (").append( internalType ).append
										   (") ve.getValue( getFacesContext().getELContext() ); ");
			}
			writer.append("\n\t\t} else {");
			writer.append("\n\t\t\tStateHelper sh = getStateHelper(); ");
			writer.append("\n\t\t\tString valuesKey = PropertyKeys.").append(pseudoFieldName).append(".name() + \"_rowValues\";");
			writer.append("\n\t\t\tMap clientValues = (Map) sh.get(valuesKey);");
            writer.append("\n\t\t\tboolean mapNoValue = false;");
			// differentiate between the case where the map has clientId and it's value is null
			// verses it not existing in the map at all.
            writer.append("\n\t\t\tif (clientValues != null) { ");

			writer.append("\n\t\t\t\tString clientId = getClientId();");
			writer.append("\n\t\t\t\tif (clientValues.containsKey( clientId ) ) { ");
			writer.append("\n\t\t\t\t\tretVal = (").append(internalType).append(") clientValues.get(clientId); ");
            writer.append("\n\t\t\t\t} else { ");
            writer.append("\n\t\t\t\t\tmapNoValue=true;");
            writer.append("\n\t\t\t\t}");
            writer.append("\n\t\t\t}");


			writer.append("\n\t\t\tif (mapNoValue || clientValues == null ) { ");
            writer.append("\n\t\t\t\tString defaultKey = PropertyKeys.").append(pseudoFieldName).append(".name() + \"_defaultValues\";");
            writer.append("\n\t\t\t\tMap defaultValues = (Map) sh.get(defaultKey); ");
            writer.append("\n\t\t\t\tif (defaultValues != null) { ");
            writer.append("\n\t\t\t\t\tif (defaultValues.containsKey(\"defValue\" )) {");
            writer.append("\n\t\t\t\t\t\tretVal = (").append(internalType).append(") defaultValues.get(\"defValue\"); ");
            writer.append("\n\t\t\t\t\t}");
            writer.append("\n\t\t\t\t}");
			writer.append("\n\t\t\t}");
			writer.append("\n\t\t}");
			writer.append("\n\t\treturn retVal;");
		} else {
			writer.append("\n\t\treturn super.");
			if (isBoolean) {
				writer.append("is");
			} else {
				writer.append("get");
			}
			writer.append(methodName + "();");
		}
        writer.append("\n\t}\n");
	}


	private void addJavaDoc(String name, boolean isSetter, String doc) {
		writer.append("\n\t/**\n");
		if (isSetter) {
			writer.append("\t* <p>Set the value of the <code>");
		} else {
			writer.append("\t* <p>Return the value of the <code>");
		}
		writer.append(name);
		writer.append("</code> property.</p>");
		if (doc != null && !"".equals(doc)) {
			String[] lines = doc.split("\n");
			writer.append("\n\t* <p>Contents:");

			for (int j=0; j < lines.length; j++){
				if (j>0) {
					writer.append("\n\t* ");
				}
				writer.append(lines[j]);
				if (j == (lines.length-1)) {
					writer.append("</p>");
				}                        
			}
		} 
		writer.append("\n\t*/\n");
	}



	private void addFacet(Class clazz, Component component) {
		Iterator<Field> iterator = getComponentContext().getFieldsForFacet().values().iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			Facet facet = (Facet)field.getAnnotation(Facet.class);
			String facetName = field.getName();
			addJavaDoc(field.getName() + " facet", true, facet.javadocSet());
			writer.append("\tpublic void set");
			writer.append(field.getName().substring(0,1).toUpperCase());
			writer.append(field.getName().substring(1));
			writer.append("Facet");

			writer.append("(");
			writer.append(field.getType().getName());
			writer.append(" ");
			writer.append(field.getName());
			writer.append(") {\n\t\tgetFacets().put(\"");
			writer.append(facetName);
			writer.append("\", ");
			writer.append(field.getName());
			writer.append(");\n");
			writer.append("\t}\n");


			//getter
			addJavaDoc(field.getName() + " facet", false, facet.javadocGet());
			writer.append("\tpublic ");
			writer.append(field.getType().getName());
			writer.append(" ");
			writer.append("get");
			writer.append(field.getName().substring(0,1).toUpperCase());
			writer.append(field.getName().substring(1));
			writer.append("Facet");
			writer.append("() {\n");
			writer.append("\t\t return getFacet(\"");
			writer.append(facetName);
			writer.append("\");\n\t}\n");

		}

	}

	public void addFieldGetterSetter(Field field, org.icefaces.component.annotation.Field fieldAnnotation) {
		// [Art] PropertyValues prop = getComponentContext().getPropertyValuesMap().get(field);

		boolean isBoolean = field.getType().getName().endsWith("boolean")||
		field.getType().getName().endsWith("Boolean");

        
        // primitive properties are supported (ones with values
        // even if no default is specified in the Meta). Wrapper properties
        // (null default and settable values) are also supported
        // There are four property names which must be forced to be primitive                                L
        // or else the generated signiture clashes with the property names in
        // EditableValueHolder
        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey( field.getName().toString().trim() );

          String returnAndArgumentType = field.getType().getName();

        // If primitive property, get the primitive return type
        // otherwise leave it as is.
        if (isPrimitive) {
            if (GeneratorContext.WrapperTypes.containsKey( field.getType().getName() )) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get( field.getType().getName() );
            }
        }

		String methodName;
        // The publicly exposed property name. Will differ from the field name
        // if the field name is a java keyword
		String pseudoFieldName;
        String camlCaseMethodName;
		/* [Art]
        if (!"null".equals(prop.name) ) {
            methodName = prop.name;
            pseudoFieldName = methodName + "Val";
        } else {
		*/
            methodName = field.getName();
            pseudoFieldName = methodName;
        // [Art] }
        camlCaseMethodName = methodName.substring(0,1).toUpperCase() + methodName.substring(1);

		//-------------------------------------
		// writing Setter
        //-------------------------------------

		// [Art] addJavaDoc(pseudoFieldName, true, prop.javadocSet);
		writer.append("\tpublic void set");
		writer.append(camlCaseMethodName);

        // Allow java autoconversion to deal with most of the conversion between
        // primitive types and Wrapper classes
		writer.append("(");
        writer.append( returnAndArgumentType );
		writer.append(" ");
		writer.append(field.getName());
		writer.append(") {");

		/* [Art]
		if (!prop.isDelegatingProperty) {
			writer.append("\n\t\tValueExpression ve = getValueExpression(PropertyKeys.");
			writer.append(pseudoFieldName);
			writer.append(".name() );");
//			writer.append("\n\t\tMap clientValues = null;");
			writer.append("\n\t\tif (ve != null) {");
			writer.append("\n\t\t\t// map of style values per clientId");
			writer.append("\n\t\t\tve.setValue(getFacesContext().getELContext(), ");
			writer.append( field.getName() );
			writer.append(" );");

			writer.append("\n\t\t} else { ");

//		    writer.append("\n\t\t\tMap clientValues = (Map) sh.get(valuesKey); ");
//
//			writer.append("\n\t\t\tif (clientValues == null) {" );
//            writer.append("\n\t\t\t\tclientValues = new HashMap(); ");
////            writer.append("\n\t\t\t\tmarkInitialState();");
//            writer.append("\n\t\t\t\tsh.put(PropertyKeys.");
//			writer.append(field.getName());
//			writer.append(", clientValues ); ");
//			writer.append("\n\t\t\t}");
			writer.append("\n\t\t\tPhaseId pi = getFacesContext().getCurrentPhaseId();");
			*/
            writer.append("\n\t\t\tStateHelper sh = getStateHelper(); ");
            
			/* [Art]
			writer.append("\n\t\t\tif (pi.equals(PhaseId.RENDER_RESPONSE) || pi.equals(PhaseId.RESTORE_VIEW))  {");
            // Here,

            writer.append("\n\t\t\t\tString defaultKey = PropertyKeys.").append( pseudoFieldName ).
                                append(".name() + \"_defaultValues\";" );
            writer.append("\n\t\t\t\tMap clientDefaults = (Map) sh.get(defaultKey);");

            writer.append("\n\t\t\t\tif (clientDefaults == null) { ");
            writer.append("\n\t\t\t\t\tclientDefaults = new HashMap(); ");
            writer.append("\n\t\t\t\t\tclientDefaults.put(\"defValue\"," ).append( field.getName().toString() ).append(");");
            writer.append("\n\t\t\t\t\tsh.put(defaultKey, clientDefaults); "); 
            writer.append("\n\t\t\t\t} "); 


			writer.append("\n\t\t\t} else {");
			*/
			writer.append("\n\t\t\t\tString clientId = getClientId();");
            writer.append("\n\t\t\t\tString valuesKey = PropertyKeys.").append( pseudoFieldName ).
                    append(".name() + \"_rowValues\"; ");
            writer.append("\n\t\t\t\tMap clientValues = (Map) sh.get(valuesKey); ");
            writer.append("\n\t\t\t\tif (clientValues == null) {");
            writer.append("\n\t\t\t\t\tclientValues = new HashMap(); ");
            writer.append("\n\t\t\t\t}");
            writer.append("\n\t\t\t\tclientValues.put(clientId, " ).append( field.getName().toString()).
                    append(");");

            writer.append("\n\t\t\t\t//Always re-add the delta values to the map. JSF merges the values into the main map" );
            writer.append("\n\t\t\t\t//and values are not state saved unless they're in the delta map. " );

            writer.append("\n\t\t\t\tsh.put(valuesKey, clientValues);" );

		/* [Art]
			writer.append("\n\t\t\t}" );
			writer.append("\n\t\t}" );
		} else {
			writer.append("\n\t\tsuper.set" + methodName + "(" + field.getName() + ");");
		}
		*/
        writer.append("\n\t}\n" );

        //-----------------
		//getter
        //-----------------

		// [Art] addJavaDoc(pseudoFieldName, false, prop.javadocGet);

        // Internal value representation is always Wrapper type
        String internalType = returnAndArgumentType;
        if (GeneratorContext.InvWrapperTypes.containsKey( returnAndArgumentType ) ) {
            internalType = GeneratorContext.InvWrapperTypes.get( returnAndArgumentType );
        }

		writer.append("\tpublic ");
        writer.append( returnAndArgumentType );
		writer.append(" ");

		if (isBoolean) {
			writer.append("is");
		} else {
			writer.append("get");
		}
		writer.append(camlCaseMethodName);
		writer.append("() {");

		// [Art] if (!prop.isDelegatingProperty) {
			// start of the code
			writer.append("\n\t\t").append(internalType).append(" retVal = ");
						
			// No defined default value is returned as the string "null". This has to
			// be handled for various cases. primitives must have a default of some kind
			// and Strings have to return null (not "null") to work.
			String defaultValue = fieldAnnotation.defaultValue(); // [Art] prop.defaultValue;
			Log.fine("Evaluating field name: " + field.getName().toString().trim() + ", isPRIMITIVE " +
							   isPrimitive + ", defaultValue:[" + defaultValue + "], isNull:" + (defaultValue == null));

			if (isPrimitive && (defaultValue == null || defaultValue.equals("") || defaultValue.equals("null"))) {
				defaultValue = GeneratorContext.PrimitiveDefaults.get( field.getType().toString().trim() );
			}

            boolean needsQuotes = ((internalType.indexOf("String") > -1) || (internalType.indexOf("Object") > -1));
			if ( needsQuotes && (defaultValue != null ) && (!"null".equals(defaultValue)))  {
				writer.append("\"");
			}
			writer.append( defaultValue );
			if ( needsQuotes && (defaultValue != null ) && (!"null".equals(defaultValue)))  {
				writer.append("\"");
			}
			writer.append(";");

			/* [Art]
			// Start of Value Expression code
			writer.append("\n\t\tValueExpression ve = getValueExpression( PropertyKeys.");
			writer.append( pseudoFieldName );
			writer.append(".name() );");

			writer.append("\n\t\tif (ve != null) {" );
			// For primitives, don't overwrite a default value with a null value obtained from
			// the value expression. For the stateHelper, we're the ones putting those values
			// into the map, hence a value wont be found there.
			if (isPrimitive) {
				writer.append("\n\t\t\tObject o = ve.getValue( getFacesContext().getELContext() );");
				writer.append("\n\t\t\tif (o != null) { " );
				writer.append("\n\t\t\t\tretVal = (").append( internalType ).append
										   (") o; ");
				writer.append("\n\t\t\t}"); 
			} else {
				writer.append("\n\t\t\t\tretVal = (").append( internalType ).append
										   (") ve.getValue( getFacesContext().getELContext() ); ");
			}
			writer.append("\n\t\t} else {");
			*/
			writer.append("\n\t\t\tStateHelper sh = getStateHelper(); ");
			writer.append("\n\t\t\tString valuesKey = PropertyKeys.").append(pseudoFieldName).append(".name() + \"_rowValues\";");
			writer.append("\n\t\t\tMap clientValues = (Map) sh.get(valuesKey);");
            writer.append("\n\t\t\tboolean mapNoValue = false;");
			// differentiate between the case where the map has clientId and it's value is null
			// verses it not existing in the map at all.
            writer.append("\n\t\t\tif (clientValues != null) { ");

			writer.append("\n\t\t\t\tString clientId = getClientId();");
			writer.append("\n\t\t\t\tif (clientValues.containsKey( clientId ) ) { ");
			writer.append("\n\t\t\t\t\tretVal = (").append(internalType).append(") clientValues.get(clientId); ");
            writer.append("\n\t\t\t\t} else { ");
            writer.append("\n\t\t\t\t\tmapNoValue=true;");
            writer.append("\n\t\t\t\t}");
            writer.append("\n\t\t\t}");


			writer.append("\n\t\t\tif (mapNoValue || clientValues == null ) { ");
            writer.append("\n\t\t\t\tString defaultKey = PropertyKeys.").append(pseudoFieldName).append(".name() + \"_defaultValues\";");
            writer.append("\n\t\t\t\tMap defaultValues = (Map) sh.get(defaultKey); ");
            writer.append("\n\t\t\t\tif (defaultValues != null) { ");
            writer.append("\n\t\t\t\t\tif (defaultValues.containsKey(\"defValue\" )) {");
            writer.append("\n\t\t\t\t\t\tretVal = (").append(internalType).append(") defaultValues.get(\"defValue\"); ");
            writer.append("\n\t\t\t\t\t}");
            writer.append("\n\t\t\t\t}");
			writer.append("\n\t\t\t}");
			// [Art] writer.append("\n\t\t}");
			writer.append("\n\t\treturn retVal;");
		/* [Art]
		} else {
			writer.append("\n\t\treturn super.");
			if (isBoolean) {
				writer.append("is");
			} else {
				writer.append("get");
			}
			writer.append(methodName + "();");
		}
		*/
        writer.append("\n\t}\n");
	}

	
	private void addInternalFields() {
		Iterator<Field> fields = getComponentContext().getInternalFieldsForComponentClass().values().iterator();
		while (fields.hasNext()) {
			Field field = fields.next();
			org.icefaces.component.annotation.Field fieldAnnotation = (org.icefaces.component.annotation.Field)field.getAnnotation(org.icefaces.component.annotation.Field.class);
			addFieldGetterSetter(field, fieldAnnotation);
		}
/*
		Map<String, Field> nonTransientProperties = new HashMap<String, Field>();
		//write properties
		Iterator<Field> fields = getComponentContext().getInternalFieldsForComponentClass().values().iterator();
		while (fields.hasNext()) {
			Field field = fields.next();
			org.icefaces.component.annotation.Field fieldAnnotation = (org.icefaces.component.annotation.Field)field.getAnnotation(org.icefaces.component.annotation.Field.class);

			writer.append("\tprotected ");
			writer.append(field.getType().getName());
			writer.append(" ");
			writer.append(field.getName());
			String defaultValue = fieldAnnotation.defaultValue();
			boolean defaultValueIsStringLiteral = fieldAnnotation.defaultValueIsStringLiteral();            
			if (!fieldAnnotation.isTransient()) {
				nonTransientProperties.put(field.getName(), field);
			}
			if (!"null".equals(defaultValue)) {
				writer.append(" = ");
				if (field.getType().getName().endsWith("String") &&
						defaultValueIsStringLiteral) {
					writer.append("\"");
					writer.append(defaultValue);
					writer.append("\"");
				} else {
					writer.append(defaultValue);
				}
			}
			writer.append(";\n");

		}


		//write saveState
		fields = nonTransientProperties.values().iterator();
		writer.append("\n\tprivate Object[] values;\n");
		writer.append("\n\tpublic Object saveState(FacesContext context) {\n");
		writer.append("\t\tif (context == null) {\n");
		writer.append("\t\t\tthrow new NullPointerException();\n\t\t}");
		writer.append("\n\t\tif (values == null) {\n");
		writer.append("\t\t\tvalues = new Object[");
		writer.append(getComponentContext().getInternalFieldsForComponentClass().values().size()+1);
		writer.append("];\n\t\t}\n");
		writer.append("\t\tvalues[0] = super.saveState(context);\n");


		int i=1;
		while (fields.hasNext()) {
			Field field = fields.next();
			writer.append("\t\tvalues[");
			writer.append(i++);
			writer.append("] = ");
			writer.append(field.getName());
			writer.append(";\n");
		}
		writer.append("\t\treturn (values);\n");
		writer.append("\t}\n");



		//writer restoreState
		fields = nonTransientProperties.values().iterator();
		writer.append("\n\tpublic void restoreState(FacesContext context, Object state) {\n");
		writer.append("\t\tif (context == null) {\n");
		writer.append("\t\t\tthrow new NullPointerException();\n\t\t}");
		writer.append("\n\t\tif (state == null) {\n");
		writer.append("\t\t\treturn;\n\t\t}\n");
		writer.append("\t\tvalues = (Object[]) state;\n");
		writer.append("\t\tsuper.restoreState(context, values[0]);\n");


		i=1;
		while (fields.hasNext()) {
			Field field = fields.next();
			writer.append("\t\t");
			writer.append(field.getName());
			writer.append(" = ");
			writer.append("(");
			writer.append(field.getType().getName());
			writer.append(") values[");
			writer.append(i++);
			writer.append("];\n");
		}
		writer.append("\t}\n");
*/
	}

	private void handleAttribute() {
		writer.append("\t\tprivate void handleAttribute(String name, Object value) {\n");
		writer.append("\t\t\tList<String> setAttributes = (List<String>) this.getAttributes().get(\"javax.faces.component.UIComponentBase.attributesThatAreSet\");\n");
		writer.append("\t\t\tif (setAttributes == null) {\n");
		writer.append("\t\t\t\tString cname = this.getClass().getName();\n");
		writer.append("\t\t\t\tif (cname != null) {\n");
		writer.append("\t\t\t\t\tsetAttributes = new ArrayList<String>(6);\n");
		writer.append("\t\t\t\t\tthis.getAttributes().put(\"javax.faces.component.UIComponentBase.attributesThatAreSet\", setAttributes);\n");
		writer.append("\t\t\t}\n\t\t}\n");
		writer.append("\t\tif (setAttributes != null) {\n");
		writer.append("\t\t\tif (value == null) {\n");
		writer.append("\t\t\t\tValueExpression ve = getValueExpression(name);\n");
		writer.append("\t\t\t\tif (ve == null) {\n");
		writer.append("\t\t\t\t\tsetAttributes.remove(name);\n");
		writer.append("\t\t\t}\n");
		writer.append("\t\t\t\t\t} else if (!setAttributes.contains(name)) {\n");
		writer.append("\t\t\t\t\t\tsetAttributes.add(name);\n");
		writer.append("\t\t\t}\n");
		writer.append("\t\t}\n");
		writer.append("\t}\n");
	}

	public void build() {
		Component component = (Component) getComponentContext().getActiveClass().getAnnotation(Component.class);
		startComponentClass(getComponentContext().getActiveClass(), component);
		addProperties(getComponentContext().getPropertyFieldsForComponentClassAsList());
		addFacet(getComponentContext().getActiveClass(), component);
		addInternalFields();
		handleAttribute();        
		endComponentClass();
	}



}
