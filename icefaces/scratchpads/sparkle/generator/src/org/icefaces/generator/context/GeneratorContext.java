package org.icefaces.generator.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 

import org.icefaces.component.annotation.PropertyTemplate;
import org.icefaces.generator.behavior.ActionSourceBehavior;
import org.icefaces.generator.behavior.Behavior;
import org.icefaces.generator.utils.FileWriter;
import org.icefaces.generator.xmlbuilder.FaceletTagLibBuilder;
import org.icefaces.generator.xmlbuilder.FacesConfigBuilder;
import org.icefaces.generator.xmlbuilder.TLDBuilder;

public class GeneratorContext{
	private static GeneratorContext generatorContext = null;
    public static final Map<String,String> WrapperTypes= new HashMap<String, String>();
    public static final Map<String,String> InvWrapperTypes= new HashMap<String, String>();
	private TLDBuilder tldBuilder = new TLDBuilder();
    private FacesConfigBuilder facesConfigBuilder = new FacesConfigBuilder(); 
    private FaceletTagLibBuilder faceletTagLibBuilder = new FaceletTagLibBuilder();
	private List<Class> components;
    private Map<String, Object> propertyTemplate = new HashMap<String, Object>();
    private ComponentContext activeComponentContext;
    public final static String shortName = "ice";    
    public final static String namespace = "http://www.icefaces.org/icefaces/components";
    private List<Behavior> behaviors = new ArrayList<Behavior>();
     public static final Map<String,String> SpecialReturnSignatures = new HashMap<String,String>();
     public static final Map<String,String> PrimitiveDefaults = new HashMap<String,String>();


	public List<Behavior> getBehaviors() {     
		return behaviors;
	}

	static {
        WrapperTypes.put("java.lang.Boolean", "boolean");
        WrapperTypes.put("java.lang.Byte", "byte");
        WrapperTypes.put("java.lang.Character", "char");
        WrapperTypes.put("java.lang.Double", "double");
        WrapperTypes.put("java.lang.Float", "float");
        WrapperTypes.put("java.lang.Integer", "int");
        WrapperTypes.put("java.lang.Long", "long");
        WrapperTypes.put("java.lang.Short", "short");

        InvWrapperTypes.put("boolean", "java.lang.Boolean");
        InvWrapperTypes.put("byte", "java.lang.Byte");
        InvWrapperTypes.put("char", "java.lang.Character");
        InvWrapperTypes.put("double", "java.lang.Double");
        InvWrapperTypes.put("float", "java.lang.Float");
        InvWrapperTypes.put("int", "java.lang.Integer");
        InvWrapperTypes.put("long", "java.lang.Long");
        InvWrapperTypes.put("short", "java.lang.Short");

        SpecialReturnSignatures.put("immediate","immediate");
        SpecialReturnSignatures.put("valid", "valid");
        SpecialReturnSignatures.put("required", "required");
        SpecialReturnSignatures.put("localValueSet", "localValueSet");

        PrimitiveDefaults.put("boolean", "false");
        PrimitiveDefaults.put("byte", "0");
        PrimitiveDefaults.put("char", "");
        PrimitiveDefaults.put("double", "0");
        PrimitiveDefaults.put("float", "0f");
        PrimitiveDefaults.put("int", "0");
        PrimitiveDefaults.put("long", "0l");
        PrimitiveDefaults.put("short", "0");

    }

	private GeneratorContext() {
		getBehaviors().add(new ActionSourceBehavior());
        components = FileWriter.getAnnotatedCompsList();
        loadPropertyTemplate();
	}
	
    public TLDBuilder getTldBuilder() {
		return tldBuilder;
	}

	public void setTldBuilder(TLDBuilder tldBuilder) {
		this.tldBuilder = tldBuilder;
	}

	public FacesConfigBuilder getFacesConfigBuilder() {
		return facesConfigBuilder;
	}

	public void setFacesConfigBuilder(FacesConfigBuilder facesConfigBuilder) {
		this.facesConfigBuilder = facesConfigBuilder;
	}

	public FaceletTagLibBuilder getFaceletTagLibBuilder() {
		return faceletTagLibBuilder;
	}

	public void setFaceletTagLibBuilder(FaceletTagLibBuilder faceletTagLibBuilder) {
		this.faceletTagLibBuilder = faceletTagLibBuilder;
	}
    
	public ComponentContext getActiveComponentContext() {
		return activeComponentContext;
	}

	public ComponentContext createComponentContext(Class clazz) {
		return new ComponentContext(clazz);
	}
	
	public void setActiveComponentContext(ComponentContext activeComponentContext) {
		this.activeComponentContext = activeComponentContext;
	}

	public Map<String, Object> getPropertyTemplate() {
		return propertyTemplate;
	}

	public void setPropertyTemplate(Map<String, Object> propertyTemplate) {
		this.propertyTemplate = propertyTemplate;
	}
	
	public static GeneratorContext getInstance() {
		if (generatorContext == null) {
			generatorContext = new GeneratorContext();
		}
		return generatorContext;
	}

    //this method loads predefine properties 
    private void loadPropertyTemplate() {
        Field[] fields = PropertyTemplate.class.getDeclaredFields();
        for (int i=0; i < fields.length; i++) {
            //its mean "public static", must be sun class name and its properties
            if (fields[i].getModifiers() == 9) {
                try {
                    
                    propertyTemplate.put(fields[i].getName(), fields[i].get(fields[i]));
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }            
            } else {
                propertyTemplate.put(fields[i].getName(), fields[i]);   
            }
        }
    }
    
    public List<Class> getComponents() {
    	return this.components;
    }
	
    public void release() {
        getTldBuilder().write();
        getFacesConfigBuilder().write();
        getFaceletTagLibBuilder().write();
    }    
}
