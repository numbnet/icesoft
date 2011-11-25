 /*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.progressbar; 

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.icefaces.ace.event.ProgressBarChangeEvent;
import org.icefaces.application.PushRenderer;
import org.icefaces.samples.showcase.example.ace.progressbar.utilityBeans.PushManagementBean;
import org.icefaces.samples.showcase.example.ace.progressbar.utilityBeans.PushMessage;
import org.icefaces.util.EnvUtils;


 @ComponentExample(
        parent = ProgressBarBean.BEAN_NAME,
        title = "example.ace.progressBarPush.title",
        description = "example.ace.progressBarPush.description",
        example = "/resources/examples/ace/progressbar/progressBarPush.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressBarPush.xhtml",
                    resource = "/resources/examples/ace/progressbar/progressBarPush.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressBarPush.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/progressbar/ProgressBarPush.java")
        }
)
@ManagedBean(name= ProgressBarPush.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarPush extends ComponentExampleImpl<ProgressBarPush> implements Serializable {
    
    private static final String PUSH_GROUP = "myPushGroup";
    public static final String BEAN_NAME = "progressBarPush";
    
    @ManagedProperty(value="#{pushManagementBean}")
    private PushManagementBean pushManagementBean;
    
    private String sessionId;
    private String pushId;
    private int progressValue;
    private long time;
    
    public ProgressBarPush() 
    {
        super(ProgressBarPush.class);
        FacesContext fcontext = FacesContext.getCurrentInstance();
        HttpSession session = EnvUtils.getSafeSession(fcontext);
        sessionId = session.getId();
        pushId = PUSH_GROUP + sessionId;
        time = System.currentTimeMillis();
        PushRenderer.addCurrentSession(pushId);
    }
    
    public String startProgress()
    {
        clearAllMessages();
        if(progressValue!=0)
            progressValue = 0;
        String message = "Progress has been started! ";
        addMessageToPushManager(message);
        if(progressValue !=100 || progressValue > 100) {
              message = progressValue + "% complete ...";
              addMessageToPushManager(message);
        }
        PushRenderer.render(pushId);
        return null;
    }
    
    private void updateTime()
    {
        time = System.currentTimeMillis();
    }
    
    public void completeListener() 
    {
        String message = "Progress has been completed !";
        addMessageToPushManager(message);
        PushRenderer.render(pushId);
    }
    
    private void addMessageToPushManager(String messageToAdd)
    {
        PushMessage message = new PushMessage();
        message.setId(sessionId);
        message.setDescription(messageToAdd);
        pushManagementBean.addMessage(message);
    }

    public int getProgressValue() 
    {
        if(!pushManagementBean.getMessages().isEmpty())
        {
            int lastIndex = pushManagementBean.getMessages().size() -1;
            PushMessage lastMessage = pushManagementBean.getMessages().get(lastIndex);
            if(lastMessage.getId().equals(this.sessionId)&&progressValue<100)
            {
                long difference = System.currentTimeMillis() - time;
                if(difference>=2000)
                {
                    progressValue+=10;
                    updateTime();
                    if(progressValue !=100 || progressValue > 100) 
                    {
                        String message = progressValue + "% complete ...";
                        addMessageToPushManager(message);
                        PushRenderer.render(pushId);
                    }
                }
            }
        }
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public PushManagementBean getPushManagementBean() {
        return pushManagementBean;
    }

    public void setPushManagementBean(PushManagementBean pushManagementBean) {
        this.pushManagementBean = pushManagementBean;
    }

    private void clearAllMessages() {
        pushManagementBean.clearMessages();
    }
}