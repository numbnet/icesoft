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
public class QRScanController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

    @RequestMapping(value = "/qrscan", method = RequestMethod.GET)
    public void variousGet(HttpServletRequest request, QRScanBean model) {
    }

    @RequestMapping(value = "/qrscan", method = RequestMethod.POST)
    public void variousPost(HttpServletRequest request,
                            QRScanBean model) {
    }

    @ModelAttribute("QRScanBean")
    public QRScanBean createBean() {
        return new QRScanBean();
    }
}

