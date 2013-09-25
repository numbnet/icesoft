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

package org.icemobile.samples.mediacast;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/upload/*"})
@MultipartConfig
public class UploadServlet extends HttpServlet {
    static String TEMP_DIR = "javax.servlet.context.tmpdir";
    
    private static final Logger LOG = Logger.getLogger(UploadServlet.class.toString());

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	LOG.info("incoming upload");

        ServletContext servletContext = request.getServletContext();
        try {
            for (Part part : request.getParts()) {
                String partType = part.getContentType();
                String partName = part.getName();
                String fileName = getAppropriateFileName(partType);
                if (null != fileName) {
                    String dirPath = servletContext
                            .getRealPath("/images") + "/";
                    File dirFile = new File(dirPath);
                    if (!dirFile.exists()) {
                        dirFile.mkdir();
                    }
                    String fullPath = dirPath + fileName;
                    writePart(part, fullPath);
                    UploadModel uploadModel = (UploadModel)request.getSession(true)
                    			.getAttribute("uploadModel");
                    if( uploadModel == null ){
                    	uploadModel = new UploadModel();
                    }
                    String title;
                    String description;
                    //multiple uploads from offline.html are named cam
                    if (partName.startsWith("cam"))  {
                        String itemIndex = partName.substring(3);
                        title = request.getParameter("title" + itemIndex);
                        description = request.getParameter("description" + itemIndex);
                    } else {
                        //otherwise a single upload
                        title = request.getParameter("title");
                        description = request.getParameter("description");
                    }
                    uploadModel.getCurrentMediaMessage().setTitle(title);
                    uploadModel.getCurrentMediaMessage().setDescription(description);
                    Map uploadAttributes = new HashMap();
                    uploadAttributes.put("file", new File(fullPath));
                    uploadAttributes.put("contentType", partType);
                    uploadModel.setPhotoUploadMap(uploadAttributes);
                    uploadModel.setVideoUploadMap(uploadAttributes);
                    uploadModel.setAudioUploadMap(uploadAttributes);
                    uploadModel.setSelectedMediaInput(
                            getAppropriateMediaType(partType));
                    MediaStore mediaStore = (MediaStore)
                        servletContext.getAttribute(MediaStore.BEAN_NAME);
                    MediaController.processUpload(uploadModel, mediaStore);
                    MediaController.uploadsCompleted(uploadModel, mediaStore);
                }
            }
        } catch (Exception e) {
            //ignoring part decoding Exceptions
        	LOG.warning(e.getMessage());
        	e.printStackTrace();
        }
        
        LOG.info("upload finished ... redirecting back to mediacast");

        response.sendRedirect(request.getContextPath() + "/mediacast.jsf");
    }

    static String getAppropriateMediaType(String contentType)  {
        if (null == contentType)  {
            return null;
        }
        if (contentType.startsWith("image")) {
            return MediaMessage.MEDIA_TYPE_PHOTO;
        }
        if (contentType.startsWith("video"))  {
            return MediaMessage.MEDIA_TYPE_VIDEO;
        }
        if (contentType.startsWith("audio")) {
            return MediaMessage.MEDIA_TYPE_AUDIO;
        }
        return null;
    }

    static String getAppropriateFileName(String contentType)  {
        if (null == contentType)  {
            return null;
        }
        String baseName = String.valueOf(System.currentTimeMillis());
        if (contentType.startsWith("image")) {
            return baseName + ".jpg";
        }
        if (contentType.startsWith("video"))  {
            return baseName + ".mp4";
        }
        if (contentType.startsWith("audio")) {
            return baseName + ".mp4";
        }
        return null;
    }

    void writePart(Part part, String path) throws IOException {
        File tempDir = (File) ( getServletContext().getAttribute(TEMP_DIR) );
        File tempFile = File.createTempFile("ice", ".tmp", tempDir);
        FileOutputStream tempStream = new FileOutputStream(tempFile);

        InputStream partStream = part.getInputStream();
        copyStream(partStream, tempStream);
        tempFile.renameTo(new File(path));
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1000];
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                out.write(buf, 0, l);
            }
        }
    }

}
