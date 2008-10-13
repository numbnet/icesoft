package org.icesoft.testclient.event;

import org.icesoft.testclient.client.Client;

import java.net.URLEncoder;

/**
 * This Event sends the loginEvent necessary for logging in by prssing a button.
 * Hopefully, this is generic enough to enable logon to a lot of applications.  
 */
public class LoginEvent extends MouseClickEvent {

    // These are the form and component containing the username of the client
    protected String loginForm;
    protected String loginTextComponent;
    protected String passwordComponent;
    protected String loginButton;

    // this is the element to wind up with the focus. On login, this will usually
    // end up with the button that is pressed.
    protected String focusForm;
    protected String focusComponent;

    // username submitted as the value of the login component
    protected String username;
    protected String password;

    protected String loginButtonValue;

    public String encodeEvent(Client controller) {

    // get the login clicks event
        StringBuffer data = new StringBuffer( super.encodeEvent(controller) );

        try {

            data.append("&").append(URLEncoder.encode(loginForm+":"+
                                                      loginButton, "UTF-8")).append("=");
            data.append(URLEncoder.encode(loginButtonValue, "UTF-8")).append("&");

            data.append(URLEncoder.encode(loginForm, "UTF-8")).append("=");
            data.append(URLEncoder.encode(loginForm, "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.focus", "UTF-8")).append("=");
            data.append(URLEncoder.encode(focusForm + ":" + focusComponent, "UTF-8")).append("&");

            data.append(URLEncoder.encode(loginForm + ":" + loginTextComponent, "UTF-8")).append("=");
            data.append(URLEncoder.encode(username, "UTF-8")).append("&");

            if (password != null) {
                data.append(URLEncoder.encode(loginForm + ":" + passwordComponent, "UTF-8")).append("=");
                data.append(URLEncoder.encode(password, "UTF-8")).append("&");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toString();
    }


    public void setLoginButtonValue(String loginButtonValue) {
        this.loginButtonValue = loginButtonValue;
    }


    public void setLoginButton(String loginButton) {
        this.loginButton = loginButton;
        super.setComponentId( loginButton );
        
    }

    /**
     * The id of the component to receieve focus after the login. Generally
     * the button that is pressed
     * @param focusComponent id of component to get focus 
     */
    public void setFocusComponent(String focusComponent) {
        this.focusComponent = focusComponent;
    }

    /**
     * The id of the form containing the component to receieve focus after the login.
     * @param focusForm id of form containing component to get focus 
     */
    public void setFocusForm(String focusForm) {
        this.focusForm = focusForm;
    }

    /**
     * Id of component that will have the value of the password. Generally
     * a textfield. 
     * @param loginTextComponent id
     */
    public void setLoginTextComponent(String loginTextComponent) {
        this.loginTextComponent = loginTextComponent;
    }

    /**
     * Id of form containing loginTextComponent
     * @param loginForm id
     */
    public void setLoginForm(String loginForm) {
        this.loginForm = loginForm;
    }

    /**
     * Not guaranteed to work just yet! 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Value to be passed with the login component. 
     * @param username username of client
     */
    public void setUsername(String username) {
        this.username = username;
    }


    public void setPasswordComponent(String passwordComponent) {
        this.passwordComponent = passwordComponent;
    }
}
