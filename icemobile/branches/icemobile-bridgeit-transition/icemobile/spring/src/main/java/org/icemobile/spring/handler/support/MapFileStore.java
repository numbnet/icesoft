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
package org.icemobile.spring.handler.support;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.application.FileResource;
import org.icemobile.application.ResourceAdapter;
import org.icemobile.application.ResourceStore;
import org.icemobile.spring.handler.FileResourceAdapter;

/**
 * A simple map-based ResourceStore of FileResources. 
 * Resources are keyed to the maps of the user-token, 
 * each of which contain a map of the Resources by name,
 * as well as a global map keyed by uuid.
 *
 */
public class MapFileStore implements ResourceStore<FileResource> {
    
    protected Map<String,Map<String, FileResource>> wrappedMap;
    protected Map<String,FileResource> uuidMap;
    protected ResourceAdapter<FileResource> resourceAdapter = new FileResourceAdapter();
    
    /**
     * Create a MapFileStore with a wrapped HashMap.
     */
    public MapFileStore(){
        wrappedMap = new HashMap<String, Map<String,FileResource>>();
        uuidMap = new HashMap<String,FileResource>();
    }

    /**
     * Remove the FileResource from the ResourceStore.
     * 
     * @param resource The FileResource to remove.
     * @see FileResource
     */
    public void remove(FileResource resource) {
        
        if( resource != null ){
            //remove the resource from the session map
            Map<String,FileResource> sessionMap = getSessionMap(resource.getToken());
            Iterator<String> iter = sessionMap.keySet().iterator();
            while( iter.hasNext() ){
                String key = iter.next();
                if( sessionMap.get(key).equals(resource)){
                    sessionMap.remove(key);
                    break;
                }
            }
            //also remove the resource from the uuid map
            if( resource.getUuid() != null ){
                uuidMap.remove(resource.getUuid());
            }
        }
    }
    
    private Map<String,FileResource> getSessionMap(String token){
        Map<String,FileResource> sessionMap = wrappedMap.get(token);
        if( sessionMap == null ){
            sessionMap = new HashMap<String,FileResource>();
            synchronized(this.wrappedMap){
                this.wrappedMap.put(token, sessionMap);
            }
        }
        return sessionMap;
    }

    /**
     * Remove a FileResource from the ResourceStore based on the unique
     * combination of user token and resource name.
     * 
     * @param token The user token
     * @param name The resource name
     */
    public void remove(String token, String name) {
        FileResource resource = getSessionMap(token).remove(name);
        if( resource != null && resource.getUuid() != null ){
            uuidMap.remove(resource.getUuid());
        }
    }

    /**
     * Add a FileResource to the ResourceStore.
     * 
     * @param The FileResource to add.
     * @see FileResource
     */
    public void add(FileResource resource) {
        if( resource != null ){
            //only add it to the session map if there's a token
            if( resource.getToken() != null ){
                Map<String,FileResource> sessionMap = getSessionMap(resource.getToken());
                sessionMap.put(resource.getName(), resource);
            }
            
            //add it to the uuid map only if there's a uuid
            if( resource.getUuid() != null ){
                uuidMap.put(resource.getUuid(), resource);
            }
        }
        
    }

    /**
     * Remove all Resources from the ResourceStore.
     */
    public void clear() {
        this.wrappedMap.clear();
        this.uuidMap.clear();
    }

    /**
     * Get a FileResource by user token and resource name.
     * 
     * @param token The user token.
     * @param name The resource name.
     * @return The FileResource from the ResourceStore
     */
    public FileResource get(String token, String name) {
        return getSessionMap(token).get(name);
    }

    /**
     * Retrieve all resources from a request and add them to the ResourceStore.
     * 
     * @param request The servlet request to parse.
     * @param token The user token.
     */
    public FileResource[] handleRequest(HttpServletRequest request, String token) {
        FileResource[] resources = null;
        if( resourceAdapter != null ){
            resources = resourceAdapter.handleRequest(request);
        }
        if( resources != null ){
            for( FileResource resource : resources ){
                resource.setToken(token);
                resource.setStore(this);
                this.add(resource);
            }
        }
        return resources;
    }

    /**
     * 
     * @param adapter The ResourceAdapter
     */
    public void setResourceAdaptor(ResourceAdapter adapter) {
        this.resourceAdapter = adapter;
    }

    /**
     * Retrieve the Resource from the InputStream and add it to the ResourceStore.
     * 
     * @param is The InputStream to process.
     * @param contentType The content-type (mime-type) of the Resource.
     * @param name The Resource name.
     * @param token The user token. 
     */
    public void handleInputStream(InputStream is, String contentType, String name, String token) {
        if( resourceAdapter != null ){
            FileResource resource = resourceAdapter.handleInputStream(is, contentType);
            resource.setName(name);
            resource.setToken(token);
            resource.setStore(this);
            this.add(resource);
        }
    }

    /**
     * Get the FileResource by uuid.
     * 
     * Note that as all wrapped session have to be
     * searched, this 
     * 
     * @param the uuid of the resource
     */
    public FileResource get(String uuid) {
        return uuidMap.get(uuid);
    }

}
