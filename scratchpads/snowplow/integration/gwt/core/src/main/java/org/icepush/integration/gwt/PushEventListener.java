package org.icepush.integration.gwt;


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