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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/** Tag class for the DataTable detail section */
public class DataTableDetailTag extends BodyTagSupport {
    
    public void setParent(Tag parent) {
        if (!(parent instanceof DataTableTag)) {
            throw new IllegalArgumentException("dataTableDetail must be child of dataTable");
        }
        super.setParent(parent);
    }

    @Override
    public int doStartTag() throws JspException {
        if( ((DataTableTag)getParent()).getRenderedDetailView() == null ){
            return EVAL_BODY_BUFFERED;
        }
        else{
            return SKIP_BODY;
        }
    }
    
    @Override
    public int doAfterBody() throws JspException {
        BodyContent body = this.getBodyContent();
        try {
            if (body!=null){
                ((DataTableTag)getParent()).setRenderedDetailView(body.getString());
                body.clearBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

}
