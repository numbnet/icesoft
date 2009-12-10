package org.icepush.integration.jsp.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.icepush.PushContext;
import org.icepush.integration.common.notify.Notifier;
import org.icepush.integration.common.notify.GroupNotifier;

public class RegisterTag extends BaseTag {

    private String callback;

    @Override
	public int doStartTag() throws JspException {
	int i = super.doStartTag();

	try {
	    //Get the writer object for output.
	    JspWriter w = pageContext.getOut();

	    //Write script to register;
	    w.write("<script type=\"text/javascript\">");
	    w.write("ice.push.register(['" + pushid + "']," + callback + ");");
	    w.write("</script>");

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return SKIP_BODY;
    }

    public String getCallback() {
	return callback;
    }
    public void setCallback(String cb) {
	this.callback = cb;
    }
}
