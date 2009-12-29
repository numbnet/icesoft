package org.icepush.samples.icechat.gwt.client;

import java.io.Serializable;


/**@TODO rename to be Transfer object */
public class User implements Serializable{
	private String username;
	private String nickname;
	private String password;
	
	public void setUsername(String u){
		this.username = u;
	}
	public void setNickname(String n){
		this.nickname = n;
	}
	
	public void setPassword(String p){
		this.password = p;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getNickname(){
		return this.nickname;
	}
	
	public String getPassword(){
		return this.password;
	}
}