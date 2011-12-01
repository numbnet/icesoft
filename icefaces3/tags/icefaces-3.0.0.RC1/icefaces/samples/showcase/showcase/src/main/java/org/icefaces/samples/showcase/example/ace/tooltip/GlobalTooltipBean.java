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

package org.icefaces.samples.showcase.example.ace.tooltip; 

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;


@ComponentExample(
        parent = TooltipOverviewBean.BEAN_NAME,
        title = "example.ace.globalTooltip.title",
        description = "example.ace.globalTooltip.description",
        example = "/resources/examples/ace/tooltip/globalTooltip.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="globalTooltip.xhtml",
                    resource = "/resources/examples/ace/tooltip/globalTooltip.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="GlobalTooltip.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tooltip/GlobalTooltipBean.java")
        }
)

@ManagedBean(name= GlobalTooltipBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GlobalTooltipBean extends ComponentExampleImpl<GlobalTooltipBean> implements Serializable {

    public static final String BEAN_NAME = "globalTooltipBean";
    
    public GlobalTooltipBean() {
        super(GlobalTooltipBean.class);
    }
}
