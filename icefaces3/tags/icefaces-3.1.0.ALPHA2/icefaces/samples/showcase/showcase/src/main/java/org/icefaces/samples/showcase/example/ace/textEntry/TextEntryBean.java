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

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.textEntry.title",
        description = "example.ace.textEntry.description",
        example = "/resources/examples/ace/textEntry/textEntry.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textEntry.xhtml",
                    resource = "/resources/examples/ace/textEntry/textEntry.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextEntry.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textEntry/TextEntryBean.java")
        }
)
@Menu(
	title = "menu.ace.textEntry.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.textEntry.subMenu.main",
	                isDefault = true,
                    exampleBeanName = TextEntryBean.BEAN_NAME)
    }
)
@ManagedBean(name= TextEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TextEntryBean extends ComponentExampleImpl<TextEntryBean> implements Serializable
{
    public static final String BEAN_NAME = "textEntryBean";
    
    private String firstName;
    private String lastName;
    private String city;
    private String province;
    private String provinceCode;
    private String country;
    public TextEntryBean()
    {
        super(TextEntryBean.class);
    }
    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
