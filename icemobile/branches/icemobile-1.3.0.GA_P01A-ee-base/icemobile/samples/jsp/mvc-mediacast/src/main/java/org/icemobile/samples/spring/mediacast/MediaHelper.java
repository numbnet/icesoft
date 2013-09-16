/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icemobile.samples.spring.mediacast;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Formatter;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;


/**
 * Helper class for processing and handling media files, mostly handler code best
 * kept out of the controller and model beans.
 *
 */
@Service
public class MediaHelper implements Serializable, ServletContextAware{
	
	private static final Log log = LogFactory
			.getLog(MediaHelper.class);
	
	private String videoConvertCommand;
	private String audioConvertCommand;
	private String thumbConvertCommand;
	
	private static final int SMALL_PHOTO_HEIGHT = 96;
	private static final int LARGE_PHOTO_HEIGHT = 320;
	
	private ServletContext servletContext;
	
	
	@PostConstruct
	public void init(){
		videoConvertCommand = servletContext
				.getInitParameter("org.icemobile.videoConvertCommand");

		audioConvertCommand = servletContext
				.getInitParameter("org.icemobile.audioConvertCommand");

		thumbConvertCommand = servletContext
				.getInitParameter("org.icemobile.thumbnailCommand");

		log.debug("video convert command: " + videoConvertCommand);
		log.debug("audio convert command: " + audioConvertCommand);
		log.debug("thumbnail convert command: " + thumbConvertCommand);
		
	}
	
	public File[] processSmallAndLargeImages(File image, String id) throws IOException{

			log.info("incoming photo file to be processed: " + image.getName());
			
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
	
	public File processSmallImage(File image, String id) throws IOException{
		BufferedImage bi = createCroppedBufferedImage(image);
		BufferedImage smallImage = processImage(bi, SMALL_PHOTO_HEIGHT);
		
		String dir = image.getParent();
		
		File smallImageFile = new File(dir+File.separator+id+"-small.png");
		ImageIO.write(smallImage, "png", smallImageFile);
		smallImage.flush();
		smallImage = null;
		return smallImageFile;
	}
	
	
	private BufferedImage createCroppedBufferedImage(File image) throws IOException{
		BufferedImage bi = ImageIO.read(image);
		// scale the original file into a small thumbNail and the other
		// into a 1 megapixelish sized image.
		int width = bi.getWidth();
		int height = bi.getHeight();
		log.debug("image original dimensions " + width+"x"+height);
		int xOffset = width > height ? (width - height) / 2 : 0; 			
		int yOffset = height > width ? (height - width) / 2 : 0; 
		int length = Math.min(width, height);
		//crop
		BufferedImage croppedBI = bi.getSubimage(xOffset, yOffset, length, length);
		bi.flush();
		bi = null;
		return croppedBI;
	}
	
	public BufferedImage processImage(BufferedImage bi, int height) throws IOException{
		AffineTransform tx = new AffineTransform();
		double imageScale = calculateImageScale(height, bi.getHeight());
		log.debug("scaling image "+bi.getWidth()+"x"+bi.getHeight()+" by " + imageScale);
		tx.scale(imageScale, imageScale);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage smallImage = op.filter(bi, null);
		return smallImage;
	}
	
	private double calculateImageScale(double intendedSize, double height) {
		double scaleHeight = height / intendedSize;
		return 1 / scaleHeight;
	}
	
	public void setServletContext(ServletContext ctx) {
		this.servletContext = ctx;
	}


}
