package org.icefaces.component.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;

public class PropertyTemplate {
    public final static String ONBLUR = "onblur";
    public final static String ONCHANGE = "onchange";
    public final static String ONCLICK = "onclick";
    public final static String ONDBLCLICK = "ondblclick";
    public final static String ONFOCUS = "onfocus";
    public final static String ONKEYDOWN = "onkeydown";
    public final static String ONKEYPRESS = "onkeypress";
    public final static String ONKEYUP = "onkeyup";
    public final static String ONMOUSEDOWN = "onmousedown";
    public final static String ONMOUSEMOVE = "onmousemove";
    public final static String ONMOUSEOUT = "onmouseout";
    public final static String ONMOUSEOVER = "onmouseover";
    public final static String ONMOUSEUP = "onmouseup";
    public final static String ONSELECT = "onselect";    
    public final static String JAVA_SCRIPT_HANDLERS = "JavaScriptHandlers";
    public final static String STYLE = "style";    
    public final static String STYLE_CLASS = "styleClass";  
    
    //------------------ onEvents
    @Property
    protected String onblur;
    
    @Property
    private String onchange;
    
    @Property
    private String onclick;
    
    @Property
    private String ondblclick;
    
    @Property
    private String onfocus;
    
    @Property
    private String onkeydown;
    
    @Property
    private String onkeypress;

    @Property
    private String onkeyup;
    
    @Property
    private String onmousedown;

    @Property
    private String onmousemove;
    
    @Property
    private String onmouseout;
    
    @Property
    private String onmouseover;
    
    @Property
    private String onmouseup;
    
    @Property
    private String onselect;
    
    public static List JavaScriptHandlers = new ArrayList();
    static {
        JavaScriptHandlers.add(ONBLUR);
        JavaScriptHandlers.add(ONCHANGE);
        JavaScriptHandlers.add(ONCLICK);
        JavaScriptHandlers.add(ONDBLCLICK);
        JavaScriptHandlers.add(ONFOCUS);
        JavaScriptHandlers.add(ONKEYDOWN);
        JavaScriptHandlers.add(ONKEYPRESS);
        JavaScriptHandlers.add(ONKEYUP);
        JavaScriptHandlers.add(ONMOUSEDOWN);
        JavaScriptHandlers.add(ONMOUSEMOVE);
        JavaScriptHandlers.add(ONMOUSEOUT);
        JavaScriptHandlers.add(ONMOUSEOVER);   
        JavaScriptHandlers.add(ONMOUSEUP);
        JavaScriptHandlers.add(ONSELECT);         
    }

    //------------------ Common attributes
    @Property(tlddoc="CSS style(s) to be applied when this component is rendered.")
    private String style;
    
    
    @Property(tlddoc="Space-separated list of CSS style class(es) to be applied " +
            "when this element is rendered. This value must be passed through as " +
            "the \"class\" attribute on generated markup.")
    private String styleClass;
    

    //------------------- UIComponentBase
    public final static String ID = "id";
    public final static String RENDERED = "rendered";
    public final static String BINDING = "binding";       
    @Property(tlddoc="The component identifier for this component. This value " +
            "must be unique within the closest parent component that is a naming container. ")
    protected String id;
    

    @Property( tlddoc="Flag indicating whether or not this component should " +
    		"be rendered (during Render Response Phase), or processed on any subsequent form submit. ")
    protected Boolean rendered;
    
    @Property( tlddoc="The ValueExpression linking this component to a property in a backing bean ")
    private UIComponent binding; 
    
    public static List UIComponentBase = new ArrayList();
    static {
        UIComponentBase.add(ID);
        UIComponentBase.add(RENDERED);
        UIComponentBase.add(BINDING);        
    } 
    
    //----------------------------------
    
    
    //------------------- UICommand    
    public final static String ACTION_EXPRESSION = "actionExpression";
    public final static String METHOD_BINDING_ACTION_LISTENER = "methodBindingActionListener";    
    public final static String IMMEDIATE = "immediate";      
    @Property(tlddoc="MethodBinding representing the application action to " +
    		"invoke when this component is activated by the user. The expression " +
    		"must evaluate to a either a String or a public method that takes no " +
    		"parameters, and returns a String (the logical outcome) which is " +
    		"passed to the NavigationHandler for this application.")
    protected MethodExpression actionExpression;
    
    
    @Property(tlddoc="MethodBinding representing an action listener method that " +
    		"will be notified when this component is activated by the user. The " +
    		"expression must evaluate to a public method that takes an ActionEvent " +
    		"parameter, with a return type of void.")
    protected MethodExpression methodBindingActionListener;
    
    
    @Property(tlddoc="Flag indicating that this component's value must be " +
    		"converted and validated immediately (that is, during Apply Request " +
    		"Values phase), rather than waiting until Process Validations phase.")
    protected boolean immediate;
    
    public static List UICommand = new ArrayList();
    static {
        UICommand.add(METHOD_BINDING_ACTION_LISTENER);
        UICommand.add(ACTION_EXPRESSION);
        UICommand.add(IMMEDIATE);
    }
    
    //--------------------------------------
    
    
    @Property(tlddoc="CSS style(s) to be applied when this component is rendered.")
    protected String layout;
    
    public static List HtmlPanelGroup = new ArrayList();
    static {
        HtmlPanelGroup.add(STYLE);
        HtmlPanelGroup.add(STYLE_CLASS);
        HtmlPanelGroup.addAll(JavaScriptHandlers);
    }
    
    
    //--------------
    
    /*
    
    protected String accesskey;
    protected String alt;
    protected String dir;
    protected String disabled;
    protected String image;
    protected String label;
    protected String lang;
    

    
    protected String readonly;
    protected String tabindex;
    protected String title;
    protected String type;
    protected String charset;
    protected String coords;
    protected String hreflang;
    protected String rel;
    protected String rev;
    protected String shape;
    protected String target;
    protected String bgcolor;
    protected String border;
    protected String frame;
    protected String rules;
    protected String summary;
    protected String width;
    protected String accept;
    protected String acceptcharset;
    protected String enctype;
    protected String height;
    protected String ismap;
    protected String longdesc;
    protected String usemap;
    protected String autocomplete;
    protected String maxlength;
*/
    
    
    //---------
}

