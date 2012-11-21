package org.icemobile.samples.springbasic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

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
        String rawContact = contactBean.getRawContact();
        contactBean.setEmail(null);
        contactBean.setPhone(null);
        contactBean.setName(null);
        if( rawContact != null && !"".equals(rawContact)){
            try {
                //contact string has to be decoded
                String decoded = URLDecoder.decode(rawContact,"UTF-8");
                String[] tokens = decoded.split("&");
                for( int i = 0 ; i < tokens.length ; i++ ){
                    //each contact field will have a key and value
                    String key = tokens[i].substring(0,tokens[i].indexOf("="));
                    String val = tokens[i].substring(tokens[i].indexOf("=")+1);
                    //possible keys are 'name', 'phone', and 'email'
                    if( "name".equals(key)){
                        contactBean.setName(val);
                    }
                    else if( "phone".equals(key)){
                        contactBean.setPhone(val);
                    }
                    else if( "email".equals(key)){
                        contactBean.setEmail(val);
                    }
                    
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


}
