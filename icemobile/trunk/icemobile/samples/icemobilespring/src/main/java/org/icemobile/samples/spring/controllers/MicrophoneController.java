package org.icemobile.samples.spring.controllers;

import org.icemobile.samples.spring.ModelBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"microphoneBean","micUploadReady","micMessage","micUpload"})
public class MicrophoneController extends BaseController{

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
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.POST, consumes="multipart/form-data")
	public void processMicrophone(HttpServletRequest request, ModelBean modelBean, 
            @RequestParam(value = "mic", required = false) MultipartFile file, Model model) throws IOException {

        String fileName = saveClip(request, file);
        model.addAttribute("micUploadReady", null != fileName);
        model.addAttribute("micUpload", "media/" + fileName);
		model.addAttribute("micMessage", "Hello " + modelBean.getName() + ", your audio file was uploaded successfully.");

    }
	
	//non-file upload
	@RequestMapping(value = "/microphone", method=RequestMethod.POST)
    public void processMicrophoneP(Model model)  {
    }


	@RequestMapping(value = "/jsonmic", method=RequestMethod.POST)
	public @ResponseBody MicUpdate jsonMicrophone(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic", required = false) MultipartFile file, Model model) throws IOException {

        String newFileName = saveClip(request, file);

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
        return new MicUpdate( "Thanks for the sound, " + modelBean.getName() +
                " and your interest in [" + imcheck +
                "] ICEmobile and [" + jqcheck + "] jquery.", 
                request.getContextPath() + "/" + newFileName );
    }

    private String saveClip(HttpServletRequest request, MultipartFile file)
            throws IOException {

        String fileName = "audio-" + Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits()), 32) + ".m4a";
        if ((null != file) && !file.isEmpty()) {
            file.transferTo(new File(request.getRealPath("/media/" + fileName)));
        }
        return fileName;
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
