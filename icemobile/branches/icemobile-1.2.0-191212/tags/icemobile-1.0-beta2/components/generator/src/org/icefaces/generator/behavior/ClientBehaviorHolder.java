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

import org.icefaces.generator.context.GeneratorContext;


public class ClientBehaviorHolder extends Behavior {

    public ClientBehaviorHolder() {
        super(ClientBehaviorHolder.class);
    }

    @Override
    public boolean hasBehavior(Class clazz) {
        return clazz.isAnnotationPresent(org.icefaces.component.annotation.ClientBehaviorHolder.class);
    }


    public boolean hasInterface() {
        return true;
    }

    public String getInterfaceName() {
        return "ClientBehaviorHolder";
    }

    public void addImportsToComponent(StringBuilder stringBuilder) {
        stringBuilder.append("import javax.faces.component.behavior.ClientBehaviorHolder;\n");
        stringBuilder.append("import java.util.Collection;\n");
    }

    public void addCodeToComponent(StringBuilder output) {
        org.icefaces.component.annotation.ClientBehaviorHolder anno = (org.icefaces.component.annotation.ClientBehaviorHolder)
                GeneratorContext.getInstance().getActiveComponentContext().getActiveClass().getAnnotation(org.icefaces.component.annotation.ClientBehaviorHolder.class);
        output.append("\n\tCollection<String> eventNames = null;");
        output.append("\n\tpublic Collection<String> getEventNames() {");
        output.append("\n\tif (eventNames == null) {");
        output.append("\n\t\teventNames = new ArrayList<String>();");
        for (String event : anno.events()) {
            output.append("\n\t\teventNames.add(\"" + event + "\");");
        }
        output.append("\n\t}");
        output.append("\n\t\treturn eventNames;");
        output.append("\n\t}\n");

        output.append("\n\tpublic String getDefaultEventName() {");
        output.append("\n\t\treturn \"" + anno.defaultEvent() + "\";");
        output.append("\n\t}\n");
    }
}
