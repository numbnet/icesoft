package org.icemobile.samples.spring.controllers;

import java.io.IOException;

import org.icemobile.component.ContactDecoder;
import org.icemobile.samples.spring.AjaxUtils;
import org.icemobile.samples.spring.ContactBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

@Controller
@SessionAttributes("contactBean")
public class ContactListController extends BaseController {
    
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
