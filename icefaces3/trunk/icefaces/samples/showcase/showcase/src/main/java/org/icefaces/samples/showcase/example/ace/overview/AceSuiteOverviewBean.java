
package org.icefaces.samples.showcase.example.ace.overview;
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

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.aceSuiteOverview.title",
        description = "example.ace.aceSuiteOverview.description",
        example = "/resources/examples/ace/aceSuiteOverview/aceSuiteOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="aceSuiteOverview.xhtml",
                    resource = "/resources/examples/ace/aceSuiteOverview/aceSuiteOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AceSuiteOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/overview/AceSuiteOverviewBean.java")
        }
)
@Menu(
	title = "menu.ace.accordionpanel.subMenu.title",
	menuLinks = {
                    @MenuLink(title = "menu.ace.accordionpanel.subMenu.main", isDefault = true, exampleBeanName = AceSuiteOverviewBean.BEAN_NAME)
                }
)
@ManagedBean(name= AceSuiteOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AceSuiteOverviewBean extends ComponentExampleImpl<AceSuiteOverviewBean> implements Serializable 
{
    public static final String BEAN_NAME = "aceSuiteOverview";
    
    public AceSuiteOverviewBean() 
    {
        super(AceSuiteOverviewBean.class);
    }
}
