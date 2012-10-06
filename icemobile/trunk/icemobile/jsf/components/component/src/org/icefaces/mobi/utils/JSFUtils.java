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
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.icemobile.util.Utils;

public class JSFUtils {

    private static Logger logger = Logger.getLogger(JSFUtils.class.getName());

    public static UIComponent findChildComponent(UIComponent parent, String id) {
        if (id.equals(parent.getId())) {
            return parent;
        }
        UIComponent child = null;
        UIComponent retComp = null;
        Iterator children = parent.getFacetsAndChildren();
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

    /**
     * Copied over from ace ComponentUtils for ajax support
     * 
     * @param component
     * @return
     */
    public static UIComponent findParentForm(UIComponent component) {
        UIComponent parent = component;
        while (parent != null)
            if (parent instanceof UIForm)
                break;
            else
                parent = parent.getParent();

        return parent;
    }

    /**
     * taken from ace Util.ComponentUtils for ajax tag support Nov 20, 2011
     * 
     * @param context
     * @param component
     * @param list
     * @return
     */
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
                if (comp != null) {
                    buffer.append(comp.getClientId(context));
                } else {
                    if (context.getApplication().getProjectStage()
                            .equals(ProjectStage.Development)) {
                        logger.log(
                                Level.INFO,
                                "Cannot find component with identifier \"{0}\" in view.",
                                id);
                    }
                    buffer.append(id);
                }
            }
        }
        return buffer.toString();
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
