/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.generator.behavior;

import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.meta.annotation.ClientEvent;
 
public class ClientBehaviorHolder extends Behavior {

	public ClientBehaviorHolder() {
		super(ClientBehaviorHolder.class);
	}

	@Override
	public boolean hasBehavior(Class clazz) {
		return clazz.isAnnotationPresent(org.icefaces.ace.meta.annotation.ClientBehaviorHolder.class);
	}


	public boolean hasInterface() {
		return true;
	}

	public String getInterfaceName() {
		return "IceClientBehaviorHolder";
	}
	
	public void addImportsToComponent(StringBuilder stringBuilder) {
		stringBuilder.append("import org.icefaces.ace.api.IceClientBehaviorHolder;\n");
		stringBuilder.append("import java.util.Collection;\n");
	}	
	
	public void addCodeToComponent(StringBuilder output) {
		org.icefaces.ace.meta.annotation.ClientBehaviorHolder anno = (org.icefaces.ace.meta.annotation.ClientBehaviorHolder)
			GeneratorContext.getInstance().getActiveMetaContext().getActiveClass().getAnnotation(org.icefaces.ace.meta.annotation.ClientBehaviorHolder.class);
		ClientEvent[] events = anno.events();
		output.append("\n\tCollection<String> eventNames = null;");
		output.append("\n\tpublic Collection<String> getEventNames() {");
		output.append("\n\tif (eventNames == null) {");
		output.append("\n\t\teventNames = new ArrayList<String>();");
		for (int i = 0; i < events.length; i++) {
			output.append("\n\t\teventNames.add(\""+ events[i].name() +"\");");
		}			
		output.append("\n\t}");
		output.append("\n\t\treturn eventNames;");
		output.append("\n\t}\n");
		
		output.append("\n\tpublic String getDefaultEventName() {");
		output.append("\n\t\treturn \"" + anno.defaultEvent() + "\";");
		output.append("\n\t}\n");

		output.append("\n\tMap<String, String> defaultRenderMap = null;");
		output.append("\n\tpublic String getDefaultRender(String event) {");
		output.append("\n\tif (defaultRenderMap == null) {");
		output.append("\n\t\tdefaultRenderMap = new HashMap<String, String>();");
		for (int i = 0; i < events.length; i++) {
			output.append("\n\t\tdefaultRenderMap.put(\""+ events[i].name() +"\",\"" + events[i].defaultRender() + "\");");
		}			
		output.append("\n\t}");
		output.append("\n\t\treturn defaultRenderMap.get(event);");
		output.append("\n\t}\n");

		output.append("\n\tMap<String, String> defaultExecuteMap = null;");
		output.append("\n\tpublic String getDefaultExecute(String event) {");
		output.append("\n\tif (defaultExecuteMap == null) {");
		output.append("\n\t\tdefaultExecuteMap = new HashMap<String, String>();");
		for (int i = 0; i < events.length; i++) {
			output.append("\n\t\tdefaultExecuteMap.put(\""+ events[i].name() +"\",\"" + events[i].defaultExecute() + "\");");
		}			
		output.append("\n\t}");
		output.append("\n\t\treturn defaultExecuteMap.get(event);");
		output.append("\n\t}\n");
	}
}
