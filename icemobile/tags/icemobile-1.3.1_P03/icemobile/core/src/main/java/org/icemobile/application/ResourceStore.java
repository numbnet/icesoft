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

import javax.servlet.http.HttpServletRequest;


public interface ResourceStore<T extends Resource>{
    
    public void remove(T resource);
    public void remove(String token, String key);
    public void add(T resource);
    public void handleRequest(HttpServletRequest request, String token);
    public void handleInputStream(InputStream is, String contentType, String name, String token);
    public void clear();
    public T get(String token, String key);
    public T get(String uuid);
    public void setResourceAdaptor(ResourceAdapter adapter);
    

}
