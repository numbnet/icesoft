package org.icepush.integration.jsp.core;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.icepush.PushContext;

public class PushPeriodicTag extends TagSupport {

    protected String group;
    protected long interval;
    protected GroupIntervalTimer timer;

    @Override
    public int doStartTag() throws JspException {

	// Get timer;
	timer = (GroupIntervalTimer)pageContext.getServletContext().getAttribute("ICEpushJSPtimer");
	if (timer == null) {
	    throw(new JspException("GroupIntervalTimer must be configured as ServletContextListener."));
	}
	timer.setPushContext(PushContext.getInstance(pageContext.getServletContext()));
	    
	return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException{
	try {
	    timer.addGroup(group, interval);
	} catch (IllegalStateException e) {
	    throw new JspException(e.toString());
	}
	return EVAL_PAGE;
    }

    @Override
    public void release() {
	group = null;
	timer = null;
    }
    
    public String getGroup() {
	return group;
    }
    public void setGroup(String grp) {
	this.group = grp;
	} 
    public long getInterval() {
	return interval;
    }
    public void setInterval(long interval) {
	this.interval = interval;
    }
}
