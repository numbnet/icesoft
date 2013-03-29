package org.icemobile.jsp.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;


/**
 * Helper class for processing and handling media files.
 *
 */
public class MediaHelper implements Serializable{

    private static final int SMALL_PHOTO_HEIGHT = 96;
    private static final int LARGE_PHOTO_HEIGHT = 320;

    public File[] processSmallAndLargeImages(File image, String id) throws IOException{

        String dir = image.getParent();

        BufferedImage croppedBI = createCroppedBufferedImage(image);

        BufferedImage smallImage = processImage(croppedBI, SMALL_PHOTO_HEIGHT);
        File smallImageFile = new File(dir+File.separator+id+"-small.png");
        ImageIO.write(smallImage, "png", smallImageFile);
        smallImage.flush();
        smallImage = null;

        BufferedImage largeImage = processImage(croppedBI, LARGE_PHOTO_HEIGHT);
        File largeImageFile = new File(dir+File.separator+id+"-large.png");
        ImageIO.write(largeImage, "png", largeImageFile);
        largeImage.flush();
        largeImage = null;

        croppedBI.flush();
        croppedBI = null;

        return new File[]{smallImageFile,largeImageFile};
    }

    public static File processSmallImage(File image, String id) throws IOException{
        BufferedImage bi = createCroppedBufferedImage(image);
        BufferedImage smallImage = processImage(bi, SMALL_PHOTO_HEIGHT);

        String dir = image.getParent();

        File smallImageFile = new File(dir+File.separator+id+"-small.png");
        ImageIO.write(smallImage, "png", smallImageFile);
        smallImage.flush();
        smallImage = null;
        return smallImageFile;
    }


    private static BufferedImage createCroppedBufferedImage(File image) throws IOException{
        BufferedImage bi = ImageIO.read(image);
        // scale the original file into a small thumbNail and the other
        // into a 1 megapixelish sized image.
        int width = bi.getWidth();
        int height = bi.getHeight();
        int xOffset = width > height ? (width - height) / 2 : 0; 			
        int yOffset = height > width ? (height - width) / 2 : 0; 
        int length = Math.min(width, height);
        //crop
        BufferedImage croppedBI = bi.getSubimage(xOffset, yOffset, length, length);
        bi.flush();
        bi = null;
        return croppedBI;
    }

    public static BufferedImage processImage(BufferedImage bi, int height) throws IOException{
        AffineTransform tx = new AffineTransform();
        double imageScale = calculateImageScale(height, bi.getHeight());
        tx.scale(imageScale, imageScale);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage smallImage = op.filter(bi, null);
        return smallImage;
    }

    private static double calculateImageScale(double intendedSize, double height) {
        double scaleHeight = height / intendedSize;
        return 1 / scaleHeight;
    }


}
