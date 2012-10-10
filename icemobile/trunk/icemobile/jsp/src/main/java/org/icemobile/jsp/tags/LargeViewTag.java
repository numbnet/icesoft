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
            return EVAL_PAGE;
        }
    }
    
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
