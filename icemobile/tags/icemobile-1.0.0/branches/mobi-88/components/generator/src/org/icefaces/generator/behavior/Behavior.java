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

package org.icefaces.generator.behavior;

import org.icefaces.component.annotation.Property;
import org.icefaces.generator.artifacts.ComponentArtifact;
import org.icefaces.generator.context.ComponentContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public abstract class Behavior {
    private Map<String, Field> properties = new HashMap<String, Field>();

    public Behavior(Class clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Property.class)) {
                getProperties().put(field.getName(), field);
            }
        }
    }

    public Map<String, Field> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Field> properties) {
        this.properties = properties;
    }

    public boolean hasInterface() {
        return false;
    }

    public String getInterfaceName() {
        return null;
    }

    public abstract boolean hasBehavior(Class clazz);

    public void addImportsToComponent(StringBuilder stringBuilder) {

    }

    public void addCodeToComponent(StringBuilder stringBuilder) {

    }

    public void addImportsToTag(StringBuilder stringBuilder) {

    }

    public void addProperties(ComponentContext componentContext) {

    }

    public void addPropertiesEnumToComponent(StringBuilder output) {

    }

    public void addGetterSetter(ComponentArtifact artifact, StringBuilder output) {
        Iterator<Field> fields = getProperties().values().iterator();
        while (fields.hasNext()) {
            artifact.addGetterSetter(fields.next());
        }
    }
}
