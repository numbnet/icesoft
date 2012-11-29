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
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MicrophoneController {

    String currentFileName = null;
    String currentUserName = null;

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
	public void processJqmmic(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic", required = false) MultipartFile file, Model model) throws IOException {
        this.processMicrophone(request, modelBean, file, model);
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.GET)
    public void processMicrophone(Model model)  {
        model.addAttribute("mediaReady", new Boolean(null != currentFileName));
        model.addAttribute("clipName", currentFileName);
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.POST, consumes="multipart/form-data")
	public void processMicrophone(HttpServletRequest request, ModelBean modelBean, 
            @RequestParam(value = "mic", required = false) MultipartFile file, Model model) throws IOException {

        String fileName = saveClip(request, file);
        model.addAttribute("mediaReady", new Boolean(null != currentFileName));
        model.addAttribute("clipName", currentFileName);
		model.addAttribute("message", "Hello " + modelBean.getName() + ", your audio file '" + fileName + "' was uploaded successfully.");

    }
	
	//non-file upload
	@RequestMapping(value = "/microphone", method=RequestMethod.POST)
    public void processMicrophoneP(Model model)  {
        model.addAttribute("mediaReady", new Boolean(null != currentFileName));
        model.addAttribute("clipName", currentFileName);
    }


	@RequestMapping(value = "/jsonmic", method=RequestMethod.POST)
	public @ResponseBody MicUpdate jsonMicrophone(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic", required = false) MultipartFile file, Model model) throws IOException {

        String newFileName = saveClip(request, file);
        String submittedName = modelBean.getName();
        if (null != submittedName)  {
            currentUserName = submittedName;
        }

        Map additionalParams = modelBean.getAdditionalInfo();
        String imcheck = " ";
        String jqcheck = " ";
        if (null != additionalParams)  {
            if (additionalParams.keySet().contains("icemobile"))  {
                imcheck = "*";
            }
            if (additionalParams.keySet().contains("jquery"))  {
                jqcheck = "*";
            }
        }
        return new MicUpdate( "Thanks for the sound, " + currentUserName +
                " and your interest in [" + imcheck +
                "] ICEmobile and [" + jqcheck + "] jquery.", 
                request.getContextPath() + "/" + newFileName );
    }

    private String saveClip(HttpServletRequest request, MultipartFile file)
            throws IOException {

        String fileName = null;
//        String uuid = Long.toString(
//                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
//        String newFileName = "media/clip-" + uuid + ".m4a";
        String newFileName = "media/clip-shared.m4a";
        if ((null != file) && !file.isEmpty()) {
            fileName = file.getOriginalFilename();
            file.transferTo(new File(request.getRealPath("/" + newFileName)));
            currentFileName = newFileName;
        }

        if (null == fileName) {
            //use previously uploaded file, such as from ICEmobile-SX
            newFileName = getCurrentFileName(request);

        }

        return newFileName;
    }

    private String getCurrentFileName(HttpServletRequest request) {
        if (null == currentFileName) {
            return "media/clip-0.m4a";
        }
        return currentFileName;
    }

}

class MicUpdate  {
    private String path;
    private String message;

    public MicUpdate( String message, String path)  {
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
