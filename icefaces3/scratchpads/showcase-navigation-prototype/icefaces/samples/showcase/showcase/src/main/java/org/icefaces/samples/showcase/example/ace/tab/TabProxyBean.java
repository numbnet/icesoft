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

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TabSetBean.BEAN_NAME,
        title = "example.ace.tabSet.proxy.title",
        description = "example.ace.tabSet.proxy.description",
        example = "/resources/examples/ace/tab/tabset-proxy.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabset-proxy_side.xhtml",
                    resource = "/resources/examples/ace/tab/tabset-proxy.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="TabProxyBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tab/TabProxyBean.java")
        }
)
@ManagedBean(name = TabProxyBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabProxyBean extends ComponentExampleImpl<TabProxyBean>
        implements Serializable {

    public static final String BEAN_NAME = "tabProxy";

    private String exampleText = "";
    private boolean invalidSwitch = false;
    private boolean uploadRequired = true;
    private boolean fileEntryRequired = true;

    public boolean isFileEntryRequired() {
        return fileEntryRequired;
    }

    public void setFileEntryRequired(boolean fileEntryRequired) {
        this.fileEntryRequired = fileEntryRequired;
    }

    public boolean isUploadRequired() {
        return uploadRequired;
    }

    public void setUploadRequired(boolean uploadRequired) {
        this.uploadRequired = uploadRequired;
    }

    public boolean isInvalidSwitch() {
        return invalidSwitch;
    }

    public void setInvalidSwitch(boolean invalidSwitch) {
        this.invalidSwitch = invalidSwitch;
    }

    public String getExampleText() {
        return exampleText;
    }

    public void setExampleText(String exampleText) {
        this.exampleText = exampleText;
    }

    public TabProxyBean() {
        super(TabProxyBean.class);
    }
}
