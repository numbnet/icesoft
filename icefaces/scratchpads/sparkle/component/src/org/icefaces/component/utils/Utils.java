package org.icefaces.component.utils;

import java.beans.Beans;
import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
 
import org.icefaces.component.effects.EffectBehavior;

public class Utils {
    public static void renderChildren(FacesContext facesContext,
                                      UIComponent component)
            throws IOException {
        if (component.getChildCount() > 0) {
            for (Iterator it = component.getChildren().iterator();
                 it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                renderChild(facesContext, child);
            }
        }
    }
    
    
    public static void renderChild(FacesContext facesContext, UIComponent child)
            throws IOException {
        if (!child.isRendered()) {
            return;
        }
    
        child.encodeBegin(facesContext);
        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }

    public static UIComponent findNamingContainer(UIComponent uiComponent) {
        UIComponent parent = uiComponent.getParent();
        while (parent != null) {
            if (parent instanceof NamingContainer) {
                break;
            }
            parent = parent.getParent();
        }
        return parent;
    }


    public static UIComponent findForm(UIComponent uiComponent) {
        UIComponent parent = uiComponent.getParent();
        while (parent != null && !(parent instanceof UIForm)) {
            parent = findNamingContainer(parent);
        }
        return parent;
    }
    
    public static void decodeBehavior(FacesContext facesContext, UIComponent uiComponent) {
    	
    }
    
    
    public static boolean iterateEffects(EffectBehavior.Iterator iterator) {
    	if (!(iterator.getUIComponent() instanceof ClientBehaviorHolder)) return false;
    	for (String effect : ((ClientBehaviorHolder)iterator.getUIComponent()).getClientBehaviors().keySet()) {
    		for (ClientBehavior behavior: ((ClientBehaviorHolder)iterator.getUIComponent()).getClientBehaviors().get(effect)) {
    			if (behavior instanceof EffectBehavior) {
    				iterator.next(effect, (EffectBehavior)behavior);		
    			}
    		}
    	}
    	return true;
    }
}
