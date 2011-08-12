/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mediacast.navigation;

import java.io.Serializable;

/**
 * Destination hold a page tile and path to the respective include.
 */
public class Destination implements Serializable {

    private String key;
    private String title;
    private String backTitle;
    private String path;
    private boolean hideBack;

    public Destination(String key, String title, String backTitle, String path) {
        this.key = key;
        this.title = title;
        this.path = path;
        this.backTitle = backTitle;
    }

    public Destination(String key, String title, String backTitle, String path, boolean hideBack) {
        this.key = key;
        this.title = title;
        this.path = path;
        this.backTitle = backTitle;
        this.hideBack = hideBack;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getBackTitle() {
        return backTitle;
    }

    public boolean isHideBack() {
        return hideBack;
    }

    public String getKey() {
        return key;
    }
}
