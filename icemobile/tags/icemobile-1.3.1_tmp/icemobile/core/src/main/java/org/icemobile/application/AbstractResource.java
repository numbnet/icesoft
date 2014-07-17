/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.application;

import java.util.Date;


public abstract class AbstractResource implements Resource{

    protected long id;
    protected String contentType;
    protected String token;
    protected String name;
    protected String uuid;
    protected long lastUpdated;
    
    /**
     * Get the content type for the resource.
     * 
     * @return the content-type String
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the content-type for the resource.
     * 
     * @param type The content-type
     */
    public void setContentType(String type) {
        this.contentType = type;        
    }
    
    /**
     * Get the unique id for the resource.
     * 
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the unique id for the resource.
     * 
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the resource name. The name corresponds to the name of 
     * the ICEmobile component name attribute.
     * 
     * @return the name of the resource
     */
    public String getName() {
        return name;
    }

    /**
     * Set the resource name. The name corresponds to the name of
     * the ICEmobile component name attribute.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the user token for the resource.
     * 
     * @return the user token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the user token for the resource. 
     * The user token should be unique to each user.
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Get the UUID for the resource.
     * 
     * @return The UUID.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Set the UUID for the resource.
     * 
     * @param uuid The UUID.
     */
    public void setUiid(String uuid) {
        this.uuid = uuid;
    }
    
    /**
     * Get the lastUpdated time (milliseconds since midnight, January 1, 1970 UTC)
     * 
     * @return the time the resource was last updated
     */
    public long getLastUpdated(){
        return lastUpdated;
    }
    
    /**
     * Set the lastUpdated time (milliseconds since midnight, January 1, 1970 UTC)
     * 
     * @param time the time the resource was last updated
     */
    public void setLastUpdated(long time){
        this.lastUpdated = time;
    }



}
