/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.samples.spring.controllers;

import java.io.IOException;

import org.icemobile.component.ContactDecoder;
import org.icemobile.samples.spring.ContactBean;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("contactBean")
public class ContactListController extends ICEmobileBaseController {
    
    @ModelAttribute("contactBean")
    public ContactBean createBean() {
        return new ContactBean();
    }
    
    @RequestMapping(value = "/contact")
    public void doRequest(
        @ModelAttribute("contactBean") ContactBean model) {
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public void process(ContactBean contactBean) throws IOException {
        //raw contact string will be in encoded format 
        //of [contact=val&][phone=val&][email=val&]
        String rawContact = contactBean.getRawContact();
        if (null == rawContact)  {
            return;
        }
        ContactDecoder contactDecoder = new ContactDecoder(contactBean.getRawContact());
        contactBean.reset();
        contactBean.setEmail(contactDecoder.getEmail());
        contactBean.setPhone(contactDecoder.getPhone());
        contactBean.setName(contactDecoder.getName());
    }


}
