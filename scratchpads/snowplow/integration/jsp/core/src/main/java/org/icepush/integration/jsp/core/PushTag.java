package org.icepush.integration.jsp.core;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.icepush.PushContext;

public class PushTag extends TagSupport {

    protected String group;

    @Override
    public int doStartTag() throws JspException {

	final PushContext pc = PushContext.getInstance(pageContext.getServletContext());
	if (pc == null) {
	    throw(new JspException("PushContext not available in PushTag.doStartTag()"));
	}
	System.out.println("Pushing to " + group);
	pc.push(group);
	    
	return SKIP_BODY;
    }

    @Override
    public void release() {
	group = null;
    }
    
    public String getGroup() {
	return group;
    }
    public void setGroup(String grp) {
	this.group = grp;
	} 
}
