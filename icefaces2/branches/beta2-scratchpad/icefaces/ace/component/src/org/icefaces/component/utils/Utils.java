package org.icefaces.component.utils;

import java.beans.Beans;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.component.animation.AnimationBehavior;

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
    
    
    public static boolean iterateEffects(AnimationBehavior.Iterator iterator) {
    	if (!(iterator.getUIComponent() instanceof ClientBehaviorHolder)) return false;
    	for (String effect : ((ClientBehaviorHolder)iterator.getUIComponent()).getClientBehaviors().keySet()) {
    		for (ClientBehavior behavior: ((ClientBehaviorHolder)iterator.getUIComponent()).getClientBehaviors().get(effect)) {
    			if (behavior instanceof AnimationBehavior) {
    				iterator.next(effect, (AnimationBehavior)behavior);		
    			}
    		}
    	}
    	return true;
    }
    
    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
            String componentClass, String applicationClass)
            throws IOException {
        int componentLen = (componentClass == null) ? 0 :
            (componentClass = componentClass.trim()).length();
        int applicationLen = (applicationClass == null) ? 0 :
            (applicationClass = applicationClass.trim()).length();
        if (componentLen > 0 && applicationLen == 0) {
            writer.writeAttribute("class", componentClass, "styleClass");
        }
        else if (componentLen == 0 && applicationLen > 0) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        }
        else if (componentLen > 0 || applicationLen > 0) {
            int totalLen = componentLen + applicationLen;
            if (componentLen > 0 && applicationLen > 0) {
                totalLen++;
            }
            
            StringBuilder sb = new StringBuilder(totalLen);
            if (componentLen > 0) {
                sb.append(componentClass);
            }
            if (applicationLen > 0) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(applicationClass);
            }
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }
    
    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
            String componentClass, String applicationClass, boolean disabled)
            throws IOException {
        final String disabledStr = "-disabled";
        int componentLen = (componentClass == null) ? 0 :
            (componentClass = componentClass.trim()).length();
        int applicationLen = (applicationClass == null) ? 0 :
            (applicationClass = applicationClass.trim()).length();
        if (componentLen > 0 && applicationLen == 0) {
            if (disabled) {
                String styleClass = (componentClass + disabledStr).intern();
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            else {
                writer.writeAttribute("class", componentClass, "styleClass");
            }
        }
        else if (componentLen == 0 && applicationLen > 0 && !disabled) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        }
        else if (componentLen > 0 || applicationLen > 0) {
            int totalLen = componentLen + applicationLen;
            if (disabled && componentLen > 0) {
                totalLen += disabledStr.length();
            }
            if (disabled && applicationLen > 0) {
                totalLen += disabledStr.length();
            }
            if (componentLen > 0 && applicationLen > 0) {
                totalLen++;
            }
            
            StringBuilder sb = new StringBuilder(totalLen);
            if (componentLen > 0) {
                sb.append(componentClass);
                if (disabled) {
                    sb.append(disabledStr);
                }
            }
            if (applicationLen > 0) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(applicationClass);
                if (disabled) {
                    sb.append(disabledStr);
                }
            }
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }
    
    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
            String[] componentClasses, String applicationClass, boolean disabled)
            throws IOException {
        final String disabledStr = "-disabled";
        int componentCount = (componentClasses == null ? 0 :
            componentClasses.length);
        StringTokenizer st = new StringTokenizer(applicationClass, " ");
        int applicationCount = st.countTokens();
        
        if (componentCount == 1 && applicationCount == 0) {
            if (disabled) {
                String styleClass =
                    (componentClasses[0].trim() + disabledStr).intern();
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            else {
                writer.writeAttribute("class", componentClasses[0], "styleClass");
            }
        }
        else if (componentCount == 0 && applicationCount == 1 && !disabled) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        }
        else if (componentCount > 0 || applicationCount > 0) {
            StringBuilder sb = new StringBuilder(
                (componentCount + applicationCount) * 16 );
            for (int i = 0; i < componentCount; i++) {
                concatenateStyleClass(sb, componentClasses[i], disabled,
                    disabledStr);
            }
            while (st.hasMoreTokens()) {
                concatenateStyleClass(sb, st.nextToken(), disabled,
                    disabledStr);
            }
            sb.trimToSize();
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }
    
    private static void concatenateStyleClass(StringBuilder sb,
            String styleClass, boolean disabled, String disabledStr) {
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(styleClass);
        if (disabled) {
            sb.append(' ');
            sb.append(styleClass);
            sb.append(disabledStr);
        }
    }
}
