package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * This is an example Client space Controller implementation for the flipSwitch
 * demo on the various UI page. Both GET and POST operations navigate to the
 * /various page.
 */
@Controller
public class AccordionController {

    @RequestMapping(value = "/accordion", method = RequestMethod.GET)
    public void variousGet(HttpServletRequest request, AccordionBean model) {
    }

    @RequestMapping(value = "/accordion", method = RequestMethod.POST)
    public void variousPost(HttpServletRequest request,
                            AccordionBean model) {
    }

    @ModelAttribute("accordionBean")
    public AccordionBean createBean() {
        return new AccordionBean();
    }
}
