package org.icepush.samples.icechat.spring.mvc;

public class LoginFormData {
    private String userName;
    private String nickName;
    private String password;
    
    public String getUserName() { return userName; }
    public String getNickName() { return nickName; }
    public String getPassword() { return password; }
    
    public void setUserName(String userName) { this.userName = userName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public void setPassword(String password) { this.password = password; }
}
