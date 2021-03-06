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

package org.icefaces.samples.showcase.example.ace.progressbar; 

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;


@ComponentExample(
        parent = ProgressBarBean.BEAN_NAME,
        title = "example.ace.progressBarClientSideOnly.title",
        description = "example.ace.progressBarClientSideOnly.description",
        example = "/resources/examples/ace/progressbar/progressBarClient.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressBarClient.xhtml",
                    resource = "/resources/examples/ace/progressbar/progressBarClient.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressBarClient.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/progressbar/ProgressBarClient.java")
        }
)
@ManagedBean(name= ProgressBarClient.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarClient extends ComponentExampleImpl<ProgressBarClient> implements Serializable {

    public static final String BEAN_NAME = "progressBarClient";
    
    public ProgressBarClient() {
        super(ProgressBarClient.class);
    }
}
