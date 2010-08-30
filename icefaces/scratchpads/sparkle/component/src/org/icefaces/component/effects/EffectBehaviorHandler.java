package org.icefaces.component.effects;

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


public class EffectBehaviorHandler extends BehaviorHandler{
    private final TagAttribute run;
    private final TagAttribute name;
    private final TagAttribute effectObject;
    
 	public EffectBehaviorHandler(BehaviorConfig config) {
		super(config);
		run = this.getAttribute("run");
		name = this.getAttribute("name");
		effectObject = this.getAttribute("effectObject");
		System.out.println("EffectBehaviorHandler createdddd");
		

	}
 
 
    private void setAttribute(FaceletContext context, 
            			TagAttribute tagAttribute, 
            				EffectBehavior behavior,  
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
        
        EffectBehavior effectBehavior = createEffectBehavior(context, eventName);
        clientBehaviorHolder.addClientBehavior(eventName, effectBehavior);
    }
    
    private EffectBehavior createEffectBehavior(FaceletContext context, String eventName) {
    	
    	Application application = context.getFacesContext().getApplication();
    	EffectBehavior effectBehavior = (EffectBehavior)application.createBehavior(EffectBehavior.BEHAVIOR_ID);
    	setAttribute(context, run, effectBehavior, Boolean.class);
    	setAttribute(context, effectObject, effectBehavior, Effect.class);   
    	setAttribute(context, name, effectBehavior, String.class);      	
    	
    	
    	return effectBehavior;
    }
 

 
}
