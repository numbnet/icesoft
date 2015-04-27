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

package org.icemobile.samples.mobileshowcase.view.navigation;

import java.io.Serializable;

/**
 *
 */
public class Destination implements Serializable {

    protected String title;

    protected String titleExt;

    private String titleBack;

    public Destination(String title, String titleExt, String titleBack) {
        this.title = title;
        this.titleBack = titleBack;
        this.titleExt = titleExt;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleBack() {
        return titleBack;
    }

    public String getTitleExt() {
        return titleExt;
    }
}
