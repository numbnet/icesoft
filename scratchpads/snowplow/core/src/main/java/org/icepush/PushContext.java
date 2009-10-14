package org.icepush;

public interface PushContext {

    String createPushId(String browserId);

    void notify(String targetName);

    void addGroupMember(String groupName, String pushId);

    void removeGroupMember(String groupName, String pushId);
}
