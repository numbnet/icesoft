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
@SessionAttributes("microphoneBean")
public class MicrophoneController {

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

	@ModelAttribute("microphoneBean")
	public ModelBean createBean() {
		return new ModelBean();
	}

	@RequestMapping(value = "/jqmmic", method=RequestMethod.GET)
    public void processJqmmic()  {
    }
    
	@RequestMapping(value = "/jqmmic", method=RequestMethod.POST)
	public void processJqmmic(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic-file", required = false) MultipartFile file, @RequestParam(value = "camera", required = false) MultipartFile inputFile, Model model) throws IOException {
        this.processMicrophone(request, modelBean, file, inputFile, model);
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.GET)
    public void processMicrophone()  {
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.POST)
	public void processMicrophone(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic-file", required = false) MultipartFile file, @RequestParam(value = "mic", required = false) MultipartFile inputFile, Model model) throws IOException {

        String fileName = "empty";
        if (null != file)  {
            fileName = file.getOriginalFilename();
            file.transferTo(new File(request.getRealPath("/media/clip.mp4")));
        }
        if (null != inputFile)  {
            fileName = inputFile.getOriginalFilename();
            inputFile.transferTo(new File(request.getRealPath("/media/clip.mp4")));
        }
		model.addAttribute("message", "Hello " + modelBean.getName() + ", your audio file '" + fileName + "' was uploaded successfully.");

    }

	@RequestMapping(value = "/jsonmic", method=RequestMethod.POST)
	public @ResponseBody MicUpdate jsonMicrophone(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic-file", required = false) MultipartFile file, @RequestParam(value = "mic", required = false) MultipartFile inputFile, Model model) throws IOException {

        String fileName = "empty";
        String uuid = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32 );
        String newFileName = "/media/clip-" + uuid + ".mp4";
        if (null != file)  {
            fileName = file.getOriginalFilename();
            file.transferTo(new File(request.getRealPath(newFileName)));
        }
        if (null != inputFile)  {
            fileName = inputFile.getOriginalFilename();
            inputFile.transferTo(new File(request.getRealPath(newFileName)));
        }

        return new MicUpdate("Thanks for the sound, " + modelBean.getName(), 
                request.getContextPath() + newFileName);
    }

}

class MicUpdate  {
    private String clipLocation;
    private String message;

    public MicUpdate( String message, String clipLocation)  {
        this.clipLocation = clipLocation;
        this.message = message;
    }

    public String getClipLocation()  {
        return clipLocation;
    }

    public String getMessage()  {
        return message;
    }
}
