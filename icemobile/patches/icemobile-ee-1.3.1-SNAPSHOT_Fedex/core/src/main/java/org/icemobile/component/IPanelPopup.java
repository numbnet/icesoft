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

public interface IPanelPopup extends IMobiComponent{

    public static final String BLACKOUT_PNL_HIDDEN_CLASS = "mobi-panelpopup-bg-hide ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelpopup-bg ";
    public static final String CONTAINER_CLASS = "mobi-panelpopup-container ";
    public static final String HIDDEN_CONTAINER_CLASS = "mobi-panelpopup-container-hide ";
    public static final String TITLE_CLASS = "mobi-panelpopup-title-container ";


    public String getHeaderText();
    public void setHeaderText(String text);
    public boolean isClientSide();
    public void setClientSide(boolean clientSide);

    public String getId();
    public boolean isAutoCenter();
    public void setAutoCenter(boolean autoCenter);

    public void setWidth(int width);
    public int getWidth();

    public void setHeight(int height);
    public int getHeight();

    public boolean isVisible();
    public void setVisible(boolean visible);

}
