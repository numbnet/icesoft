package org.icemobile.samples.springbasic;

import java.io.IOException;

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
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/icemobile")
@SessionAttributes("icemobileBean")
public class ICEmobileController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

	@RequestMapping(method=RequestMethod.GET)
	public void fileUploadForm() {
	}

	@ModelAttribute("icemobileBean")
	public ModelBean createBean() {
		return new ModelBean();
	}

	@RequestMapping(method=RequestMethod.POST)
	public void processUpload(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "camera-file", required = false) MultipartFile file, @RequestParam(value = "camera", required = false) MultipartFile inputFile, Model model) throws IOException {
        String fileName = "empty";
        if (null != file)  {
            fileName = file.getOriginalFilename();
            file.transferTo(new File(request.getRealPath("/resources/uploaded.jpg")));
        }
        if (null != inputFile)  {
            fileName = inputFile.getOriginalFilename();
            inputFile.transferTo(new File(request.getRealPath("/resources/uploaded.jpg")));
        }
		model.addAttribute("message", "Hello " + modelBean.getName() + ", your file '" + fileName + "' was uploaded successfully.");
	}
	
}
