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

public class RegionTag extends BaseTag {

    private String page;
    private String id;

    @Override
    public int doStartTag() throws JspException {
	int i = super.doStartTag();

	try {
	    //Get the writer object for output.
	    JspWriter w = pageContext.getOut();

	    //Write script to register;
	    if (id == null) {
		id = pushid;
	    }
	    w.write("<script type=\"text/javascript\">");
	    w.write("ice.push.register(['" + pushid + "'], function(){");
	    w.write("getRegion('" + ((HttpServletRequest)pageContext.getRequest()).getContextPath() + page +
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
	return SKIP_BODY;
    }

    public String getId() {
	return id;
    }
    public void setId(String id) {
	this.id = id;
    }
    public String getPage() {
	return page;
    }
    public void setPage(String page) {
	this.page = page;
    }
}
