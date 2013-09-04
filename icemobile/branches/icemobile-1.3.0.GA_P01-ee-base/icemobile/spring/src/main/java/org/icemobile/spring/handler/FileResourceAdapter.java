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
package org.icemobile.spring.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.application.FileResource;
import org.icemobile.application.ResourceAdapter;
import org.icemobile.util.IOUtils;
import org.icemobile.util.Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.WebUtils;

/**
 * A ResourceAdapter to process resources into FileResources.
 * 
 * @see FileResource
 * @see ResourceAdapter
 *
 */
public class FileResourceAdapter implements ResourceAdapter<FileResource>{

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

    /**
     * Process an InputStream for a resource.
     * 
     * @param is The InputStream to process.
     * @param contentType The content-type (mime-type) of the Resource
     * @return The generated FileResource.
     */
    public FileResource handleInputStream(InputStream is, String contentType) {
        File newFile;
        FileResource resource = null;
        FileOutputStream fos = null;
        try {
            newFile = File.createTempFile("icemobile-", 
                    Utils.FILE_EXT_BY_CONTENT_TYPE.get(contentType));
            newFile.deleteOnExit();
            fos = new FileOutputStream(newFile);
            IOUtils.copyStream(is, fos);
            resource = new FileResource();
            resource.setContentType(contentType);
            resource.setFile(newFile);
            resource.setUiid(UUID.randomUUID().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if( fos != null ){
                    fos.close();
                }
                if( is != null ){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return resource;
    }

}
