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

package org.icemobile.jsp.tags;

import java.util.logging.Logger;

import javax.servlet.jsp.JspException;

import org.icemobile.util.ClientDescriptor;

public class LargeViewTag extends BaseBodyTag {
    
    private static Logger LOG = Logger.getLogger(SmallViewTag.class.getName());
    
    public int doStartTag() throws JspException {
        ClientDescriptor client = getClient();
        if( !client.isHandheldBrowser()){
            return EVAL_BODY_INCLUDE;
        }
        else{
            return SKIP_BODY;
        }
    }
    
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
