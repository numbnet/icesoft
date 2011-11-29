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
package org.icefaces.samples.showcase.example.compat.overview;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.compat.iceSuiteOverview.title",
        description = "example.compat.iceSuiteOverview.description",
        example = "/resources/examples/compat/iceSuiteOverview/iceSuiteOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="iceSuiteOverview.xhtml",
                    resource = "/resources/examples/compat/iceSuiteOverview/iceSuiteOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="IceSuiteOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/compat/overview/IceSuiteOverviewBean.java")
        }
)
@Menu(
	title = "menu.compat.iceSuiteOverview.subMenu.title",
	menuLinks = {
                    @MenuLink(title = "menu.compat.iceSuiteOverview.subMenu.main", isDefault = true, exampleBeanName = IceSuiteOverviewBean.BEAN_NAME)
                }
)

@ManagedBean(name= IceSuiteOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class IceSuiteOverviewBean extends ComponentExampleImpl<IceSuiteOverviewBean> implements Serializable 
{
    public static final String BEAN_NAME = "iceSuiteOverview";
    
    public IceSuiteOverviewBean() 
    {
        super(IceSuiteOverviewBean.class);
    }
}
