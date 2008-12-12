/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail;

import com.icesoft.applications.faces.webmail.util.db.HibernateUtil;
import com.icesoft.applications.faces.webmail.contacts.ContactManager;
import com.icesoft.applications.faces.webmail.login.LoginBean;
import com.icesoft.applications.faces.webmail.login.LoginManager;
import com.icesoft.applications.faces.webmail.mail.MailManager;
import com.icesoft.applications.faces.webmail.mail.MailManager.MailManagersOperationRunner;
import com.icesoft.applications.faces.webmail.mail.MailAccountTimerCache;
import com.icesoft.applications.faces.webmail.navigation.NavigationManager;
import com.icesoft.applications.faces.webmail.tasks.TasksManager;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.*;

/**
 * <p>The WebmailMediator object is responsible for mediating the WebmailBase
 * applications.</p>
 *
 * @since 1.0
 */
public class WebmailMediator implements
        HttpSessionListener, ServletContextListener, Renderable {
    
    private static Log log = LogFactory.getLog(WebmailMediator.class);
    
    private LoginManager loginManager;
    
    // manges contact realated activities
    private ContactManager contactManager;
    
    private TasksManager tasksManager;
    
    private MailManager mailManager;
    
    private NavigationManager navigationManager;
    
    private static RenderManager renderManager;
    
    // On demand render group for the mail API, not currently used but
    // will be used in the future for an global address book
    public static final String ON_DEMAND_RENDERER = "webmailContactGroupRender";
    
    public static final String SESSION_DESTROYED = "sessionDestroyed";
    
    private static ResourceBundle messageBundle = null;
    
    // local instance of the persistent faces state, need by the render api
    // this var must be thread local.
    private PersistentFacesState persistentFacesState;
    
    // Timer used to que up all timerTasks related to tickling mails accounts
    // why is it here an not in MailManager,  to insure that
    private static Timer javaMailTickleTimer;
    
    
    
    /**
     * Creates a new instance of a WebmailMediator.
     */
    public WebmailMediator() {
        // to avoid class loading delays we construct the factory now
        // to avoid unesssary wait on the login screen.
        HibernateUtil.getSessionFactory();
        // get a local thread instance of the persistent Faces State.
        persistentFacesState = PersistentFacesState.getInstance();
        // setup timer
        if (javaMailTickleTimer == null){
            javaMailTickleTimer = new Timer();
        }
    }
    
    
    public String shutdownWebmail() {
        
        if (log.isDebugEnabled()) {
            log.debug("Started Shutting down Webmail session ");
        }
        
        if (loginManager != null) {
            loginManager.dispose();
        }
        
        if (contactManager != null) {
            contactManager.dispose();
        }
        
        if (tasksManager != null) {
            tasksManager.dispose();
        }
        
        if (navigationManager != null) {
            navigationManager.dispose();
        }
        
        if (mailManager != null) {
            mailManager.dispose();
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Finished Shutting down Webmail session ");
        }
        
        return LoginBean.LOGOUT_SUCCESS;
    }
    
    /**
     * Sets the contact manager callback.
     *
     * @param contactManager contact manager associated with this object.
     */
    public void setContactManager(ContactManager contactManager) {
        this.contactManager = contactManager;
    }
    
    /**
     * Gets the contact manager.
     *
     * @return contact manager.
     */
    public ContactManager getContactManager() {
        return contactManager;
    }
    
    /**
     * Gets the tasks manager.
     *
     * @return tasks manager
     */
    public TasksManager getTasksManager() {
        return tasksManager;
    }
    
    /**
     * Sets the tasks Manager
     *
     * @param tasksManager task manager to be used by mediator
     */
    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }
    
    public LoginManager getLoginManager() {
        return loginManager;
    }
    
    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }
    
    /**
     * Gets the JavaMail tickleTimer which is used to tickle the mail api.
     * @return  the mail tickle timer
     */
    public Timer getJavaMailTickleTimer() {
        return javaMailTickleTimer;
    }
    
    /**
     * Gets the mail manager.
     *
     * @return mail manager
     */
    public MailManager getMailManager() {
        return mailManager;
    }
    
    /**
     * Sets the mail manager.
     *
     * @param mailManager mail manager to be used by mediator
     */
    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }
    
    /**
     * Gets the navigation manager.
     *
     * @return navigation manager
     */
    public NavigationManager getNavigationManager() {
        return navigationManager;
    }
    
    /**
     * Sets the navigation manager.
     *
     * @param navigationManager mavigation manager to be used by mediator
     */
    public void setNavigationManager(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }
    
    public void sheduleTickelTimer(TimerTask timer, final int frequency){
        if (javaMailTickleTimer != null){
            javaMailTickleTimer.schedule(
                    timer,
                    frequency,
                    frequency);
            // add a pointer to the timer task to the cache for clean up
            // purposes.
            HttpSession httpSession = null;
            
            FacesContext facesContext =  FacesContext.getCurrentInstance();
            
            // make sure we have a valid facesContext
            if (facesContext != null){
                httpSession =
                        (HttpSession) facesContext.getExternalContext()
                        .getSession(false);
            }
            
            // finally make sure the session is not null before trying to set the
            // login
            if (httpSession != null){
                MailAccountTimerCache.cacheMailAccountTimer(httpSession, timer );
            }
            
        }
    }
    
    /**
     * Sets the render manager. The RenderManager should be declared in Applicaiton
     * space.
     *
     * @param manager RenderManager
     */
    public void setRenderManager(RenderManager manager) {
        WebmailMediator.renderManager = manager;
        if (renderManager != null) {
            renderManager.getOnDemandRenderer(ON_DEMAND_RENDERER).add(this);
        }
    }
    
    /**
     * Requests a requestRender on the RenderManager class.  This method should
     * be used instead of directly manipulating the FacesContext as it is much
     * more effecient and scallable.
     */
    public static void requestOnDemandGroupRender() {
        if (renderManager != null) {
            renderManager.getOnDemandRenderer(ON_DEMAND_RENDERER).requestRender();
        } else if (log.isDebugEnabled()) {
            log.debug("RenderManager could not initiate a group renderer request");
        }
    }
    
    /**
     * Requests a requestRender on the RenderManager class.  This method should
     * be used instead of directly manipulating the FacesContext as it is much
     * more effecient and scallable.
     */
    public void requestOnDemandRender() {
        if (renderManager != null) {
            renderManager.requestRender(this);
        } else if (log.isDebugEnabled()) {
            log.debug("RenderManager could not initiate a renderer request");
        }
    }
    
    /**
     * Runnable interface call back for this class persistent faces tate.
     *
     * @return persistent faces state of this class.
     */
    public PersistentFacesState getState() {
        return persistentFacesState;
    }
    
    /**
     * Callback method that is called if any exception occurs during an attempt
     * to render this Renderable.
     *
     * @param renderingException The exception that occurred when attempting
     * to render this Renderable.
     */
    public void renderingException(RenderingException renderingException) {
        if (log.isDebugEnabled() &&
                renderingException instanceof TransientRenderingException) {
            log.debug("Transient Rendering excpetion:", renderingException);
        } else if (renderingException instanceof FatalRenderingException) {
            if (log.isDebugEnabled()) {
                log.debug("Fatal rendering exception: ", renderingException);
            }
            renderManager.getOnDemandRenderer(ON_DEMAND_RENDERER).remove(this);
        }
    }
    
    
    /**
     * Called by the servelet container when a session is destroyed.  This
     * method will call the logOut method which does any needed clean-up.
     *
     * @param httpSessionEvent season event contains seesion id which can be used to
     *                         clean up the respective SeasonBean.
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        
        // remove all timers associated with this session
        MailAccountTimerCache.releaseMailAccountTimers(httpSessionEvent.getSession());
        
        if (FacesContext.getCurrentInstance() != null){
            Application application =
                    FacesContext.getCurrentInstance().getApplication();
            if (log.isDebugEnabled()){
                log.debug("Session Destroyed, cleaning up season data " +
                        httpSessionEvent.getSession().getId());
            }
            
            WebmailMediator webmailMediator =
                    ((WebmailMediator) application.createValueBinding("#{webMailMediator}").
                    getValue(FacesContext.getCurrentInstance()));
            // first listern will win on destroying timer.
            if (webmailMediator != null){
                webmailMediator.shutdownWebmail();
            }
        }else{
            if (log.isDebugEnabled()){
                log.debug("Session cound not be Destroyed, cleaning up season data failed ");
            }
        }
        
        // mark the session as being destroyed.
        httpSessionEvent.getSession().setAttribute(SESSION_DESTROYED, Boolean.TRUE);
        
        if (log.isDebugEnabled()){
            log.debug("sessionDestroyed id:= " + httpSessionEvent.getSession().getId());
        }
    }
    
    /**
     * Called by the servlet container when a session is to be created. Currently
     * doing nothing with it.
     *
     * @param httpSessionEvent session event associated with the creation of a new session.
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {}
    
    /**
     * Notification that the servlet context is about to be shut down. As a result
     * we want to kill the static javaMailTickleTimer.
     *
     * @param contextEvent servlet context event from servlet container.
     */
    public void contextDestroyed(ServletContextEvent contextEvent){
        if (log.isDebugEnabled()){
            log.debug("Context is about to be Destroyed");
        }
        
        // we need to grab the application context which will give us
        // our bens as this method is called from another thread.
        if (FacesContext.getCurrentInstance() != null){
            Application application =
                    FacesContext.getCurrentInstance().getApplication();
            
            WebmailMediator webmailMediator =
                    ((WebmailMediator) application.createValueBinding("#{webMailMediator}").
                    getValue(FacesContext.getCurrentInstance()));
            // first listern will win on destroying timer.
            if (webmailMediator != null){
                if (log.isDebugEnabled()){
                    log.debug("Context is about to be Destroyed, canceling application timer.");
                }
                if (webmailMediator.getJavaMailTickleTimer() != null){
                    webmailMediator.getJavaMailTickleTimer().cancel();
                }
            }
        }else{
            if (log.isDebugEnabled()){
                log.debug("Context is about to be Destroyed, cancelling timer");
            }
            if (getJavaMailTickleTimer() != null){
                getJavaMailTickleTimer().cancel();
            }
        }
        
    }
    
    /**
     * Notification that the web application is ready to process requests.
     * @param contextEvent servlet context event from servlet container.
     */
    public void contextInitialized(ServletContextEvent contextEvent){}
    
    /**
     * Gets the message bundle used by the webmail applications used for
     * internationalization.
     *
     * @return message bundle for internationalization.
     */
    public static ResourceBundle getMessageBundle() {
        initMessageBundle();
        return messageBundle;
    }
    
    private static void initMessageBundle() {
        if (messageBundle == null) {
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            // assign a default local if the faces context has none, shouldn't happen
            if (locale == null) {
                locale = Locale.ENGLISH;
            }
            messageBundle = ResourceBundle.getBundle(
                    "com.icesoft.applications.faces.webmail.resources.messages",
                    locale);
        }
    }
    
    /**
     * Utility method for setting the error message for the loging screen.
     *
     * @param errorMessage name of properties from messages.properites resource.
     * @param formName     name of form containing error message to add.
     * @param fieldName id of component that will have message appended.
     * @param miscText text to add that is not in a message bundle, can be null
     */
    public static void addMessage(String formName, String fieldName,
            String errorMessage, String miscText) {
        
        ResourceBundle messages = WebmailMediator.getMessageBundle();
        if (errorMessage != null){
            try {
                errorMessage = messages.getString(errorMessage);
            }
            // eat any errors, and just use origional message if there is a problem
            catch (MissingResourceException e) {
                if (log.isErrorEnabled())
                    log.error("Missing Resource bundle, could not display message");
            } catch (NullPointerException e) {
                if (log.isErrorEnabled())
                    log.error("Missing Resource bundle, could not dipslay message");
            }
        } else{
            errorMessage = "";
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message;
        
        if (miscText != null){
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    (errorMessage + " "+ miscText).trim());
        }else{
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    errorMessage);
        }
        // report the message.
        context.addMessage(formName + ":" + fieldName, message);
    }
    
    
    public void startWebmail(ActionEvent e) {
        
        // Validates the LoginBean username and password.
        this.loginManager.getLoginBeanFactory().validateLoginBean(null);
        
        // make sure the mediator has been initialized correctly and return
        // to the login page if necessary
        LoginBean loginBeanRun = null;
        if (loginManager != null) {
            loginManager.setMediator(this);
            loginManager.init();
            loginBeanRun = loginManager.getLoginBeanFactory().getLoginBean();
        }
        
        if (loginBeanRun == null) {
            WebmailMediator.addMessage(
                    "loginForm",
                    "errorMessage",
                    "webmail.login.invalidLogin", null);
        }
        
        // initialize the webapplication and navigate to webmail template
        if (LoginBean.LOGIN_SUCCESS.equals(
                loginManager.getLoginBeanFactory().getLoginValidateOutcome())) {
            mailManager.setshowModalPanel(true);
            WebmailMediator.addMessage(
                    "loginForm",
                    "loadingMessage",
                    "webmail.login.status", null);
            
            // start a new thread for loading webmail managers and displaying process bar for
            // webmail iniitialization
            Thread testThread = new Thread(new MailManagersOperationRunner(this));
            testThread.start();
        }
        // if there is an error stored in loginBean then we report it back
        // in the login page.
        else {
            log.info("validation error, login failure");
            WebmailMediator.addMessage(
                    "loginForm",
                    "errorMessage",
                    "webmail.login.invalidLogin", null);
        }
    }
    
    /**
     * Helper function for rendering the update on process bar
     *
     * @param webmailMediator
     */
    public void renderHelper(){
        try {
            renderManager.requestRender(this);
        } catch (IllegalStateException e) {
            log.error("renderHelper illegalState exception", e);
        }
    }
    
}
