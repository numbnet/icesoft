package org.icepush.samples.icechat.spring.mvc;

import org.icepush.samples.icechat.beans.model.BaseCredentialsBean;

public class LoginFormData extends BaseCredentialsBean {
    public LoginFormData() {
    }

    public LoginFormData(String userName, String nickName, String password) {
        setUserName(userName);
        setNickName(nickName);
        setPassword(password);
    }

    public void clear() {
        setUserName(null);
        setNickName(null);
        setPassword(null);
    }
}
