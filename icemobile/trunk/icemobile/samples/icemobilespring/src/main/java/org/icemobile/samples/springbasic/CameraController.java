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

import org.icemobile.util.ClientDescriptor;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"cameraMessage", "cameraUpload"})
public class CameraController {

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }
    
    @ModelAttribute
    public void iosAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("ios", ClientDescriptor.getInstance(request).isIOS());
    }

    @RequestMapping(value = "/camera", 
            headers="content-type=application/x-www-form-urlencoded")
    public void fileUploadFormPost(HttpServletRequest request, Model model) {
        fileUploadForm(request, model);
    }

    @RequestMapping(value = "/camera", method = RequestMethod.GET)
    public void fileUploadForm(HttpServletRequest request, Model model) {
        model.addAttribute("isGET", Boolean.TRUE);
    }

    @ModelAttribute("cameraBean")
    public ModelBean createBean() {
        return new ModelBean();
    }

    @RequestMapping(value = "/camera", method = RequestMethod.POST)
    public void processUpload(
            HttpServletRequest request, ModelBean modelBean,
            @RequestParam(value = "cam", required = false) MultipartFile file,
            Model model) throws IOException {
        
        String newFileName = FileUploadUtils.saveImage(request, file, null);
        if ((null != file) && !file.isEmpty()) {
            model.addAttribute("cameraMessage", "Hello " + modelBean.getName() + ", your file '" + newFileName + "' was uploaded successfully.");
        }
        if (null != newFileName) {
            model.addAttribute("cameraUpload", newFileName);
        } 
    }

    @RequestMapping(value = "/jsoncam", method = RequestMethod.POST)
    public
    @ResponseBody
    CamUpdate jsonCamera(HttpServletRequest request, ModelBean modelBean,
                         @RequestParam(value = "camera-file", required = false) MultipartFile file,
                         @RequestParam(value = "cam", required = false) MultipartFile inputFile,
                         Model model) throws IOException {

        String newFileName = FileUploadUtils.saveImage(request, file, inputFile);
        
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

        return new CamUpdate( "Thanks for the photo, " + modelBean.getName() +
                " and your interest in [" + imcheck +
                "] ICEmobile and [" + jqcheck + "] jquery.", 
                request.getContextPath() + "/" + newFileName );
    }
}

class CamUpdate {
    private String path;
    private String message;

    public CamUpdate(String message, String path) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
