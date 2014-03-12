/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobi.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.icemobile.util.Utils;

public class JSFUtils {

    private static Logger logger = Logger.getLogger(JSFUtils.class.getName());
    
    /**
     * Find a UIComponent. If the contextComponent is provided, the search will be performed in the 
     * containing NamingContainer. If the target component is not found, the search will then be performed
     * again from the root. If the contextComponent is not provided, the search will be performed from the root.
     * @param id The id of the component to find
     * @param contextComponent The context of the search, if null, the search will run from the UIViewRoot
     * @return The target component
     */
    public static UIComponent findComponent(String id, UIComponent contextComponent){
        if( id == null || id.length() == 0 ){
            logger.warning("invalid argument, id is empty");
            return null;
        }
        UIComponent namingContainer = null;
        if( contextComponent == null ){
            namingContainer = FacesContext.getCurrentInstance().getViewRoot();
        }
        else{
            namingContainer = findNearestNamingContainerOrRoot(contextComponent);
        }
        UIComponent target = namingContainer.findComponent(id);
        FacesContext fc = FacesContext.getCurrentInstance();
        if( target == null && id.indexOf(UINamingContainer.getSeparatorChar(fc)) == -1){
            return findComponentById(id, namingContainer);
        }
        return target;
    }
    
    private static UIComponent findComponentById(String id, UIComponent parent){
        String compId = parent.getId();
        if( compId == null ){
            return null;
        }
        if (parent.getId().equals(id)) {
            return parent;
        }
        UIComponent component = null;
        UIComponent child = null;
        Iterator<UIComponent> children = parent.getFacetsAndChildren();
        while (children.hasNext() && (component == null)) {
            child = (UIComponent) children.next();
            component = findComponentById(id, child);
            if (component != null) {
                break;
            }
        }
        return component;
    }

    public static UIComponent findChildComponent(UIComponent parent, String id) {
        if (id.equals(parent.getId())) {
            return parent;
        }
        UIComponent child = null;
        UIComponent retComp = null;
        Iterator<UIComponent> children = parent.getFacetsAndChildren();
        while (children.hasNext() && (retComp == null)) {
            child = (UIComponent) children.next();
            if (id.equals(child.getId())) {
                retComp = child;
                break;
            }
            retComp = findChildComponent(child, id);
            if (retComp != null) {
                break;
            }
        }
        return retComp;
    }

    public static UIComponent getChildById(UIComponent parent, String id) {
        if (parent.getChildCount() > 0) {
            for (UIComponent child : parent.getChildren()) {
                if (child.getId().equals(id)) {
                    return child;
                }
            }
        }
        return null;
    }

    public static String getIdOfChildByClientId(FacesContext context,
            UIComponent parent, String clientId) {
        if (parent.getChildCount() > 0) {
            for (UIComponent child : parent.getChildren()) {
                if (child.getClientId(context).equals(clientId)) {
                    return child.getId();
                }
            }
        }
        return null;
    }

    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
            String[] componentClasses, String applicationClass, boolean disabled)
            throws IOException {
        final String disabledStr = "-disabled";
        int componentCount = (componentClasses == null ? 0
                : componentClasses.length);
        StringTokenizer st = new StringTokenizer(applicationClass, " ");
        int applicationCount = st.countTokens();

        if (componentCount == 1 && applicationCount == 0) {
            if (disabled) {
                String styleClass = (componentClasses[0].trim() + disabledStr)
                        .intern();
                writer.writeAttribute("class", styleClass, "styleClass");
            } else {
                writer.writeAttribute("class", componentClasses[0],
                        "styleClass");
            }
        } else if (componentCount == 0 && applicationCount == 1 && !disabled) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        } else if (componentCount > 0 || applicationCount > 0) {
            StringBuilder sb = new StringBuilder(
                    (componentCount + applicationCount) * 16);
            for (int i = 0; i < componentCount; i++) {
                Utils.concatenateStyleClass(sb, componentClasses[i], disabled,
                        disabledStr);
            }
            while (st.hasMoreTokens()) {
                Utils.concatenateStyleClass(sb, st.nextToken(), disabled,
                        disabledStr);
            }
            sb.trimToSize();
            writer.writeAttribute("class", sb.toString(), "styleClass");
        }
    }

    public static void writeConcatenatedStyleClasses(ResponseWriter writer,
            String componentClass, String applicationClass) throws IOException {
        int componentLen = (componentClass == null) ? 0
                : (componentClass = componentClass.trim()).length();
        int applicationLen = (applicationClass == null) ? 0
                : (applicationClass = applicationClass.trim()).length();
        if (componentLen > 0 && applicationLen == 0) {
            writer.writeAttribute("class", componentClass, "styleClass");
        } else if (componentLen == 0 && applicationLen > 0) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        } else if (componentLen > 0 || applicationLen > 0) {
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
        int componentLen = (componentClass == null) ? 0
                : (componentClass = componentClass.trim()).length();
        int applicationLen = (applicationClass == null) ? 0
                : (applicationClass = applicationClass.trim()).length();
        if (componentLen > 0 && applicationLen == 0) {
            if (disabled) {
                String styleClass = (componentClass + disabledStr).intern();
                writer.writeAttribute("class", styleClass, "styleClass");
            } else {
                writer.writeAttribute("class", componentClass, "styleClass");
            }
        } else if (componentLen == 0 && applicationLen > 0 && !disabled) {
            writer.writeAttribute("class", applicationClass, "styleClass");
        } else if (componentLen > 0 || applicationLen > 0) {
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

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    public static void renderChildren(FacesContext facesContext,
            UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
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

    /**
     * used by PagePanelRenderer to ensure no problems with push
     * @param facesContext
     * @param child
     * @throws IOException
     */
    public static void renderLayoutChild(FacesContext facesContext, UIComponent child)
            throws IOException {
        if (!child.isRendered()) {
            return;
        }
        child.encodeAll(facesContext);
    }
    public static UIComponent findNearestNamingContainerOrRoot(UIComponent uiComponent) {
        UIComponent namingContainer = uiComponent;
        while( !(namingContainer instanceof NamingContainer) && namingContainer != null
                && !(namingContainer instanceof UIViewRoot)){
            namingContainer = namingContainer.getParent();
        }
        return namingContainer;
    }

    public static UIComponent findParentForm(UIComponent component) {
        UIComponent parent = component;
        while (parent != null)
            if (parent instanceof UIForm)
                break;
            else
                parent = parent.getParent();

        return parent;
    }

    public static String findClientIds(FacesContext context,
            UIComponent component, String list) {
        if (list == null)
            return "@none";
        String[] ids = list.split("[,\\s]+");
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < ids.length; i++) {
            if (i != 0)
                buffer.append(" ");
            String id = ids[i].trim();
            // System.out.println("ComponentUtils.findClientIds()    ["+i+"]  id: "
            // + id);
            if (id.equals("@all") || id.equals("@none")) {
                // System.out.println("ComponentUtils.findClientIds()    ["+i+"]  "
                // + id);
                buffer.append(id);
            } else if (id.equals("@this")) {
                // System.out.println("ComponentUtils.findClientIds()    ["+i+"]  @this  : "
                // + component.getClientId(context));
                buffer.append(component.getClientId(context));
            } else if (id.equals("@parent")) {
                // System.out.println("ComponentUtils.findClientIds()    ["+i+"]  @parent: "
                // + component.getParent().getClientId(context));
                buffer.append(component.getParent().getClientId(context));
            } else if (id.equals("@form")) {
                UIComponent form = findParentForm(component);
                if (form == null)
                    throw new FacesException("Component "
                            + component.getClientId(context)
                            + " needs to be enclosed in a form");
                buffer.append(form.getClientId(context));
            } else {
                UIComponent comp = component.findComponent(id);
                // System.out.println("ComponentUtils.findClientIds()    ["+i+"]  comp   : "
                // + (comp == null ? "null" : comp.getClientId(context)));
                if(comp == null){
                    String encodedId = encodeNameSpace(context,id);
                    if( !encodedId.startsWith(":")){
                        encodedId = ":" + encodedId;
                    }
                    comp = component.findComponent(encodedId);
//System.out.println("ComponentUtils.findClientIds()   ["+i+"]  comp   : " + (comp == null ? "null" : comp.getClientId(context)) + "  id: " + encodedId);
                }

                if (comp != null) {
                    buffer.append(comp.getClientId(context));
                }
                else {
                    if (context.getApplication().getProjectStage().equals(ProjectStage.Development)) {
                        logger.log(Level.INFO, "Cannot find component with identifier \"{0}\" in view.", id);
                    }
                    buffer.append(id);
                }
            }
        }
        return buffer.toString();
    }
    /**
     * Environments like portlets need to namespace the components in order to uniquely identify them
     * on the page in case there are multiple instances of the same portlet or different portlets that use
     * the same ids.  This method will prepend the namespace properly taking care to ensure that the
     * namespace is not added twice and that a colon (:) is added if necessary.
     *
     * @param fc The current FacesContext instance
     * @param id The id to encode
     * @return The namespace encoded id
     */
    public static String encodeNameSpace(FacesContext fc, String id){

        if( id == null || id.trim().length() == 0){
            return id;
        }

        String tempId = id;
        ExternalContext ec = fc.getExternalContext();
        String encodedId = ec.encodeNamespace(tempId);

        //If no namespace was applied, we're done.
        if( encodedId.equals(id)){
            return id;
        }

        //Extract the actual namespace.
        int idStart = encodedId.indexOf(id);
        String ns = encodedId.substring(0,idStart);

        //Check if the id already had the namespace.  If so, we're done.
        if( id.startsWith(ns) ){
            return id;
        }

        //If necessary, add the separator character before including the namespace.
        String sep = String.valueOf(UINamingContainer.getSeparatorChar(fc));
        if( !id.startsWith(sep)){
            id = ":" + id;
        }

        //Add the namespace.
        id = ns + id;
        return id;
    }
    /**
     * Capture UIParameter (f:param) children of a component used by
     * commandButton for f:param support
     * 
     * @param component
     *            The component to work from
     * @return List of UIParameter objects, null if no UIParameter children
     *         present
     */
    public static List<UIParameter> captureParameters(UIComponent component) {
        List<UIComponent> children = component.getChildren();
        List<UIParameter> returnVal = null;
        for (UIComponent child : children) {
            if (child instanceof UIParameter) {
                UIParameter param = (UIParameter) child;
                if (returnVal == null) {
                    returnVal = new ArrayList<UIParameter>();
                }
                returnVal.add(param);
            }
        }
        return returnVal;
    }

}
