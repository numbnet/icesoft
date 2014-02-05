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

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.Resource;
import org.icemobile.application.ResourceStore;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("rawtypes")
public class ResourceStoreRequestHandler extends RangingResourceHttpRequestHandler{
    
    private static final Log LOG = LogFactory
            .getLog(ResourceStoreRequestHandler.class);
    
    private ResourceStore resourceStore;

    @PostConstruct
    public void init()  {
    }

    @Override
    protected Object[] getInputStreamAndContentLength(String requestPath)
        throws IOException{
        
        String uuid = requestPath.substring(requestPath.lastIndexOf("/")+1);
        
        LOG.debug("requestPath=" + requestPath + ", uuid=" + uuid);
        
        Resource theResource = resourceStore.get(uuid);
        if( theResource != null ){
            return new Object[]{
                    theResource.getInputStream(),
                    Long.valueOf(theResource.contentLength()),
                    theResource.getContentType()
                };
        }
        return null;
        
    }
    
    /**
     * Set the ResourceStore to use for request handling
     * 
     * @param store The ICEmobile ResourceStore
     */
    public void setResourceStore(ResourceStore store){
        this.resourceStore = store;
    }
}
