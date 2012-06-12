package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

/**
 *
 */
@Controller
public class CarouselController {

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

