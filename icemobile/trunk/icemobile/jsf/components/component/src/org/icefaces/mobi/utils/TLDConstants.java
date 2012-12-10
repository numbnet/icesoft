package org.icefaces.mobi.utils;

public class TLDConstants {
    
    public static final String STYLE = "Sets the CSS style definition to be applied to this component.";
    public static final String STYLECLASS = "Sets the CSS class to apply to this component.";
    public static final String DISABLED = "Disables this component, so it does not receive focus or get submitted.";
    public static final String READONLY = "Sets this component to read only, so value cannot be changed.";
    public static final String TABINDEX = "The tabindex of this component.";
    public static final String SINGLESUBMIT = "When singleSubmit is \"true\", triggering an action on " +
    		"this component will submit and execute only this component only (equivalent to" +
    		" <f:ajax execute='@this' render='@all'> ). When singleSubmit is \"false\", triggering an " +
    		"action on this component will submit and execute the full form that this component " +
    		"is contained within.";
    public static final String VALUECHANGELISTENER = "MethodExpression representing a value change listener " +
    		"method that will be notified when a new value has been set for this component. The expression must " +
    		"evaluate to a public method that takes a ValueChangeEvent parameter, with a return type of void, " +
    		"or to a public method that takes no arguments with a return type of void. ";
    public static final String IMMEDIATE_INPUT = "Flag indicating that this component's value must be converted and " +
    		"validated immediately (that is, during Apply Request Values phase), rather than waiting until Process " +
    		"Validations phase. ";
}
