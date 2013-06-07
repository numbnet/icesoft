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

public interface ITabPane extends IMobiComponent{
    public static final StringBuilder TABSET_TABS_CLASS = new StringBuilder("mobi-tabset-tabs ui-bar-b");
    public static final String TABSET_SPAN_CLASS = "mobitab";
    public static final String TABSET_HIDDEN_PAGECLASS = "mobi-tabpage-hidden";
    public static final String TABSET_ACTIVE_CONTENT_CLASS= "mobi-tabpage";
    public String getId();
    public String getTitle();
    public void setTitle(String title);
    public StringBuilder getContents();
    public int getIndex();
    public boolean isClient();
}
