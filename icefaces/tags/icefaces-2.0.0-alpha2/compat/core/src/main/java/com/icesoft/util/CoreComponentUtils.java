package com.icesoft.util;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 */
public class CoreComponentUtils {
    private static final Pattern ClientIdPattern = Pattern.compile("^(([\\w\\_]*)\\" + NamingContainer.SEPARATOR_CHAR + "([\\w\\_]*))*$");

    /**
     * Find a component with an id specified in the following ways <code>
     * <p/>
     * <ul><li>
     * :componetId - Absolute id, search starts at UIViewRoot </li><li>
     * componentId - Relative id, search starts at nearest parent namingContainer or ViewRoot</li><li>
     * :xN componentId - Numbered relative id, search starts at N'th parent naming container or ViewRoot</li></ul>
     * </code>
     *
     * @param clientId id of component
     * @param base     UIComponent base to start searching from
     */
    public static UIComponent findComponent(String clientId, UIComponent base) {
//System.out.println("    findComponent()  clientId: " + clientId + "  base: " + base);
        // Set base, the parent component whose children are searched, to be the
        // nearest parent that is either 1) the view root if the id expression
        // is absolute (i.e. starts with the delimiter) or 2) the nearest parent
        // NamingContainer if the expression is relative (doesn't start with
        // the delimiter)
        String delimeter = String.valueOf(NamingContainer.SEPARATOR_CHAR);
        int count = getNumberOfLeadingNamingContainerSeparators(clientId);
//System.out.println("      count: " + count);
        if (count == 1) {
            // Absolute searches start at the root of the tree
            while (base.getParent() != null) {
                base = base.getParent();
            }
            // Treat remainder of the expression as relative
            clientId = clientId.substring(delimeter.length());
        } else if (count == 0) {
            // Relative expressions start at the closest NamingContainer or root
            while (base.getParent() != null) {
                if (base instanceof NamingContainer) {
                    break;
                }
                base = base.getParent();
            }
        } else if (count > 1) {
            // Relative expressions start at the closest NamingContainer or root
            int numNamingContainersUp = count - 1;
//System.out.println("      numNamingContainersUp: " + numNamingContainersUp);
            while (base.getParent() != null) {
                if (base instanceof NamingContainer) {
                    numNamingContainersUp--;
//System.out.println("      NamingContainer["+numNamingContainersUp+"]: " + base);
                    if (numNamingContainersUp == 0)
                        break;
                }
                base = base.getParent();
            }
            clientId = clientId.substring(delimeter.length() * count);
//System.out.println("      clientId: " + clientId);
        }
        // Evaluate the search expression (now guaranteed to be relative)
        String id = null;
        UIComponent result = null;
        while (clientId.length() > 0) {
            int separator = clientId.indexOf(NamingContainer.SEPARATOR_CHAR);
            if (base instanceof UIData) {
                if (separator >= 0) {
                    clientId = clientId.substring(separator + 1);
                }
                separator = clientId.indexOf(NamingContainer.SEPARATOR_CHAR);
            }
            if (separator >= 0) {
                id = clientId.substring(0, separator);
                clientId = clientId.substring(separator + 1);
            } else {
                id = clientId;
                clientId = "";
            }
            result = findComponent(base, id);
            if ((result == null) || (clientId.length() == 0)) {
                break; // Missing intermediate node or this is the last node
            }
            if (result instanceof NamingContainer) {
                result = findComponent(clientId, result);
                break;
            }
        }

        return result;
    }

    private static int getNumberOfLeadingNamingContainerSeparators(
            String clientId) {
        int count = 0;
        String delimeter = String.valueOf(NamingContainer.SEPARATOR_CHAR);
        for (int index = 0; clientId.indexOf(delimeter, index) == index; index += delimeter.length())
            count++;
        return count;
    }


    /**
     * Find a component with a given id, given a starting component
     *
     * @param uiComponent
     * @param componentId
     * @return
     */
    private static UIComponent findComponent(UIComponent uiComponent,
                                             String componentId) {
        UIComponent component = null;
        UIComponent child = null;

        if (componentId.equals(uiComponent.getId())) {
            return uiComponent;
        }
        Iterator children = uiComponent.getFacetsAndChildren();
        while (children.hasNext() && (component == null)) {
            child = (UIComponent) children.next();
            if (!(child instanceof NamingContainer)) {
                component = findComponent(child, componentId);
                if (component != null) {
                    break;
                }
            } else if (componentId.endsWith(child.getId())) {
                component = child;
                break;
            }
        }
        return component;
    }

    /**
     * A version of findComponent() that attempts to locate a component by id (not clientId)
     * and searches into NamingContainers. If there are more than one component with the
     * provided id, the first one found will be returned
     *
     * @param uiComponent the base component to search from
     * @param componentId the id to search for
     */

    public static UIComponent findComponentInView(UIComponent uiComponent,
                                                  String componentId) {
        UIComponent component = null;
        UIComponent child = null;

        if (componentId.equals(uiComponent.getId())) {
            return uiComponent;
        }
        Iterator children = uiComponent.getFacetsAndChildren();
        while (children.hasNext() && (component == null)) {
            child = (UIComponent) children.next();
            component = findComponentInView(child, componentId);
            if (component != null) {
                break;
            }
            if (componentId.endsWith(child.getId())) {
                component = child;
                break;
            }
        }
        return component;
    }

    /**
     * ICE-4417 Migrate method for setting focus id.
     *
     * @param focusId Id of component to get the focus
     */
    public static void setFocusId(String focusId) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ice.focus", focusId);
    }

    /**
     * Return the id of the Element that currently has focus in the browser.
     *
     * @return String
     */
    public static String getFocusId(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map map = externalContext.getRequestParameterMap();
        String focusedElement = (String) map.get("ice.focus");
        return focusedElement != null && ClientIdPattern.matcher(focusedElement).matches() ? focusedElement : "";
    }
}
