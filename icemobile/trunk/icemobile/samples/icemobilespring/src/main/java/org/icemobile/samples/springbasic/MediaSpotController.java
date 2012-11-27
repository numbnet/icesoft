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
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

@Controller
@SessionAttributes({"augmentedRealityMessage", "augmentedRealityUpload"})
public class MediaSpotController {
    HashMap<String,MediaSpotBean> messages = new HashMap<String,MediaSpotBean>();
    MediaSpotBean selectedMessage = null;
    static int THUMBSIZE = 128;
    int count = 0;
    String currentFileName = null;

    private String selectedModel = "icemobile";

    private List getMarkerList(HttpServletRequest request)  {

        String urlBase = request.getScheme() + "://" +
                request.getServerName() + ":" + request.getServerPort() +
                request.getContextPath();
        
        ArrayList markerList = new ArrayList();
        HashMap marker;

        marker = new HashMap();
        marker.put("label", "icemobile");
        marker.put("model", urlBase + "/resources/3d/icemobile.obj" );
        markerList.add(marker);

        marker = new HashMap();
        marker.put("label", "puz1");
        marker.put("model", urlBase + "/resources/3d/puz1.obj" );
        markerList.add(marker);

        marker = new HashMap();
        marker.put("label", "puz2");
        marker.put("model", urlBase + "/resources/3d/puz2.obj" );
        markerList.add(marker);

        return markerList;
    }

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

    @ModelAttribute("mediaspotBean")
    public MediaSpotBean createBean() {
        return new MediaSpotBean();
    }
    
    @ModelAttribute
    public void iosAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("ios", ClientDescriptor.getInstance(request).isIOS());
    }

	@RequestMapping(value = "/mediaspot", method=RequestMethod.GET)
    public void processGet(HttpServletRequest request, Model model)  {
		model.addAttribute("locations", messages.values());
		model.addAttribute("markers", getMarkerList(request));
        if (null != selectedMessage) {
            model.addAttribute("selection", selectedMessage.getTitle());
            model.addAttribute("imgPath", 
                    selectedMessage.getFileName());
        }
    }

	@RequestMapping(value = "/mediaspot", method=RequestMethod.POST, consumes="multipart/form-data")
	public void processPost(HttpServletRequest request, 
            @RequestParam(value = "spotcam", required = false) MultipartFile photoFile,
            MediaSpotBean spotBean,
            Model model)  {
	    System.out.println("processPost() photoFile="+photoFile+", bean="+spotBean);
        String newFileName = null;
        try {
            if (null != photoFile)  {
                newFileName = FileUploadUtils.saveImage(request, photoFile, null);
                
                System.out.println("newFileName="+newFileName);
                spotBean.setFileName(newFileName);
                String title = spotBean.getTitle();
                if ((null == title) || "".equals(title))  {
                    spotBean.setTitle("Marker" + count++);
                }
                messages.put(spotBean.getTitle(), spotBean);
                System.out.println("scaling image");
                scaleImage( new File(request.getRealPath("/" + newFileName)) );
            }
    		model.addAttribute("locations", messages.values());
    		model.addAttribute("augmentedRealityMessage", "Hello your file was uploaded successfully, you may now enter augmented reality.");
    		model.addAttribute("augmentedRealityUpload", newFileName);
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
        } catch (IOException e) {
            model.addAttribute("augmentedRealityMessage", "Sorry, there was a problem processing the image upload.");
            e.printStackTrace();
        }
    }
	
	//non-file upload
	@RequestMapping(value = "/mediaspot", method=RequestMethod.POST)
    public void processPost(Model model)  {
        model.addAttribute("locations", messages.values());
        if (null != selectedMessage) {
            model.addAttribute("selection", selectedMessage.getTitle());
            model.addAttribute("imgPath", 
                    selectedMessage.getFileName());
        }
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
