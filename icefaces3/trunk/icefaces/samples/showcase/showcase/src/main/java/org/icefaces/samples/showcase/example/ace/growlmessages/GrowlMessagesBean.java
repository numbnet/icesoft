/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.samples.showcase.example.ace.growlmessages;

import org.icefaces.ace.component.textentry.TextEntry;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.HashMap;

@ComponentExample(
        title = "example.ace.growlmessages.title",
        description = "example.ace.growlmessages.description",
        example = "/resources/examples/ace/growlmessages/growlmessages.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "growlmessages.xhtml",
                        resource = "/resources/examples/ace/growlmessages/growlmessages.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "GrowlMessagesBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/growlmessages/GrowlMessagesBean.java")
        }
)
@Menu(
        title = "menu.ace.growlmessages.subMenu.main",
        menuLinks = {
                @MenuLink(title = "menu.ace.growlmessages.subMenu.main",
                        isDefault = true,
                        exampleBeanName = GrowlMessagesBean.BEAN_NAME)
        }
)
@ManagedBean(name = GrowlMessagesBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GrowlMessagesBean extends ComponentExampleImpl<GrowlMessagesBean> implements Serializable {
    public static final String BEAN_NAME = "growlMessagesBean";

    private String firstName;
    private String lastName;
    private String city;
    private String province;
    private String country;
    
    private String autoHide = "true";
    private boolean closeAll = true;
    private int maxVisibleMessages = 0;
    private String position = "top-right";

    private static HashMap<String, Integer> severityMap = new HashMap<String, Integer>() {{
            put("First Name", 0);
            put("Last Name", 1);
            put("City", 2);
            put("Country", 3);
        }
        private static final long serialVersionUID = 6584997908723158778L;
    };
    private static String[] severityNames = {"Info", "Warn", "Error", "Fatal"};

    public GrowlMessagesBean() {
        super(GrowlMessagesBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void blurListener(AjaxBehaviorEvent event) {
        TextEntry textEntry = (TextEntry) event.getComponent();
        String value = textEntry.getValue().toString().trim();
        String label = textEntry.getLabel();
        if (value.equals("") || value.equalsIgnoreCase(label)) {
            int index = severityMap.get(label);
            String message;
            if (value.equals("")) {
                message = "Sample " + severityNames[index] + ": " + label + " missing.";
            } else {
                message = "Sample " + severityNames[index] + ": Value cannot be \"" + value + "\"";
            }
            FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(index), message, message);
            FacesContext.getCurrentInstance().addMessage(textEntry.getClientId(), facesMessage);
        }
    }

    public String getAutoHide() {
        return autoHide;
    }

    public void setAutoHide(String autoHide) {
        this.autoHide = autoHide;
    }

    public boolean isCloseAll() {
        return closeAll;
    }

    public void setCloseAll(boolean closeAll) {
        this.closeAll = closeAll;
    }

    public int getMaxVisibleMessages() {
        return maxVisibleMessages;
    }

    public void setMaxVisibleMessages(int maxVisibleMessages) {
        this.maxVisibleMessages = maxVisibleMessages;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
