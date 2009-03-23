/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.faces.presenter.util;

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
    public static boolean aspectScaleImage(File toScale, int maxWidth, int maxHeight) {
        try{
            BufferedImage bsrc = ImageIO.read(toScale);
            
            // Determine if the image even needs to be scaled
            if ((bsrc.getWidth() <= maxWidth) &&
                (bsrc.getHeight() <= maxHeight)) {
            	return false;
            }
            
            // Set up the variables for calculating the aspect
            int ourWidth = bsrc.getWidth();
            int ourHeight = bsrc.getHeight();
            double aspectWidth = 1.0;
            double aspectHeight = 1.0;
            
            // Determine a proper aspect ratio
            if (ourWidth > ourHeight) {
                aspectWidth = (double)maxWidth/ourWidth;
                aspectHeight = aspectWidth;
            }
            else if (ourHeight > ourWidth) {
                aspectHeight = (double)maxHeight/ourHeight;
                aspectWidth = aspectHeight;
            }
            else {
                aspectWidth = (double)maxWidth/ourWidth;
                aspectHeight = (double)maxHeight/ourHeight;
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
