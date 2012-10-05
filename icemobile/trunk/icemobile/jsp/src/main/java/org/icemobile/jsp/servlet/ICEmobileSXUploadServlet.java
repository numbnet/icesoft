/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
package org.icemobile.jsp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.icemobile.jsp.util.MediaHelper;
import org.icemobile.util.SXUtils;
import org.icemobile.util.Utils;

/**
 * Handle the ICEmobile-SX auxiliary upload posts. 
 * 
 * The servlet will register the SX session key, as well as handle uploads.
 * This Servlet is automatically mapped to /icemobile/* for Servlet 3.0 
 * and must be manually mapped to that URL pattern in web.xml for 
 * Servlet 2.5 containers. The Servlet will handle
 * the post made by the SX container and set a session attribute that the
 * application can inspect to check if the user has registered SX.  
 *
 */
//@WebServlet(name="ICEmobileSXUploadServlet", urlPatterns = {"/icemobile/*"}, loadOnStartup=1)
public class ICEmobileSXUploadServlet extends HttpServlet {


    private static final long serialVersionUID = 9038716950477146607L;

    private static Logger log = Logger.getLogger(ICEmobileSXUploadServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ICEmobileSXUploadServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String thumbId = request.getParameter("thumbId");
        if( thumbId != null ){
            serveThumbnail(request, response, thumbId);
        }
        
    }
    
    private void serveThumbnail(HttpServletRequest request, HttpServletResponse response, String thumbId) throws ServletException, IOException {
        File thumb = (File)SXUtils.getSXUploadThumbMap(request).get(thumbId);
        System.out.println("thumb="+thumb);
        if( thumb != null ){
            String suffix = thumb.getName().substring(thumb.getName().lastIndexOf("."));
            response.setContentType(Utils.CONTENT_TYPE_BY_FILE_EXT.get(suffix));
            ServletOutputStream output = response.getOutputStream();
            InputStream input =  null;
            try { 
                input = new FileInputStream(thumb);
                byte[] buffer = new byte[2048];
                int bytesRead;    
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            finally {
                output.close();
                if (input != null){
                    input.close();
                }
            }           
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("incoming sx servlet request");

        if( SXUtils.isSXRequest(request) ){
            System.out.println("sx post");
            if(!SXUtils.isSXRegistered(request)){
                SXUtils.setSXSessionKeys(request);
            }
            else{
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if( isMultipart ){
                    System.out.println("sx upload");
                    final HttpSession session = request.getSession();
                    //start the progress off at 1
                    SXUtils.setAuxUploadProgress(1, session);

                    FileCleaningTracker fileCleaningTracker = FileCleanerCleanup
                            .getFileCleaningTracker(request.getServletContext());
                    DiskFileItemFactory factory = new DiskFileItemFactory();
                    factory.setFileCleaningTracker(fileCleaningTracker);
                    ServletFileUpload upload = new ServletFileUpload(factory);

                    @SuppressWarnings("unused")
                    final ProgressListener progressListener = new ProgressListener(){
                        private long megaBytes = -1;
                        public void update(long pBytesRead, long pContentLength, int pItems) {
                            long mBytes = pBytesRead / 1000000;
                            if (megaBytes == mBytes) {
                                return;
                            }
                            megaBytes = mBytes;
                            if (pContentLength != -1) {
                                int progress = Math.round(pBytesRead/pContentLength);
                                SXUtils.setAuxUploadProgress(progress, session);
                            }
                        }
                    };
                    Map<String,File> auxMap = SXUtils.getAuxiliaryUploadMap(request);
                    try {
                        @SuppressWarnings("unchecked")
                        List<FileItem> items = (List<FileItem>)upload.parseRequest(request);
                        Iterator<FileItem> iter = items.iterator();
                        while (iter.hasNext()) {
                            FileItem item = (FileItem) iter.next();
                            if (!item.isFormField()) {
                                String contentType = item.getContentType();
                                String id = item.getFieldName();
                                String prefix = Utils.FILE_EXT_BY_CONTENT_TYPE.get(contentType);
                                File file = File.createTempFile("sx-upload", prefix);
                                file.deleteOnExit();
                                try {
                                    item.write(file);
                                    auxMap.put(id, file);
                                    if( contentType != null && contentType.startsWith("image")){
                                        File thumb = MediaHelper.processSmallImage(file, id);
                                        thumb.deleteOnExit();//TODO cleanup listener
                                        SXUtils.getSXUploadThumbMap(request).put(id, thumb);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } 
                        }
                    } catch (FileUploadException e) {
                        e.printStackTrace();
                    }
                    request.getSession().setAttribute(SXUtils.SX_UPLOAD, auxMap);
                    SXUtils.setAuxUploadProgress(0, session);
                }
            }
        }
    }



}
