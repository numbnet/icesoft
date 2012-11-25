package org.icemobile.samples.springbasic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import org.icemobile.component.ContactDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

@Controller
@SessionAttributes("contactBean")
public class ContactListController {
    
    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }

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
        ContactDecoder contactDecoder = new ContactDecoder(contactBean.getRawContact());
        contactBean.reset();
        contactBean.setEmail(contactDecoder.getEmail());
        contactBean.setPhone(contactDecoder.getPhone());
        contactBean.setName(contactDecoder.getName());
    }


}
