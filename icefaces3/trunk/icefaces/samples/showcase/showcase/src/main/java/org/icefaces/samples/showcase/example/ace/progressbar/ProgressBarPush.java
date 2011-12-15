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

package org.icefaces.samples.showcase.example.ace.progressbar; 

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.example.compat.progress.LongTaskManager;
import org.icefaces.samples.showcase.util.FacesUtils;


 @ComponentExample(
        parent = ProgressBarBean.BEAN_NAME,
        title = "example.ace.progressBarPush.title",
        description = "example.ace.progressBarPush.description",
        example = "/resources/examples/ace/progressbar/progressBarPush.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressBarPush.xhtml",
                    resource = "/resources/examples/ace/progressbar/progressBarPush.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressBarPush.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/progressbar/ProgressBarPush.java"),
            @ExampleResource(type = ResourceType.java,
                    title="LongTaskManager.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/compat/progress/LongTaskManager.java")
        }
)
@ManagedBean(name= ProgressBarPush.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarPush extends ComponentExampleImpl<ProgressBarPush> implements Serializable {
    
    public static final String BEAN_NAME = "progressBarPush";
    
    public ProgressBarPush() 
    {
        super(ProgressBarPush.class);
    }
    
    public void startTask(ActionEvent event) 
    {
        LongTaskManager threadBean = (LongTaskManager)FacesUtils.getManagedBean(LongTaskManager.BEAN_NAME);
        threadBean.startThread(10, 10, 1000);
    }
}