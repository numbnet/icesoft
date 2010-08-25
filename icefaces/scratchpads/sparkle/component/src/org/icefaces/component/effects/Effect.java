package org.icefaces.component.effects;

import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

@FacesBehavior("org.icefaces.effects.Effect")
public class Effect extends EffectBehavior{
 
	EffectBehavior effect = new Fade();
	private String using = "Fade";

	public void setUsing(String using) {
		if (!using.equals(this.using)) {
			use(using);
		}
		this.using = using;
        clearInitialState();
    }
    
    public String getUsing() {
        return (String) eval("using", using);

    }
    
    protected void castValue(String propertyName, Object value) {
        if ("using".equals(propertyName)) {
			using = (String)value;
		}
    }
    
    public String getName() {
    	return effect.getClass().getSimpleName();
    }
	
	public String getScript(ClientBehaviorContext behaviorContext) {
    	 return effect.getScript(behaviorContext);
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

	
}
