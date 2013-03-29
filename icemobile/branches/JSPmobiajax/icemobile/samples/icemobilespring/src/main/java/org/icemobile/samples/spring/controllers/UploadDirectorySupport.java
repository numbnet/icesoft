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
package org.icemobile.samples.spring.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.icemobile.application.Resource;
import org.icemobile.util.IOUtils;
import org.icemobile.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadDirectorySupport {
    
    @Autowired
    protected ServletContext servletContext;
    
    public String addMediaToPublicDir(Resource resource) 
            throws IOException{
        String url = null;
        if (null != resource) {
            
            File publicFile = new File(servletContext.getRealPath("/media/" 
                    + UUID.randomUUID() 
                    + Utils.FILE_EXT_BY_CONTENT_TYPE.get(resource.getContentType())));
            FileOutputStream fos = new FileOutputStream(publicFile);
            IOUtils.copyStream(resource.getInputStream(), fos);
            fos.close();
            resource.getInputStream().close();
            url = "media/" + publicFile.getName();
        }
        return url;
    }

}
