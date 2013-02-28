/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.generator.utils;

import java.lang.reflect.Field;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.JSP;
import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.TagHandlerType;

public class Utility {
    public static String getComponentType(Component component) {
        String componentType = component.componentType();
        if (Component.EMPTY.equals(componentType)) {
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
        if (Component.EMPTY.equals(componentFamily)) {
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
    
    public static String getGeneratedClassName(Component component) {
        String generatedClass = component.generatedClass();
        if (generatedClass.equals(Component.EMPTY)) {
            generatedClass = component.componentClass();
        } 
        return generatedClass;
    }
	
    public static String getGeneratedClassName(TagHandler tagHandler) {
        String generatedClass = tagHandler.generatedClass();
        if (generatedClass.equals(TagHandler.EMPTY)) {
            generatedClass = tagHandler.tagHandlerClass();
        } 
        return generatedClass;
    }

    public static String getRendererClassName(Component component) {
        String rendererClass = component.rendererClass();
        if (rendererClass.equals(Component.EMPTY)) {
            rendererClass = component.componentClass()+ "Renderer";
        }
        return rendererClass;
    }

    public static String getTagName(Class metaClass, JSP jsp) {
        String tagName = jsp.tagName();
        if (tagName.equals(JSP.EMPTY)) {
            /*
            StringBuilder sb = new StringBuilder(metaClass.getSimpleName());
            final String META = "Meta";
            if (META.contentEquals(sb.subSequence(sb.length()-META.length(), sb.length()))) { //sb.endsWith(META)
                sb.delete(sb.length()-META.length(), sb.length());
            }
            if (sb.length() >= 1) {
                sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
            }
            tagName = sb.toString();
            */
        }
        return tagName;
    }

    public static String getTagClassName(Class metaClass, JSP jsp) {
        String tagClass = jsp.tagClass();
        if (tagClass.equals(JSP.EMPTY)) {
            /*
            tagClass = jsp.componentClass()+ "Tag";
            */
        }
        return tagClass;
    }

    public static String getTagClassName(Component component) {
        String tagClass = component.tagClass();
        if (tagClass.equals(Component.EMPTY)) {
            tagClass = component.componentClass()+ "Tag";
        }
        return tagClass;
    }

    public static String getHandlerClassName(Component component) {
        String handlerClass = component.handlerClass();
        if (handlerClass.equals(Component.EMPTY)) {
            handlerClass = component.componentClass()+ "Handler";
        }
        return handlerClass;
    }

    public static boolean isManualTagClass(Component component) {
        return !Component.EMPTY.equals(component.tagClass());
    }

    public static boolean isManualHandlerClass(Component component) {
        return !Component.EMPTY.equals(component.handlerClass());
    }

	public static String getTagHandlerExtendsClassName(TagHandler tagHandler) {
		String extendsClass = tagHandler.extendsClass();
		if (extendsClass.equals(TagHandler.EMPTY)) {
			extendsClass = getDefaultTagHandlerExtendsClassName(tagHandler.tagHandlerType());
		}
		return extendsClass;
	}
	
	public static String getDefaultTagHandlerExtendsClassName(TagHandlerType type) {
	
		String classPackage = "javax.faces.view.facelets.";
		switch(type) {
			case ATTRIBUTE_HANDLER:
				return classPackage+"AttributeHandler";
			case BEHAVIOR_HANDLER:
				return classPackage+"BehaviorHandler";
			case COMPONENT_HANDLER:
				return classPackage+"ComponentHandler";
			case COMPOSITE_FACELET_HANDLER:
				return classPackage+"CompositeFaceletHandler";
			case CONVERTER_HANDLER:
				return classPackage+"ConverterHandler";
			case DELEGATING_META_TAG_HANDLER:
				return classPackage+"DelegatingMetaTagHandler";
			case FACELET_HANDLER:
				return classPackage+"FaceletHandler";
			case FACELETS_ATTACHED_OBJECT_HANDLER:
				return classPackage+"FaceletsAttachedObjectHandler";
			case FACET_HANDLER:
				return classPackage+"FacetHandler";
			case META_TAG_HANDLER:
				return classPackage+"MetaTagHandler";
			case TAG_HANDLER:
				return classPackage+"TagHandler";
			case TEXT_HANDLER:
				return classPackage+"TextHandler";
			case VALIDATOR_HANDLER:
				return classPackage+"ValidatorHandler";
			default:
				return classPackage+"TagHandler";
		}
	}
	
	public static String getDefaultTagHandlerConfigClass(TagHandlerType type) {
	
		String classPackage = "javax.faces.view.facelets.";
		switch(type) {
			case BEHAVIOR_HANDLER:
				return classPackage+"BehaviorConfig";
			case COMPONENT_HANDLER:
				return classPackage+"ComponentConfig";
			case CONVERTER_HANDLER:
				return classPackage+"ConverterConfig";
			case VALIDATOR_HANDLER:
				return classPackage+"ValidatorConfig";
			default:
				return classPackage+"TagConfig";
		}
	}

    public static String getSimpleNameOfClass(String className) {
        int classIndicator = className.lastIndexOf(".");
        return className.substring(classIndicator+1);
    }

    public static String getPackageNameOfClass(String className) {
        int classIndicator = className.lastIndexOf(".");
        if (classIndicator >= 0) {
            return className.substring(0, classIndicator);
        }
        return "";
    }

    public static String getPackagePathOfClass(String className) {
        String pack = getPackageNameOfClass(className);
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        return path;
    }

    public static String getArrayAwareType(Field field) {
        boolean isArray = field.getType().isArray();
        return isArray ? field.getType().getComponentType().getName() + "[]"
                       : field.getType().getName();
    }
}
