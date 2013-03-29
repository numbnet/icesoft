/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.generator.utils;

import org.icefaces.component.annotation.Component;

import java.lang.reflect.Field;

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
