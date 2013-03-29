/*
 * ******* GENERATED CODE - DO NOT EDIT *******
 */

package org.icemobile.component;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.icemobile.component.TestCamera;
import org.icemobile.renderkit.TestCameraRenderer;
import org.icemobile.context.JSPRenderContext;

public class TestCameraTag extends BodyTagSupport {
    TestCameraRenderer testCameraRenderer = new TestCameraRenderer();

    private boolean disabled;

    public boolean isDisabled()  {
        return disabled;
    }

    public void setDisabled(boolean disabled)  {
        this.disabled = disabled;
    }

    public int doStartTag() throws JspException {
        try {
            testCameraRenderer.encodeBegin(
                    new JSPRenderContext(pageContext),
                    new TestCameraTagWrapper(this));
        }catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspException{
        try {
            testCameraRenderer.encodeEnd(
                new JSPRenderContext(pageContext),
                new TestCameraTagWrapper(this) );
        }catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

}