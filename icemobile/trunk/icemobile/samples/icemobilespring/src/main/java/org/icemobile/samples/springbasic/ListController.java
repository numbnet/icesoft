package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * This is an example Client space Controller implementation for the inputtext
 * demo on the various UI page. Both GET and POST operations navigate to the
 * /inputtext page.
 */
@Controller
public class ListController {

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void variousGet(HttpServletRequest request, ListBean model) {
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void variousPost(HttpServletRequest request,
                            ListBean model) {
    }

    @ModelAttribute("ListBean")
    public ListBean createBean() {
        return new ListBean();
    }

}
