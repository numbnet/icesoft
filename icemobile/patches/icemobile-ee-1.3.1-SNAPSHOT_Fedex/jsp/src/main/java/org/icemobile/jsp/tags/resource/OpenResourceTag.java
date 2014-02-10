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

package org.icemobile.jsp.tags.resource;


import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.OpenResourceCoreRenderer;
import org.icemobile.util.ClientDescriptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.logging.Logger;

public class OpenResourceTag extends OpenResourceBaseTag {
    private static final Logger logger = Logger.getLogger(OpenResourceTag.class.getName());
    private TagWriter writer;
    private OpenResourceCoreRenderer renderer;

    protected HttpServletRequest getRequest(){
    	return (HttpServletRequest)getContext().getRequest();
    }

    protected HttpServletResponse getResponse(){
    	return (HttpServletResponse)getContext().getResponse();
    }
   protected PageContext getContext(){
    	return (PageContext)getJspContext();
    }
    protected String getContextRoot(){
        return getRequest().getContextPath();
    }

    public ClientDescriptor getClient(){
        return ClientDescriptor.getInstance(getRequest());
    }

    public String getClientId(){
        return this.getId();
    }

    public void doTag() throws IOException {
        writer = new TagWriter(getContext());
        renderer = new OpenResourceCoreRenderer();
        renderer.encodeEnd(this, writer);
    }

    public void release(){
        this.srcAttribute=null;
        renderer=null;
        writer = null;
    }

}