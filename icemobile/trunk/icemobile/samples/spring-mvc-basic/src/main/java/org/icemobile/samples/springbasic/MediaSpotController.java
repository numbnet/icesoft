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
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

@Controller
public class MediaSpotController {
    ArrayList<String> messages = new ArrayList();

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

    @ModelAttribute("mediaspotBean")
    public MediaSpotBean createBean() {
        return new MediaSpotBean();
    }

	@RequestMapping(value = "/mediaspot", method=RequestMethod.GET)
    public void processGet(Model model)  {
		model.addAttribute("reality", getRealityParams());
    }

	@RequestMapping(value = "/mediaspot", method=RequestMethod.POST)
	public void processPost(HttpServletRequest request, 
            @RequestParam(value = "camera", required = false) MultipartFile photoFile,
            MediaSpotBean spotBean,
            Model model) throws IOException {

        String newFileName = null;
        String fileName = "empty";
        if (null != photoFile)  {
            newFileName = saveMedia(request, "img-%1$s.jpg", photoFile);
            fileName = photoFile.getOriginalFilename();
            messages.add(spotBean.getPacked() + ",," + newFileName);
            scaleImage( new File(request.getRealPath("/" + newFileName)) );
        }
		model.addAttribute("reality", getRealityParams());
		model.addAttribute("message", "Hello your file '" + fileName + "' was uploaded successfully.");
        if (null != newFileName) {
            model.addAttribute("imgPath", newFileName);
        } else {
            model.addAttribute("imgPath", "resources/uploaded.jpg");
        }
    }

    private String saveMedia(HttpServletRequest request, String format,
                             MultipartFile file) throws
            IOException {

        String fileName = null;
        String uuid = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
        String newFileName = "media/" + String.format(format, uuid);
        if ((null != file) && !file.isEmpty()) {
            fileName = file.getOriginalFilename();
            file.transferTo(new File(request.getRealPath("/" + newFileName)));
            request.getServletContext().setAttribute(
                    this.getClass().getName(), newFileName );
        }

        if (null == fileName) {
            //use previously uploaded file, such as from ICEmobile-SX
            newFileName = getCurrentFileName(request);

        }

        return newFileName;
    }

    private String getCurrentFileName(HttpServletRequest request) {
        String currentName = (String) request.getServletContext().getAttribute(
                this.getClass().getName() );
        if (null == currentName) {
            return "resources/uploaded.jpg";
        }
        return currentName;
    }
    
    private String getRealityParams()  {
        StringBuilder sb = new StringBuilder();
        for (String location : messages)  {
            sb.append("&" + location);
        }
        return sb.toString();
    }

    private void scaleImage(File photoFile) throws IOException  {

        if (null == photoFile) {
            return;
        }

        BufferedImage image = ImageIO.read(photoFile);
        // scale the original file into a small thumbNail and the other
        // into a 1 megapixelish sized image.
        int width = image.getWidth();
        int height = image.getHeight();

        // create the thumbnail
        AffineTransform tx = new AffineTransform();
        double imageScale = calculateThumbNailSize(width, height);
        tx.scale(imageScale, imageScale);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage thumbNailImage = op.filter(image, null);

        // clean up the original image.
        image.flush();

        writeImage(thumbNailImage, photoFile);

    }

    private double calculateThumbNailSize(int width, int height) {
        double thumbSize = 48.0;
        return calculateImageSize(thumbSize, width, height);
    }

    private double calculateImageSize(double intendedSize, int width, int height) {
        double scaleHeight = height / intendedSize;
        // change the algorithm, so height is always the same
        return 1 / scaleHeight;
    }

    private void writeImage(BufferedImage image, File imageFile)
            throws IOException {
        FileOutputStream fs = new FileOutputStream(imageFile);
        ImageIO.write(image, "png", fs);
        fs.close();
    }

}
