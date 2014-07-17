/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.component;


public interface IButton extends IMobiComponent{
   // Default button types.
    public static final String BUTTON_TYPE_UNIMPORTANT = "unimportant";
    public static final String BUTTON_TYPE_IMPORTANT = "important";
    public static final String BUTTON_TYPE_ATTENTION = "attention";
    public static final String BUTTON_TYPE_BACK = "back";
    //
    public static final String BUTTON_SUBMIT = "submit";
    public static final String BUTTON_DEFAULT = "button";
    // button styles.
    public static final String DISABLED_STYLE_CLASS = " mobi-button-dis";
    public static final String IMPORTANT_STYLE_CLASS = " mobi-button-important";
    public static final String UNIMPORTANT_STYLE_CLASS = " mobi-button-unimportant";
    public static final String BACK_STYLE_CLASS = " mobi-button-back";
    public static final String ATTENTION_STYLE_CLASS = " mobi-button-attention";
    public static final String SELECTED_STYLE_CLASS = " mobi-button-selected ui-btn-active";

    /**
    * <p>so far can use this to distinguish between JSF and JSP</p>
    */
    public boolean isSingleSubmit();

    public Object getValue();
    /**
     * <p>styled based on button type</p>
     */
    public String getButtonType();

    /**
     *   <p> possible values are button, submit.  default is button </p>
     */
    public String getType();

    public Boolean isSelectedButton();
    public void setSelectedButton(Boolean selected);

    public boolean isSelected();
    public String getGroupId();
    public void setGroupId(String groupId);

    public String getSubmitNotification();
    public void setSubmitNotification(String sn);

    public String getPanelConfirmation();
    public void setPanelConfirmation(String pcId);

    public String getParams();
    public void setParams(String params);

    public String getBehaviors();
    public void setBehaviors(String behaviors);

    public void setPanelConfirmationId(String pcId);
    public String getPanelConfirmationId();

    public void setSubmitNotificationId(String snId);
    public String getSubmitNotificationId();

    public String getOpenContentPane();
    public StringBuilder getJsCall();
    public void setJsCall(StringBuilder jsCall);
    public boolean isParentDisabled();
    public void setParentDisabled(boolean disabled);
    public String getName();
  //  public String getSrc();
}
