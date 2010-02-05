/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.gwt.client;

import java.io.Serializable;

/**
 *
 * @author icesoft
 */
public class Credentials implements Serializable {

    
    private String userName;
    private String sessionToken;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
    
    

    


}
