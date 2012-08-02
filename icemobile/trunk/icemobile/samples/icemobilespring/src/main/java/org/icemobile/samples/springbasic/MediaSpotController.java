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
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

@Controller
public class MediaSpotController {
    HashMap<String,MediaSpotBean> messages = new HashMap();
    MediaSpotBean selectedMessage = null;
    static int THUMBSIZE = 128;
    int count = 0;
    String currentFileName = null;

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
		model.addAttribute("locations", messages.values());
        if (null != selectedMessage) {
            model.addAttribute("selection", selectedMessage.getTitle());
            model.addAttribute("imgPath", 
                    selectedMessage.getFileName());
        }
    }

	@RequestMapping(value = "/mediaspot", method=RequestMethod.POST)
	public void processPost(HttpServletRequest request, 
            @RequestParam(value = "spotcam", required = false) MultipartFile photoFile,
            MediaSpotBean spotBean,
            Model model) throws IOException {

        String newFileName = null;
        String fileName = "empty";
        if (null != photoFile)  {
            newFileName = saveMedia(request, "img-%1$s.jpg", photoFile);
            fileName = photoFile.getOriginalFilename();
            spotBean.setFileName(newFileName);
            String title = spotBean.getTitle();
            if ((null == title) || "".equals(title))  {
                spotBean.setTitle("Marker" + count++);
            }
            messages.put(spotBean.getTitle(), spotBean);
            scaleImage( new File(request.getRealPath("/" + newFileName)) );
        }
		model.addAttribute("locations", messages.values());
		model.addAttribute("message", "Hello your file '" + fileName + "' was uploaded successfully.");
        String selection = spotBean.getSelection();
        MediaSpotBean mySelectedMessage = messages.get(selection);
        if (null != mySelectedMessage) {
            selectedMessage = mySelectedMessage;
        }
        if (null != selectedMessage) {
            model.addAttribute("selection", selectedMessage.getTitle());
            model.addAttribute("imgPath", 
                    selectedMessage.getFileName());
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
            return "resources/uploaded.jpg";
        }
        return currentFileName;
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
        //default image type creates nonstandard all black jpg file
        BufferedImage thumbNailImage = 
                new BufferedImage(THUMBSIZE, THUMBSIZE, 
                        BufferedImage.TYPE_3BYTE_BGR);
        double imageScale = calculateImageScale(THUMBSIZE, width, height);
        tx.scale(imageScale, imageScale);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(image, thumbNailImage);

        // clean up the original image.
        image.flush();

        writeImage(thumbNailImage, photoFile);

    }

    private double calculateImageScale(double intendedSize, int width, int height) {
        double scaleHeight = height / intendedSize;
        // change the algorithm, so height is always the same
        return 1 / scaleHeight;
    }

    private void writeImage(BufferedImage image, File imageFile)
            throws IOException {
        FileOutputStream fs = new FileOutputStream(imageFile);
        ImageIO.write(image, "jpg", fs);
        fs.close();
    }

}
