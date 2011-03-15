package org.icefaces.training.applicant.view.util;


import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * JSF utilities.
 */
public class FacesUtils {

    private static Properties buildProperties = null;

    /**
     * Get servlet context.
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getExternalContext();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().
                getExternalContext().getSession(create);
    }

    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(elc, getJsfEl(beanName), Object.class);
        return ve.getValue(elc);
    }


    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */
    public static void resetManagedBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
        ef.createValueExpression(elc, getJsfEl(beanName),
                Object.class).setValue(elc, null);
    }

    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName    the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
    }

    /**
     * Store the managed bean inside the request scope.
     *
     * @param beanName    the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInRequest(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(beanName, managedBean);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Get named request map object value from request map.
     *
     * @param name the name of the key in map
     * @return the key value if any, null otherwise.
     */
    public static Object getRequestMapValue(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
    }

    /**
     * Gets the action attribute value from the specified event for the given
     * name.  Action attributes are specified by &lt;f:attribute /&gt;.
     *
     * @param event
     * @param name
     * @return
     */
    public static String getActionAttribute(ActionEvent event, String name) {
        return (String) event.getComponent().getAttributes().get(name);
    }

    public static String getBuildAttribute(String name) {
        if (buildProperties != null)
            return buildProperties.getProperty(name, "unknown");
        InputStream is = null;
        try {
            is = getServletContext().getResourceAsStream("/WEB-INF/buildversion.properties");
            buildProperties = new java.util.Properties();
            buildProperties.load(is);
        } catch (Throwable e) {
            is = null;
            buildProperties = null;
            return "unknown";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable t) {
                }
            }
        }
        return buildProperties.getProperty(name, "unknown");
    }

    /**
     * Get parameter value from the web.xml file
     *
     * @param parameter name to look up
     * @return the value of the parameter
     */
    public static String getFacesParameter(String parameter) {
        // Get the servlet context based on the faces context
        ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        // Return the value read from the parameter
        return sc.getInitParameter(parameter);
    }

    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the information message
     */
    public static void addWarnMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }

    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    /**
     * Add error message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }

    /**
     * Finds component with the given id
     *
     * @param c  component check children of.
     * @param id id of component to search for.
     * @return found component if any.
     */
    public static UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}