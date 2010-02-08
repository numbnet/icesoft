package org.icepush.integration.wicket.samples.pushpanel;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.PushContext;

/**
 * Template used to implement push for a specific region (Panel)
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
    }
*/
    public String getJavascriptString() {
        return javascriptString;
    }

    public void setJavascriptString(String javascriptString) {
        this.javascriptString = javascriptString;
    }

    abstract protected void pushCallback(AjaxRequestTarget target);

}
