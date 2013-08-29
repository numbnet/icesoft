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
package org.icemobile.samples.spring;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.icemobile.application.FileResource;
import org.icemobile.spring.handler.FileResourceAdapter;
import org.icemobile.util.Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.WebUtils;

public class ScaledImageFileResourceAdapter extends FileResourceAdapter {
    
    private static final int THUMBSIZE = 128;

    
    /**
     * Process the requests for any resources.
     * 
     * @param request The servlet request to process.
     * @return An array of FileResources from the request.
     */
    public FileResource[] handleRequest(HttpServletRequest request) {
        MultipartRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartRequest.class);
        List<FileResource> files = new ArrayList<FileResource>();
        if (multipartRequest != null) {
            Iterator<String> iter = multipartRequest.getFileNames();
            while( iter.hasNext() ){
                String fileName = iter.next();
                MultipartFile multipartFile = multipartRequest.getFile(fileName);
                if( !multipartFile.isEmpty() ){
                    try {
                        File newFile = File.createTempFile("icemobile-", 
                                Utils.FILE_EXT_BY_CONTENT_TYPE.get(multipartFile.getContentType()));
                        newFile.deleteOnExit();
                        FileOutputStream fos = new FileOutputStream(newFile);
                        fos.write(multipartFile.getBytes(), 0, multipartFile.getBytes().length);
                        fos.close();
                        scaleImage(newFile);
                        FileResource resource = new FileResource();
                        resource.setContentType(multipartFile.getContentType());
                        resource.setName(multipartFile.getName());
                        resource.setFile(newFile);
                        resource.setToken(request.getSession().getId());
                        resource.setUiid(UUID.randomUUID().toString());
                        files.add(resource);
                    } catch (IOException e) {
                       e.printStackTrace();
                    }
                }
            }
        }    
        FileResource[] fileArray = new FileResource[files.size()];
        return files.toArray(fileArray);
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
