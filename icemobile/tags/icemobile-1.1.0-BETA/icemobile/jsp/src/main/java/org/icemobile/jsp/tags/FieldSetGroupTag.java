package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;

/**
 *
 */
public class FieldSetGroupTag extends TagSupport {

    public static final String FIELDSET_CLASS = "mobi-fieldset";
    public static final String FIELDSETINSET_CLASS = "mobi-fieldset-inset";


    private String id;
    private boolean inset;
    private String styleClass;
    private String style;


    public int doStartTag() throws JspTagException {

        // Todo: use symbolic string constants once things are working.

        Writer out = pageContext.getOut();

        try {

            out.write("<fieldset");
            if (id != null && !"".equals(id)) {
                out.write(" id=\'" + getId() + "\'");
            }

            // Apply default Field Group Class.
            StringBuilder classBuilder = new StringBuilder(FIELDSET_CLASS);

            if (isInset()) {
                classBuilder.append(" ").append(FIELDSETINSET_CLASS);
            }

            // apply user defined class
            String styleClass = getStyleClass();
            if (styleClass != null && !"".equals(styleClass)) {
                classBuilder.append(" ").append(styleClass);
            }

            out.write(" class=\'" + classBuilder.toString() + "\'");

            String inlineStyle = getStyle();


            // apply any user defined inline style
            if (inlineStyle != null && !"".equals(inlineStyle)) {
                if (inlineStyle.endsWith(";")) {
                    inlineStyle = inlineStyle.substring(0, inlineStyle.length() - 1);
                }
                out.write(" style=\'" + inlineStyle + ";\'");
            }
            out.write(">");

        } catch (IOException e) {
        }
        return (EVAL_BODY_INCLUDE);

    }

    public int doAfterBody() {

        Writer out = pageContext.getOut();
        try {
            out.write("</fieldset>");
        } catch (IOException ieo) {
        }

        return SKIP_BODY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isInset() {
        return inset;
    }

    public void setInset(boolean inset) {
        this.inset = inset;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
