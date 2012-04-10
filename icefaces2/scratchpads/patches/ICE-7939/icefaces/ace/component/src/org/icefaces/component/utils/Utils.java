/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.component.utils;

import java.beans.Beans;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.net.URLEncoder;

import javax.el.ValueExpression;
import javax.faces.component.*;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.component.animation.AnimationBehavior;
import org.icefaces.impl.util.DOMUtils;

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


    public static UIForm findParentForm(UIComponent comp) {
        if (comp == null) {
            return null;
        }
        if (comp instanceof UIForm) {
            return (UIForm) comp;
        }
        return findParentForm(comp.getParent());
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

    /**
     * Capture UIParameter (f:param) children of a component
     * @param component The component to work from
     * @return List of UIParameter objects, null if no UIParameter children present
     */
    public static List<UIParameter> captureParameters( UIComponent component) {
        List<UIComponent> children = component.getChildren();
        List<UIParameter>  returnVal = null;
        for (UIComponent child: children) {
            if (child instanceof UIParameter) {
                UIParameter param = (UIParameter) child;
                if (returnVal == null) {
                    returnVal = new ArrayList<UIParameter>();
                }
                returnVal.add( param );
            }
        }
        return returnVal;
    }

    /**
     * Return the name value pairs parameters as a ANSI escaped string
     * formatted in query string parameter format.
     * TODO: determine the correct escaping here
     * @param children List of children
     * @return a String in the form name1=value1&name2=value2...
     */
    public static String asParameterString ( List<UIParameter> children) {
        StringBuffer builder = new StringBuffer();
        for (UIParameter param: children) {
            builder.append(DOMUtils.escapeAnsi(param.getName()) )
                    .append("=").append(DOMUtils.escapeAnsi(
                    (String)param.getValue() ).replace(' ', '+')).append("&");
        }
        if (builder.length() > 0) {
            builder.setLength( builder.length() - 1 );
        }
        return builder.toString();
    }


    /**
     * Return the name value pairs parameters as a comma separated list. This is
     * simpler for passing to the javascript parameter rebuilding code.
     * @param children List of children
     * @return a String in the form name1, value1, name2, value2...
     */
    public static String[] asStringArray( List<UIParameter> children) {
        ArrayList builder = new ArrayList();
        for (UIParameter param: children) {
            builder.add(param.getName());
            builder.add(param.getValue().toString());
        }
        Object[] returnVal = new String[ builder.size() ];
        builder.toArray (returnVal);
        return (String[]) returnVal;
    }

    public static boolean superValueIfSet(UIComponent component, StateHelper sh, String attName, boolean superValue, boolean defaultValue) {
        ValueExpression ve = component.getValueExpression(attName);
        if (ve != null) {
            return superValue;
        }
        String valuesKey = attName + "_rowValues";
        Map clientValues = (Map) sh.get(valuesKey);
        if (clientValues != null) {
            String clientId = component.getClientId();
            if (clientValues.containsKey(clientId)) {
                return superValue;
            }
        }
        String defaultKey = attName + "_defaultValues";
        Map defaultValues = (Map) sh.get(defaultKey);
        if (defaultValues != null) {
            if (defaultValues.containsKey("defValue")) {
                return superValue;
            }
        }
        return defaultValue;
    }
}
