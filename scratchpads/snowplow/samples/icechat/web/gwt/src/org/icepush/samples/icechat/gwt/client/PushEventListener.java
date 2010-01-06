package org.icepush.samples.icechat.gwt.client;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public abstract class PushEventListener{

    private String pushId;
    private String[] groups;

    public abstract void onPushEvent();

    protected String getPushId(){
        return this.pushId;
    }

    protected void setPushId(String id){
        this.pushId = id;
    }

    protected String[] getGroups() {
        return groups;
    }

    protected void setGroups(String[] groups) {
        this.groups = groups;
    }




}