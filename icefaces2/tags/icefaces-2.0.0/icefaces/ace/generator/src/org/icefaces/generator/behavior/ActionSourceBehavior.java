package org.icefaces.generator.behavior;

import java.lang.reflect.Field;
import java.util.Iterator;

import javax.el.MethodExpression;

import org.icefaces.component.annotation.*;


public class ActionSourceBehavior extends Behavior{
//	private final String ACTION_EXPRESSION = "actionExpression";
	
	@Property(expression=Expression.METHOD_EXPRESSION)
	private MethodExpression actionExpression;
	
	@Property(expression=Expression.METHOD_EXPRESSION)
	private MethodExpression actionListener;
	
	public ActionSourceBehavior() {
		super(ActionSourceBehavior.class);
	}
	public void addCodeToComponent(StringBuilder output) {
		output.append("\n\t/** ");
		output.append("\n\t* @throws NullPointerException {@inheritDoc}");
		output.append("\n\t*/ ");
		output.append("\n\tpublic void addActionListener(ActionListener listener) {");

		output.append("\n\t\taddFacesListener(listener);");

		output.append("\n\t}");
		output.append("\n");        		    
		output.append("\n\tpublic ActionListener[] getActionListeners() {");

		output.append("\n\t\tActionListener al[] = (ActionListener [])");
		output.append("\n\t\tgetFacesListeners(ActionListener.class);");
		output.append("\n\t\treturn (al);");

		output.append("\n\t}");                    
        
		output.append("\n");  
		output.append("\n\t/**");
    	output.append("\n\t * @throws NullPointerException {@inheritDoc}");
        output.append("\n\t */");
        output.append("\n\tpublic void removeActionListener(ActionListener listener) {");
        output.append("\n\t\tremoveFacesListener(listener);");
		output.append("\n\t}");
		
		output.append("\n\tpublic void broadcast(FacesEvent event) throws AbortProcessingException {");
		output.append("\n\t\t// Perform standard superclass processing (including calling our");
		output.append("\n\t\t// ActionListeners)");
        output.append("\n\t\tsuper.broadcast(event);");

		output.append("\n\t\tif (event instanceof ActionEvent) {");
		output.append("\n\t\t\tFacesContext context = getFacesContext();");
            
		output.append("\n\t\t\t// Notify the specified action listener method (if any)");
		output.append("\n\t\t\tMethodExpression mb = getActionListener();");
		output.append("\n\t\tif (mb != null) {");
		output.append("\n\t\t\t\tmb.invoke(context, new Object[] { event });");
		output.append("\n\t\t\t}");

		output.append("\n\t\t// Invoke the default ActionListener");
		output.append("\n\t\tActionListener listener =");
		output.append("\n\t\t\t   context.getApplication().getActionListener();");
		output.append("\n\t\t\t  if (listener != null) {");
		output.append("\n\t\t\tlistener.processAction((ActionEvent) event);");
        output.append("\n\t\t\t}");
        output.append("\n\t\t}");
	    output.append("\n\t}");		

		
	}

	public void addImportsToComponent(StringBuilder stringBuilder) {
		stringBuilder.append("import javax.faces.application.Application;\n");
		stringBuilder.append("import javax.faces.event.*;\n");
		stringBuilder.append("import javax.faces.component.ActionSource2;\n");		
		
		
	}


	
	public void addPropertiesEnumToComponent(StringBuilder output) {
		Iterator<Field> fields = getProperties().values().iterator();
		while (fields.hasNext()) {
			output.append("\t\t");     
			output.append(fields.next().getName()); 
			output.append(",\n"); 
		}
		
	}
	
	public boolean hasBehavior(Class clazz) {
		return clazz.isAnnotationPresent(ActionSource.class);
	}

	public boolean hasInterface() {
		return true;
	}

	public String getInterfaceName() {
		return "ActionSource2";
	}

	
	
}
