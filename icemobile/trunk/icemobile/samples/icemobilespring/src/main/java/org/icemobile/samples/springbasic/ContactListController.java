package org.icemobile.samples.springbasic;

import java.io.IOException;
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
        String rawContact = contactBean.getContact();
        System.out.println("rawContact="+rawContact);
        if( rawContact != null ){
            String[] tokens = rawContact.split("%26");
            for( int i = 0 ; i < tokens.length ; i++ ){
                System.out.println("tokens="+tokens);
                String key = tokens[i].substring(0,tokens[i].indexOf("%3D"));
                String val = tokens[i].substring(tokens[i].indexOf("%3D")+3);
                System.out.println("key="+key+", val="+val);
                if( "contact".equals(key)){
                    contactBean.setName(val);
                }
                else if( "phone".equals(key)){
                    contactBean.setPhone(val);
                }
                else if( "email".equals(key)){
                    contactBean.setEmail(val);
                }
                
            }
        }
    }


}
