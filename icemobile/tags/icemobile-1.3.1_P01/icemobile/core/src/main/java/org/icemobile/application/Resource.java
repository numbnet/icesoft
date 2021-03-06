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

import java.io.InputStream;

public interface Resource {
    
    public String getContentType();
    public void setContentType(String contentType);
    
    public long getId();
    public void setId(long id);
    
    public String getName();
    public void setName(String name);
    
    public InputStream getInputStream();
    public void setInputStream(InputStream stream);
    
    public String getToken();
    public void setToken(String token);
    
    public String getUuid();
    public void setUiid(String uuid);
    
    public long getLastUpdated();
    public void setLastUpdated(long date);
    
    public long contentLength();
    
    public void delete();
    
    public void setStore(ResourceStore store);
    

}
