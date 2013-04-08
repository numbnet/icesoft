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

package org.icemobile.component;


public interface IMenuButtonItem extends IMobiComponent{

 //
    /**
    * <p>so far can use this to distinguish between JSF and JSP</p>
    */
    public boolean isSingleSubmit();

    public void setValue(Object value);
    public Object getValue();
    public String getLabel();
    public void setLabel(String label);

    public String getPanelConfirmationId();
 //   public void setPanelConfirmation(String pcId);

    public String getBehaviors();

    public String getSubmitNotificationId();

}
