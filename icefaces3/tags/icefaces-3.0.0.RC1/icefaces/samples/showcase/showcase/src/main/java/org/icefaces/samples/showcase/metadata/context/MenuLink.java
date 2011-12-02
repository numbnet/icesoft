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

package org.icefaces.samples.showcase.metadata.context;

import java.io.Serializable;

public class MenuLink implements Serializable {

    private String title;
    private boolean isDisabled;
    private boolean isDefault;
    private boolean isNew;
    private String exampleBeanName;

    public MenuLink(String title, boolean aDefault, boolean aNew, boolean isDisabled, String exampleBeanName) {
        this.title = title;
        this.isDisabled = isDisabled;
        isDefault = aDefault;
        isNew = aNew;
        this.exampleBeanName = exampleBeanName;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getExampleBeanName() {
        return exampleBeanName;
    }
}
