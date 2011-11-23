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

package org.icefaces.samples.showcase.example.compat.outputResource;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = OutputResourceBean.BEAN_NAME,
        title = "example.compat.outputResource.filename.title",
        description = "example.compat.outputResource.filename.description",
        example = "/resources/examples/compat/outputResource/outputResourceFilename.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResourceFilename.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResourceFilename.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceFilename.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceFilename.java")
        }
)
@ManagedBean(name= OutputResourceFilename.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceFilename extends ComponentExampleImpl<OutputResourceFilename> implements Serializable {
	
	public static final String BEAN_NAME = "outputResourceFilename";
	
	private String name = OutputResourceBean.CUSTOM_NAME;
	
	public OutputResourceFilename() {
		super(OutputResourceFilename.class);
	}
	
	public String getName() { return name; }
	
	public void setName(String name) { this.name = name; }
}
