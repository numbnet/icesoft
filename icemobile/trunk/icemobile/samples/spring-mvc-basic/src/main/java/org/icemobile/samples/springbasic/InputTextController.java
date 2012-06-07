package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * This is an example Client space Controller implementation for the inputtext
 * demo on the various UI page. Both GET and POST operations navigate to the
 * /inputtext page.
 */
@Controller
public class InputTextController {

    @RequestMapping(value = "/inputtext", method = RequestMethod.GET)
    public void variousGet(HttpServletRequest request, InputTextBean model) {
    }

    @RequestMapping(value = "/inputtext", method = RequestMethod.POST)
    public void variousPost(HttpServletRequest request,
                            InputTextBean model) {
    }

    @ModelAttribute("inputTextBean")
    public InputTextBean createBean() {
        return new InputTextBean();
    }

}
