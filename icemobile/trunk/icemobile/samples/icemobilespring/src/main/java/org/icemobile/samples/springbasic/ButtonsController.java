package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * This is an example Client space Controller implementation for the buttons
 * demo on the various UI page. Both GET and POST operations navigate to the
 * /various page.
 */
@Controller
public class ButtonsController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

    @RequestMapping(value = "/buttons", method = RequestMethod.GET)
    public void variousGet(HttpServletRequest request, GeolocationBean model) {
    }

    @RequestMapping(value = "/buttons", method = RequestMethod.POST)
    public void variousPost(HttpServletRequest request,
                              GeolocationBean model) {
    }

    @ModelAttribute("buttonsBean")
    public GeolocationBean createBean() {
        return new GeolocationBean();
    }

}
