/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.textEntry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TextEntryBean.BEAN_NAME,
        title = "example.ace.textEntry.reqStyle.title",
        description = "example.ace.textEntry.reqStyle.description",
        example = "/resources/examples/ace/textEntry/textEntryReqStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textEntryReqStyle.xhtml",
                    resource = "/resources/examples/ace/textEntry/textEntryReqStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextEntryReqStyleBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textEntry/TextEntryReqStyleBean.java")
        }
)
@ManagedBean(name= TextEntryReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TextEntryReqStyleBean extends ComponentExampleImpl<TextEntryReqStyleBean> implements Serializable
{
    public static final String BEAN_NAME = "textEntryReqStyleBean";
    
    private String reqColor = "redRS";
    private String optColor = "greenRS";
    
    public TextEntryReqStyleBean() {
        super(TextEntryReqStyleBean.class);
    }
    
    public String getReqColor() {
        return reqColor;
    }
    
    public String getOptColor() {
        return optColor;
    }
    
    public void setReqColor(String reqColor) {
        this.reqColor = reqColor;
    }
    
    public void setOptColor(String optColor) {
        this.optColor = optColor;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
