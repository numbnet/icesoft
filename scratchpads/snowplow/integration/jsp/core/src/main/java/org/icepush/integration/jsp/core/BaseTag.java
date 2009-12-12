package org.icepush.integration.jsp.core;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.icepush.PushContext;
import org.icepush.integration.common.notify.Notifier;
import org.icepush.integration.common.notify.GroupNotifier;

public class BaseTag extends TagSupport {

    protected String group;
    protected String notifier;
    protected String pushid;

    @Override
    public int doStartTag() throws JspException {

	// Get a push id;
	final PushContext pc = PushContext.getInstance(pageContext.getServletContext());
	if (pc == null) {
	    throw(new JspException("PushContext not available in BaseTag.doStartTag()"));
	}
	pushid = pc.createPushId((HttpServletRequest)pageContext.getRequest(),
				 (HttpServletResponse)(pageContext.getResponse()));
	    
	// Find the notifier bean;
	Notifier notifierBean = null;
	if (notifier != null) {
	    notifierBean = (Notifier)pageContext.findAttribute(notifier);
	    if (notifierBean != null) {
		notifierBean.setPushContext(pc);
	    } else {
		throw( new JspException("Could not find notifier bean " + notifier));
	    } 
	} 

	// Set group if there is one;
	if (group != null) {
	    pc.addGroupMember(group, pushid);
	} else {
	    group = pushid;
	}
	try {
	    // Set group in notifier;
	    GroupNotifier gnotifier = (GroupNotifier)notifierBean;
	    gnotifier.setGroup(group);
	} catch (ClassCastException e) {
	}

    return SKIP_BODY;
    }

    @Override
    public void release() {
	group = null;
	notifier = null;
	pushid = null;
    }
    
    public String getGroup() {
	return group;
    }
    public void setGroup(String grp) {
	this.group = grp;
	} 
    public String getNotifier() {
	return notifier;
    }
    public void setNotifier(String notifier) {
	this.notifier = notifier;
    }
}
