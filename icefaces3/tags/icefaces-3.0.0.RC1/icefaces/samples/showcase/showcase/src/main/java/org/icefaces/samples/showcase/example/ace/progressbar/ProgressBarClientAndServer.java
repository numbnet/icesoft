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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.icefaces.ace.event.ProgressBarChangeEvent;


@ComponentExample(
        parent = ProgressBarBean.BEAN_NAME,
        title = "example.ace.progressBarClientAndServer.title",
        description = "example.ace.progressBarClientAndServer.description",
        example = "/resources/examples/ace/progressbar/progressBarClientAndServer.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressBarClientAndServer.xhtml",
                    resource = "/resources/examples/ace/progressbar/progressBarClientAndServer.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="progressBarClientAndServer.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/progressbar/progressBarClientAndServer.java")
        }
)
@ManagedBean(name= ProgressBarClientAndServer.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarClientAndServer extends ComponentExampleImpl<ProgressBarClientAndServer> implements Serializable {

    public static final String BEAN_NAME = "progressBarClientAndServer";
    private int progressValue;
    private String message;
    
    public ProgressBarClientAndServer() 
    {
        super(ProgressBarClientAndServer.class);
        progressValue = 0;
        message = "";
    }
    
    public void changeListener(ProgressBarChangeEvent event) 
    {
         message = (int)event.getPercentage() + "%";
    }
    
    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
