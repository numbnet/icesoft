package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import org.icepush.PushContext;
import org.icepush.PushNotification;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.inject.Inject;

@Controller
public class CloudPushController {

    @Inject
    private WebApplicationContext context;

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }

    @RequestMapping(value = "/cloudpush", 
            headers="content-type=application/x-www-form-urlencoded")
    public void fileUploadFormPost(HttpServletRequest request, Model model) {
        fileUploadForm(request, model);
    }

    @RequestMapping(value = "/cloudpush", method = RequestMethod.GET)
    public void fileUploadForm(HttpServletRequest request, Model model) {
        model.addAttribute("isGET", Boolean.TRUE);
    }

    @RequestMapping(value = "/cloudpushregion", method = RequestMethod.GET)
    public void camRegion(HttpServletRequest request, Model model) {
        model.addAttribute("isGET", Boolean.TRUE);
    }

    @ModelAttribute("cloudpushBean")
    public CloudPushBean createBean() {
        return new CloudPushBean();
    }

    @RequestMapping(value = "/cloudpush", method = RequestMethod.POST)
    public void processUpload(
            @RequestParam(value = "pushType", required = false) String pushType,
            @ModelAttribute("cloudpushBean") CloudPushBean model)
            throws IOException {
        PushContext.getInstance(context.getServletContext())
                .push("cloudPush",
                new PushNotification(model.title, model.message));
    }

}
