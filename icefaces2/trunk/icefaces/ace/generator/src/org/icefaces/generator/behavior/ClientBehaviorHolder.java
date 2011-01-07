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

package org.icefaces.generator.behavior;

import java.util.ArrayList;
import java.util.Collection;

 
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
		output.append("\n\tCollection<String> eventNames = null;");
		output.append("\n\tpublic Collection<String> getEventNames() {");
		output.append("\n\tif (eventNames == null) {");
		output.append("\n\t\teventNames = new ArrayList<String>();");
		output.append("\n\t\teventNames.add(\"transition\");");
		output.append("\n\t\teventNames.add(\"click\");");			
		output.append("\n\t}");
		output.append("\n\t\treturn eventNames;");
		output.append("\n\t}\n");
		
		output.append("\n\tpublic String getDefaultEventName() {");
		output.append("\n\t\treturn \"click\";");		
		output.append("\n\t}\n");
	}
}
