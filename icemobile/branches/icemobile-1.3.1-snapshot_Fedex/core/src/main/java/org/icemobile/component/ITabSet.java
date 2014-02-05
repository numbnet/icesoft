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


public interface ITabSet extends IMobiComponent{
    
    public static final StringBuilder TABSET_CONTAINER_CLASS = new StringBuilder("mobi-tabset");
    public static final StringBuilder TABSET_CONTAINER_BOTTOM_CLASS = new StringBuilder("mobi-tabset-tabs-bottom");
    public static final StringBuilder TABSET_CONTAINER_BOTTOM_FOOTER_CLASS = new StringBuilder("mobi-tabset-tabs-bottom-footer");
    public static final StringBuilder TABSET_CONTAINER_TOP_CLASS = new StringBuilder("mobi-tabset-tabs-top");
    public static final StringBuilder TABSET_CONTAINER_TOP_HEADER_CLASS = new StringBuilder("mobi-tabset-tabs-top-header");
    public static final StringBuilder TABSET_TABS_CLASS = new StringBuilder("mobi-tabset-tabs ui-bar-b");
    /* activeTab is now done with javascript so tabs are not rendered every request if not changed */
  //  public static final StringBuilder TABSET_ACTIVETAB_CLASS = new StringBuilder("activeTab ");
    public static final StringBuilder TABSET_CONTENT_CLASS = new StringBuilder("mobi-tabset-content");
    public static final String TABSET_HIDDEN_PAGECLASS = "mobi-tabpage-hidden";
    public static final String TABSET_ACTIVE_CONTENT_CLASS= "mobi-tabpage";

    public void setHeight(String fixedHeight);
    public String getHeight();
    public int getIndex();
    public boolean isAutoWidth();
    public void setAutoWidth(boolean width);
    public void setSelectedId(String currentId);
    public String getSelectedId();
    public String getHashVal();
    public String getOrientation();
    public boolean getIsTop();
    public boolean setIsTop(String orientation);
    public Boolean isParentFooter();
    public Boolean isParentHeader();
    public boolean isSingleSubmit();
    public boolean isAutoHeight();
    public String getDefaultId();
    public void setDefaultId(String defId);
    public boolean isFitToParent();

}
