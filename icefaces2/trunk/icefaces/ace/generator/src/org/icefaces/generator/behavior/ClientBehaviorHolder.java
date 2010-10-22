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
