package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import javax.enterprise.inject.Model;

@Model
public class CredentialsBean implements Serializable{
	
	private String userName;
	private String nickName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
