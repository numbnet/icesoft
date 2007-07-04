package com.icesoft.faces.presenter.util;

import com.icesoft.faces.presenter.slide.Slide;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to scale images so they fit properly into the WebMC viewer
 */
public class ImageScaler
{
    private static Log log = LogFactory.getLog(ImageScaler.class);
    
    /**
     * Method to downscale an image while maintaining the original aspect ratio
     * This uses the Java2D AffineTransform method
     *
     *@param toScale image file (assumed to be valid)
     *@return true on successful scaling, false otherwise (or if not needed)
     */
    public static boolean aspectScaleImage(File toScale) {
        try{
            BufferedImage bsrc = ImageIO.read(toScale);
            
            // Determine if the image even needs to be scaled
            if ((bsrc.getWidth() <= Slide.MAX_WIDTH) &&
                (bsrc.getHeight() <= Slide.MAX_HEIGHT)) {
                return false;
            }
            
            // Set up the variables for calculating the aspect
            int ourWidth = bsrc.getWidth();
            int ourHeight = bsrc.getHeight();
            double aspectWidth = 1.0;
            double aspectHeight = 1.0;
            
            // Determine a proper aspect ratio
            if (ourWidth > ourHeight) {
                aspectWidth = (double)Slide.MAX_WIDTH/ourWidth;
                aspectHeight = aspectWidth;
            }
            else if (ourHeight > ourWidth) {
                aspectHeight = (double)Slide.MAX_HEIGHT/ourHeight;
                aspectWidth = aspectHeight;
            }
            else {
                aspectWidth = (double)Slide.MAX_WIDTH/ourWidth;
                aspectHeight = (double)Slide.MAX_HEIGHT/ourHeight;
            }
            
            // Setup the destination image file to override the original
            BufferedImage bdest = new BufferedImage((int)(ourWidth*aspectWidth), (int)(ourHeight*aspectHeight),
                                                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bdest.createGraphics();
            
            // Perform the scale operation on the image
            AffineTransform at = AffineTransform.getScaleInstance(aspectWidth, aspectHeight);
            
            // Render and rewrite the image
            g.drawRenderedImage(bsrc, at);
            ImageIO.write(bdest, "JPG", toScale);
            
            return true;
        }catch (Exception scaleFailed) {
            if (log.isErrorEnabled()) {
                log.error("Failed to scale the image '" + toScale + "'", scaleFailed);
            }
        }
        
        return false;
    }
}
