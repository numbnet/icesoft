package org.icefaces.component.animation;

import java.io.IOException;
import java.util.Collection;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;


public class AnimationBehaviorHandler extends BehaviorHandler{
    private final TagAttribute run;
    private final TagAttribute name;
    private final TagAttribute to;
    private final TagAttribute from;
    private final TagAttribute easing;
    private final TagAttribute iterations;
    private final TagAttribute duration;    
    private final TagAttribute effectObject;
    
 	public AnimationBehaviorHandler(BehaviorConfig config) {
		super(config);
		run = this.getAttribute("run");
		name = this.getAttribute("name");
		to = this.getAttribute("to");
		from = this.getAttribute("from");
		easing = this.getAttribute("easing");
		iterations = this.getAttribute("iterations");
		effectObject = this.getAttribute("effectObject");
		duration = this.getAttribute("duration");
	}
 
 
    private void setAttribute(FaceletContext context, 
            			TagAttribute tagAttribute, 
            				AnimationBehavior behavior,  
            					Class type) {

    	if (tagAttribute != null) {
    		behavior.setValueExpression(tagAttribute.getLocalName(),
    					tagAttribute.getValueExpression(context, type));
    	}    
    }
    

    public void apply(FaceletContext context, UIComponent parent)
    throws IOException {
    
        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        if (!(parent instanceof ClientBehaviorHolder)) {
               throw new TagException(this.tag,
                                           "Effect behavior can not be attach to non clientBehaviorHolder parent");
		} 
        ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder)parent;
        String eventName = getEventName();
        if (eventName == null) {
        	eventName = clientBehaviorHolder.getDefaultEventName();
        	if (eventName == null)  {
                throw new TagException(this.tag,
                        "Event attribute could not be determined " + eventName);        		
        	}
        }else {
            Collection<String> eventNames = clientBehaviorHolder.getEventNames();
            if (!eventNames.contains(eventName)) {
                throw new TagException(this.tag,"Event is not supported by this component "+ clientBehaviorHolder.getClass().getSimpleName());
            }
        }
        
        AnimationBehavior effectBehavior = createEffectBehavior(context, eventName);
        clientBehaviorHolder.addClientBehavior(eventName, effectBehavior);
    }
    
    private AnimationBehavior createEffectBehavior(FaceletContext context, String eventName) {
    	
    	Application application = context.getFacesContext().getApplication();
    	AnimationBehavior effectBehavior = (AnimationBehavior)application.createBehavior(AnimationBehavior.BEHAVIOR_ID);
    	setAttribute(context, run, effectBehavior, Boolean.class);
    	setAttribute(context, effectObject, effectBehavior, Effect.class);   
    	setAttribute(context, name, effectBehavior, String.class);      	
    	setAttribute(context, to, effectBehavior, String.class);
    	setAttribute(context, from, effectBehavior, String.class);   
    	setAttribute(context, easing, effectBehavior, String.class);        	
    	setAttribute(context, iterations, effectBehavior, Integer.class);
    	setAttribute(context, duration, effectBehavior, Double.class);
    	return effectBehavior;
    }
 

 
}
