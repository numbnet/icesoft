package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is an example Client space Controller implementation for the flipSwitch
 * demo on the various UI page. Both GET and POST operations navigate to the
 * /various page.
 */
@Controller
public class FlipSwitchController {

    @RequestMapping(value = "/various", method = RequestMethod.GET)
    public String variousGet(HttpServletRequest request, FlipSwitchBean model) {
        return "various";
    }

    @RequestMapping(value = "/various", method = RequestMethod.POST)
    public String variousPost(HttpServletRequest request,
                              FlipSwitchBean model) {
//        model.
        return "various";
    }

    @ModelAttribute("flipSwitchBean")
    public FlipSwitchBean createBean() {
        return new FlipSwitchBean();
    }

}
