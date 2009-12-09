package org.icepush.integration.jsp.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.icepush.PushContext;
import org.icepush.integration.common.notify.Notifier;
import org.icepush.integration.common.notify.GroupNotifier;

public class RegionTag extends TagSupport {

    private String id;
    private String group;
    private String notifier;
    private String page;

    @Override
	public int doStartTag() throws JspException {

	try {
	    // Get a push id;
	    final PushContext pc = PushContext.getInstance(pageContext.getServletContext());
	    if (pc == null) {
		throw(new JspException("PushContext not available in RegisterTag.doStartTag()"));
	    }
	    final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
  	    final String pushid = 
		pc.createPushId(request,(HttpServletResponse)(pageContext.getResponse()));
	    
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

	    //Get the writer object for output.
	    JspWriter w = pageContext.getOut();

	    //Write script to register;
	    if (id == null) {
		id = pushid;
	    }
	    w.write("<script type=\"text/javascript\">");
	    w.write("ice.push.register(['" + pushid + "'], function(){");
	    w.write("getRegion('" + request.getContextPath() + page +
		    "', '" + id + "','" + group + "');}");
	    w.write(");");
	    w.write("</script>");

	    //Write the div;
	    w.write("<div id=\"" + id + "\">");
	    w.flush();

	    //Include the page;
	    try {
		String params = new String("?group=" + group);
		pageContext.getServletContext().getRequestDispatcher(page+params).
		    include(pageContext.getRequest(),pageContext.getResponse());
	    } catch (IOException ioe) {
		ioe.printStackTrace();
	    } catch (ServletException se) {
		se.printStackTrace();
	    }

	    //Close the div;
	    w.write("</div>");

	} catch (IOException e) {
	    e.printStackTrace();
	}
	group = null;
	id=null;
	notifier=null;
	return SKIP_BODY;
    }

    public String getId() {
	return id;
    }
    public void setId(String id) {
	this.id = id;
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
    public String getPage() {
	return page;
    }
    public void setPage(String page) {
	this.page = page;
    }
}
