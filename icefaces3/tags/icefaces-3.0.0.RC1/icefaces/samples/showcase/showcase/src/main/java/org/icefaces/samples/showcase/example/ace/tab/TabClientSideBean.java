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

package org.icefaces.samples.showcase.example.ace.tab;


import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TabSetBean.BEAN_NAME,
        title = "example.ace.tabSet.clientSide.title",
        description = "example.ace.tabSet.clientSide.description",
        example = "/resources/examples/ace/tab/tabset-client_side.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabset-client_side.xhtml",
                    resource = "/resources/examples/ace/tab/tabset-client_side.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="TabClientSideBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tab/TabClientSideBean.java")
        }
)
@ManagedBean(name = TabClientSideBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabClientSideBean extends ComponentExampleImpl<TabClientSideBean>
        implements Serializable {

    public static final String BEAN_NAME = "tabClientSide";

    private boolean fastTabs = true; // Add delay (large image, backend wait etc.) to tab loading make tabset difference clear

    public String getSlowDownTab() {
        try { Thread.sleep(2000); }
        catch (Exception e) {
            FacesUtils.addErrorMessage("Server-side tab waiting could not finish.");
        }
        return "";
    }

    public void setSlowDownTab(String slowDownTab) {}

    public boolean isFastTabs() {
        return fastTabs;
    }

    public void setFastTabs(boolean fastTabs) {
        this.fastTabs = fastTabs;
    }

    public TabClientSideBean() {
        super(TabClientSideBean.class);
    }
}
