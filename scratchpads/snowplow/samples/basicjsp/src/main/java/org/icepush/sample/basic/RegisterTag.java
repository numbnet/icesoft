package org.icepush.sample.basic;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.InterruptedException;
import org.icepush.PushContext;

public class RegisterTag extends TagSupport {

    private String group;
    private String notifier;
    private String callback;

    @Override
	public int doStartTag() throws JspException {

	try {
	    //Get the writer object for output.
	    JspWriter w = pageContext.getOut();

	    // Get a push id;
	    final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
	    PushContext pc = PushContext.getInstance(request);
	    if (pc == null) {
		throw(new JspException("PushContext not available in RegisterTag.doStartTag()"));
	    }
  	    final String pushid = pc.createPushId();
	    
	    // Add to group;
	    pc.addGroupMember(group, pushid);

	    // Find the bean;
	    BasicGroupNotifier notifierBean = (BasicGroupNotifier)request.getAttribute(notifier);
	    if (notifierBean == null) {
		HttpSession session = request.getSession();
		notifierBean = (BasicGroupNotifier)session.getAttribute(notifier);
	    }
	    if (notifierBean == null) {
		throw( new JspException("Could not find notifier bean " + notifier));
	    } 

	    // Initialize bean context;
	    notifierBean.setPushContext(pc);
	    notifierBean.setGroup(group);

	    //Write script to register;
	    w.write("<script type=\"text/javascript\">");
	    w.write("ice.push.register(['" + pushid + "']," + callback + ");");
	    w.write("</script>");

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return SKIP_BODY;
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
    public String getCallback() {
	return callback;
    }
    public void setCallback(String cb) {
	this.callback = cb;
    }
}
