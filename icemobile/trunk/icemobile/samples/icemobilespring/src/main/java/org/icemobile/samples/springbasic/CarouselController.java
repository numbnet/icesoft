package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class CarouselController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

    @RequestMapping(value = "/carousel", method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, CarouselBean model) {
    }

    @RequestMapping(value = "/carousel", method = RequestMethod.POST)
    public void doPost(HttpServletRequest request,
                            CarouselBean model) {
    }

    @ModelAttribute("carouselBean")
    public CarouselBean createBean() {
        return new CarouselBean();
    }
}

