/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.gwt.client;

/**
 *
 * @author icesoft
 */
public class UserSession {

    private Credentials credentials;

    private static UserSession instance = null;

    private UserSession(){

    }
    
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    
    public static UserSession getInstance(){
        if(instance == null){
            instance = new UserSession();
        }
        
        return instance;
    }



    
}
