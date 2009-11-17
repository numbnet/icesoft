package org.icepush.sample.basic;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.icepush.PushContext;

public class RegionTag extends TagSupport {

    private String id;
    private String group;
    private String notifier;

    @Override
	public int doStartTag() throws JspException {

	try {
	    // Find the notifier bean;
	    BasicGroupNotifier notifierBean = (BasicGroupNotifier)pageContext.findAttribute(notifier);
	    if (notifierBean == null) {
		throw( new JspException("Could not find notifier bean " + notifier));
	    } 

	    // Get a push id;
	    final PushContext pc = PushContext.getInstance(pageContext.getServletContext());
	    if (pc == null) {
		throw(new JspException("PushContext not available in RegisterTag.doStartTag()"));
	    }
	    final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
  	    final String pushid = 
		pc.createPushId(request,(HttpServletResponse)(pageContext.getResponse()));
	    
	    // Add to group;
	    notifierBean.setPushContext(pc);
	    if (group == null) {
		group = pushid;
	    } else {
		pc.addGroupMember(group, pushid);
	    }
	    notifierBean.addGroup(group);

	    //Get the writer object for output.
	    JspWriter w = pageContext.getOut();

	    //Write script to register;
	    w.write("<script type=\"text/javascript\">");
	    w.write("ice.push.register(['" + pushid + "'], function() {");
	    w.write("ice.push.post('" + request.getContextPath() + 
		    "/pushnotifier/notify.html', function(parameter) {");
	    w.write("parameter('notifier', '" + notifier + "');");
	    w.write("}, function(statusCode, responseText) {");
	    w.write("replaceDiv('" + id + "', responseText);");
	    w.write("}); });");
	    w.write("</script>");

	    //Write the div;
	    w.write("<div id=\"" + id + "\">");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	group = null;
	return EVAL_BODY_INCLUDE;
    }


    @Override
	public int doEndTag() throws JspException {

	try {
	    //Get the writer object for output.
	    JspWriter w = pageContext.getOut();

	    //Close the div;
	    w.write("</div>");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return EVAL_PAGE;
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
}
