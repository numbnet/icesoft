package org.icepush.integration.wicket.core;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.PushContext;

/**
 * TO ENABLE YOUR APPLICATION FOR PUSH:
 *
 * 1. Add icepush.jar and icepush-wicket.jar to your application.
 * 2. Nest the following in the head tag of your page:
 *    <script type="text/javascript" src="code.icepush"></script>
 * 3. Add the following entry to your web.xml:
 *    <servlet>
 *      <servlet-name>icepush</servlet-name>
 *      <servlet-class>org.icepush.servlet.ICEpushServlet</servlet-class>
 *      <load-on-startup>1</load-on-startup>
 *    </servlet>
 *    <servlet-mapping>
 *      <servlet-name>icepush</servlet-name>
 *      <url-pattern>*.icepush</url-pattern>
 *    </servlet-mapping>
 *
 * TO CREATE A PUSHPANEL EXTEND THIS CLASS AND DO THE FOLLOWING:
 *
 * 1. Add push javascript to your panel's html file:
 *    <script wicket:id="pushJavascript" type="text/javascript">
 *    </script>
 *    WARNING: Do not nest this script in a component that is updated via AJAX
 *             This will result in the script being executed multiple times.
 * 1. Add push call(s) if necessary, to your java class:
 *    push();
 * 2. Implement the pushCallback(AjaxRequestTarget target) method in your java class
 *    to update your model and render the appropriate components on callback.
 *
 */
public abstract class PushPanel extends Panel {

    // Initialize PushContext (Creates PushId)
    WicketPushRequestContext pushRequestContext = new WicketPushRequestContext((WebRequest)getRequest(),(WebResponse)getResponse());

    Label pushJavascript;
    String javascriptString = "ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('')});";

    final AbstractDefaultAjaxBehavior behave;

    public PushPanel(String id) {
        super (id);
        this.setOutputMarkupId(true);

        // Create or add to push group.  The group has the same name as the component id.
        PushContext.getInstance(getWebRequest().getHttpServletRequest().getSession().getServletContext()).addGroupMember(id,pushRequestContext.getCurrentPushId());

        // Push callback.  Called from wicketAjaxGet method.
        behave = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {

                pushCallback(target);

            }
        };
        add(behave);

        // Push Javascript
        pushJavascript = new Label("pushJavascript", new PropertyModel(this,"javascriptString"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);
    }

    @Override
    protected void onBeforeRender(){
        String tempJavascriptString = "ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('" + behave.getCallbackUrl() + "')});";
        if(!javascriptString.equals(tempJavascriptString)){
            javascriptString = tempJavascriptString;
            pushJavascript.modelChanged();
        }
        super.onBeforeRender();
    }

    public String getJavascriptString() {
        return javascriptString;
    }

    public void setJavascriptString(String javascriptString) {
        this.javascriptString = javascriptString;
    }

    protected void push(){
        PushContext.getInstance(getWebRequest().getHttpServletRequest().getSession().getServletContext()).push(getId());
    }

    abstract protected void pushCallback(AjaxRequestTarget target);

}
