package org.icefaces.component.effects;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
 
import javax.faces.context.FacesContext;

public class EffectBehavior extends ClientBehaviorBase{
	private static final String EFFECT_TYPE = "effectType";
    private Map<String, ValueExpression> bindings;
	private String effectsLib = "ice.yui3.effects";
	private boolean usingStyleClass;

	protected String getEffectsLib() {
		return effectsLib;
	}

	protected void setEffectsLib(String effectsLib) {
		this.effectsLib = effectsLib;
	}

	private String effectClass;
    public String getEffectClass() {
		return effectClass;
	}

	public void setEffectClass(String effectClass) {
		this.effectClass = effectClass;
	}
 
    
	public EffectBehavior() {
		System.out.println("EffectBehavior initialized ");
	}
	
	public void setUsingStyleClass(boolean usingStyleClass) {
		this.usingStyleClass = usingStyleClass;
    }
    
    public boolean isUsingStyleClass() {
        return usingStyleClass;

    }
    
    public String getName() {
    	return this.getClass().getSimpleName();
    }
 
    public String getScript(ClientBehaviorContext behaviorContext) {
     	return "new "+ getEffectsLib() + "['" + getName() + "']('"+ behaviorContext.getComponent().getClientId() +"').run()";
    }
    
    public void decode(FacesContext context,
            UIComponent component) {
    	super.decode(context, component);
		Map map = context.getExternalContext().getRequestParameterMap();
		String id = "effect_"+ component.getClientId();
		if (map.containsKey(id)) {
			setEffectClass(map.get(id).toString());
			System.out.println("Deocde Effect class found "+ getEffectClass());
		}
    }
    
    protected Object eval(String propertyName, Object value) {

        if (value != null) {
            return value;
        }

        ValueExpression expression = getValueExpression(propertyName);

        if (expression != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            return expression.getValue(ctx.getELContext());
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
		castValue(propertyName, value);
    }   
    
    protected void castValue(String propertyName, Object value) {

    }
	public interface Iterator {
		public void next (String name, EffectBehavior effectBehavior);
	}
}

//faces-config.xml
//<behavior>
//<behavior-id>org.icefaces.effects.Effect</behavior-id>
// <behavior-class>
//  org.icefaces.component.effects.EffectBehavior
// </behavior-class>
//</behavior>

//taglib.xml
//<tag>
//	<tag-name>effect</tag-name>
//		<behavior>
//			<behavior-id>org.icefaces.effects.Effect</behavior-id>
//	</behavior>
// </tag>