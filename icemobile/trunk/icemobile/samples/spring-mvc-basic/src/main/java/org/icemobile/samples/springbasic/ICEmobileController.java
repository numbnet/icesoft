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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("icemobileBean")
public class ICEmobileController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

	@RequestMapping(value="/icemobile", method=RequestMethod.GET)
	public void fileUploadForm() {
	}

	@ModelAttribute("icemobileBean")
	public ModelBean createBean() {
		return new ModelBean();
	}

	@RequestMapping(value="/icemobile", method=RequestMethod.POST)
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
	
	@RequestMapping(value = "/jsoncam", method=RequestMethod.POST)
	public @ResponseBody CamUpdate jsonCamera(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "camera-file", required = false) MultipartFile file, @RequestParam(value = "camera", required = false) MultipartFile inputFile, Model model) throws IOException {

        String fileName = "empty";
        String uuid = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32 );
        String newFileName = "/resources/img-" + uuid + ".jpg";
        if (null != file)  {
            fileName = file.getOriginalFilename();
            file.transferTo(new File(request.getRealPath(newFileName)));
        }
        if (null != inputFile)  {
            fileName = inputFile.getOriginalFilename();
            inputFile.transferTo(new File(request.getRealPath(newFileName)));
        }

        return new CamUpdate("Thanks for the photo, " + modelBean.getName(), 
                request.getContextPath() + newFileName);
    }

}

class CamUpdate  {
    private String path;
    private String message;

    public CamUpdate(String message, String path)  {
        this.path = path;
        this.message = message;
    }

    public String getPath()  {
        return path;
    }

    public String getMessage()  {
        return message;
    }
}
