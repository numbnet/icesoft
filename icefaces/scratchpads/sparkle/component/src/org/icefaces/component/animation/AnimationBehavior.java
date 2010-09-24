package org.icefaces.component.animation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.behavior.FacesBehavior;
 
import javax.faces.context.FacesContext;

import org.icefaces.component.utils.ScriptWriter;
@FacesBehavior("org.icefaces.animation.Animation")
@ResourceDependencies({
	@ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
    @ResourceDependency(name ="anim/anim-min.js",library = "yui/3_1_1"),
    @ResourceDependency(name ="plugin/plugin-min.js",library = "yui/3_1_1"),    
    @ResourceDependency(name ="pluginhost/pluginhost-min.js",library = "yui/3_1_1"),      
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),    
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),   
    @ResourceDependency(name="animation.js",library="org.icefaces.component.animation"),
    @ResourceDependency(name="animation.css",library="org.icefaces.component.animation")   
})
public class AnimationBehavior extends ClientBehaviorBase{
	public final static String BEHAVIOR_ID = "org.icefaces.animation.Animation";
    private Map<String, ValueExpression> bindings;
	private String effectsLib = "ice.animation.";
	private boolean usingStyleClass;
	private boolean run;
	Effect effect = new Fade();
	private String style;
	
	 
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setEffectObject(Effect effect) {
		this.effect = effect;
        clearInitialState();
    }
    
    public Effect getEffect() {
        return (Effect) eval("effectObject", effect);

    }


    
 
	public void setName(String name) {
		use(name);
        clearInitialState();
    }
    
    public String getName() {
        return effect.getClass().getSimpleName();

    }

	public void setRun(boolean run) {
        ValueExpression expression = getValueExpression("run");

        if (expression != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            expression.setValue(ctx.getELContext(), run);
        } else {
        
        	this.run = run;
        }
        clearInitialState();
    }
    
    public boolean isRun() {
        return (Boolean) eval("run", run);

    }
    
    private void run(FacesContext facesContext, UIComponent uiComponent) {
//    	if (!isRun()) return;
//    		setRun(false);
//	    	try {
//				ScriptWriter.insertScript(facesContext, uiComponent, buildScript(uiComponent) );
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    }
	private void use(String name) {
		if ("Fade".equals(name)) {
			effect = new Fade();
		} else if ("Appear".equals(name)) {
			effect = new Appear();
		} else if ("Highlight".equals(name)) {
			effect = new Highlight();
		}
	}
	
	protected String getEffectsLib() {
		return effectsLib;
	}

	protected void setEffectsLib(String effectsLib) {
		this.effectsLib = effectsLib;
	}

	private String styleClass;
    public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
 
    
	public AnimationBehavior() {
		System.out.println("EffectBehavior initialized ");
	}
	
	public void setUsingStyleClass(boolean usingStyleClass) {
		this.usingStyleClass = usingStyleClass;
    }
    
    public boolean isUsingStyleClass() {
        return usingStyleClass;

    }

   
    
    public String getScript(ClientBehaviorContext behaviorContext) {
    	return getScript(behaviorContext, true);
    }

    public String getScript(ClientBehaviorContext behaviorContext, boolean run) {
    	if (!effect.getProperties().containsKey("event")) {
    		effect.getProperties().put("event", behaviorContext.getEventName());
    	}
    	if (!effect.getProperties().containsKey("name")) {
    		effect.getProperties().put("name", getName());
    	}  
    	effect.setSourceElement(behaviorContext.getComponent().getClientId());
    	if (behaviorContext.getComponent().getAttributes().get("styleClass") != null) {
    		effect.getProperties().put("componentStyleClass", behaviorContext.getComponent().getAttributes().get("styleClass"));
    	}
    	StringBuilder call = new StringBuilder();
    	call.append(getEffectsLib());
    	if(run) {
    		call.append("run");
    	} else {
    		call.append("register");
    	}
    	call.append("(");
    	call.append(effect.getPropertiesAsJSON());
    	call.append(");");
    	return call.toString();
    }
    
 
    
    public void decode(FacesContext context,
            UIComponent component) {
    	super.decode(context, component);
		Map map = context.getExternalContext().getRequestParameterMap();
		String id = "effect_style"+ component.getClientId();
		if (map.containsKey(id)) {
			setStyle(map.get(id).toString());
			System.out.println("Style found "+ map.get(id).toString());
		}
    }
    
    public void encodeBegin(FacesContext context, UIComponent uiComponent){

    	run(context, uiComponent);
    }
    protected Object eval(String propertyName, Object value) {

        ValueExpression expression = getValueExpression(propertyName);

        if (expression != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            return expression.getValue(ctx.getELContext());
        }
        
        if (value != null) {
            return value;
        }
        return null;
    }   
    
    public ValueExpression getValueExpression(String name) {

        if (name == null) {
            throw new NullPointerException();
        }

        return ((bindings == null) ? null : bindings.get(name));
    }

    /**
     * <p class="changed_added_2_0">Sets the {@link ValueExpression} 
     * used to calculate the value for the specified property name.</p>
     * </p>
     *
     * @param name Name of the property for which to set a
     *  {@link ValueExpression}
     * @param binding The {@link ValueExpression} to set, or <code>null</code>
     *  to remove any currently set {@link ValueExpression}
     *
     * @throws NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void setValueExpression(String name, ValueExpression binding) {

        if (name == null) {
            throw new NullPointerException();
        }

        if (binding != null) {

            if (binding.isLiteralText()) {
                setLiteralValue(name, binding);
            } else {
                if (bindings == null) {

                    // We use a very small initial capacity on this HashMap.
                    // The goal is not to reduce collisions, but to keep the
                    // memory footprint small.  It is very unlikely that an
                    // an AjaxBehavior would have more than 1 or 2 bound 
                    // properties - and even if more are present, it's okay
                    // if we have some collisions - will still be fast.
                    bindings = new HashMap<String, ValueExpression>(6,1.0f);
                }

                bindings.put(name, binding);
        		if ("effectObject".equals(name)) {
        			effect = ((Effect)binding.getValue(FacesContext.getCurrentInstance().getELContext()));
        			effect.setEffectBehavior(this);
        		} else if ("name".equals(name)) {
        			String effectName = ((String)binding.getValue(FacesContext.getCurrentInstance().getELContext()));
        			use(effectName);
        		}
            }
        } else {
            if (bindings != null) {
                bindings.remove(name);
                if (bindings.isEmpty()) {
                    bindings = null;
                }
            }
        }

        clearInitialState();
    }  

    protected void setLiteralValue(String propertyName,
            ValueExpression expression) {

		assert(expression.isLiteralText());
		
		Object value;
		javax.el.ELContext context = FacesContext.getCurrentInstance().getELContext();
		
		try {
		value = expression.getValue(context);
		} catch (javax.el.ELException ele) {
		throw new FacesException(ele);
		}
		if ("run".equals(propertyName)) {
			run = (Boolean)value;
		} else if ("name".equals(propertyName)){
			setName((String)value);
		}
    }   
 
	public interface IIterator {
		public void next (String name, AnimationBehavior effectBehavior);
	}
	
	public static abstract class Iterator implements IIterator {
		private UIComponent uiComponent;
		private Iterator(){
			
		}
		public Iterator(UIComponent uiComponent) {
			this.uiComponent = uiComponent;
		}
		
		public UIComponent getUIComponent() {
			return this.uiComponent;
		}
	}
}