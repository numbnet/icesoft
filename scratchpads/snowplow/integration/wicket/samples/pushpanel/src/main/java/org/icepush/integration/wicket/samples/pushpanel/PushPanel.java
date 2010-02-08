package org.icepush.integration.wicket.samples.pushpanel;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.PushContext;

/**
 * TO ADD PUSH TO YOUR PANEL EXTEND THIS CLASS.
 *
 * IN YOUR SUBCLASS DO THE FOLLOWING:
 * 1. Add push call(s) to your panel:
 *    push();
 * 2. Implement the pushCallback(AjaxRequestTarget target) method to update your
 *    model and render the appropriate components on callback.
 *
 */
public abstract class PushPanel extends Panel {

    // Initialize PushContext (Creates PushId)
    WicketPushRequestContext pushRequestContext = new WicketPushRequestContext((WebRequest)getRequest(),(WebResponse)getResponse());

    Label pushJavascript;
    String javascriptString = "window.onload = function(){ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('')});};";

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

                // Render the Panel
                target.addComponent(this.getComponent());
            }
        };
        add(behave);

        // Push Javascript
/*        pushJavascript = new Label("pushJavascript", new PropertyModel(this,"javascriptString"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);*/
    }

/*    @Override
    protected void onBeforeRender(){
        javascriptString = "window.onload = function(){ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('" + behave.getCallbackUrl() + "')});};";
        pushJavascript.modelChanged();
        super.onBeforeRender();
    }*/

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
