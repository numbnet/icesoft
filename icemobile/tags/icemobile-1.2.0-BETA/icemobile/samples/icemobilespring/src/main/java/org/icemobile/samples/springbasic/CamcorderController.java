package org.icemobile.samples.springbasic;

import java.io.IOException;

import org.icemobile.util.ClientDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"camcorderBean","camcorderUploadReady","camcorderMessage","camcorderUpload"})
public class CamcorderController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}
	
    @ModelAttribute
    public void iosAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("ios", ClientDescriptor.getInstance(request).isIOS());
    }

	@ModelAttribute("camcorderBean")
	public ModelBean createBean() {
		return new ModelBean();
	}

	@RequestMapping(value = "/camcorder", method=RequestMethod.GET)
    public void get(Model model)  {
    }
	
	@RequestMapping(value = "/camcorder", method=RequestMethod.POST)
    public void post(Model model)  {
    }


	@RequestMapping(value = "/camcorder", method=RequestMethod.POST, consumes="multipart/form-data")
	public void processVideo(HttpServletRequest request, ModelBean modelBean,
                             @RequestParam(value = "camvid", required = false) MultipartFile file,
                             Model model) throws IOException {
	    String videoFilename = "video-" + Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()), 32) + ".mp4";
        if (null != file)  {
            file.transferTo(new File(request.getRealPath("/media/"+videoFilename)));
        }
        model.addAttribute("camcorderUploadReady", true);
		model.addAttribute("camcorderMessage", "Hello " + modelBean.getName() +
                ", your video was uploaded successfully.");
		model.addAttribute("camcorderUpload", "./media/"+videoFilename);

    }

}
