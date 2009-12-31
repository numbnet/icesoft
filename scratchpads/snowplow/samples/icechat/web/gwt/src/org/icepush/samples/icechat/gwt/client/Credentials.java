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
    private String nickName;

  
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    


}
