package org.icepush.integration.wicket.samples.pushpanel;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.PushContext;

/**
 * Template used to implement push for a specific region (Panel)
 */
public final class PushPanel extends Panel {

    // Initialize PushContext (Creates PushId)
    WicketPushRequestContext pushRequestContext = new WicketPushRequestContext((WebRequest)getRequest(),(WebResponse)getResponse());

    public PushPanel(String id) {
        super (id);
        this.setOutputMarkupId(true);

        // Create or add to your group(s).  In this case the group is 'ExamplePanel'.
        PushContext.getInstance(getWebRequest().getHttpServletRequest().getSession().getServletContext()).addGroupMember("ExamplePanel",pushRequestContext.getCurrentPushId());

        // Push Javascript
        // USE THE callbackURLButton IN THIS TEMPLATE TO OUTPUT YOUR CALLBACK URL.  THIS URL WILL BE THE ARGUMENT IN THE WICKETAJAXGET METHOD CALL BELOW.
/*        Label pushJavascript = new Label("pushJavascript", new Model("window.onload = function(){ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('?wicket:interface=:0:pushPanel::IActivePageBehaviorListener:0:-1&wicket:ignoreIfNotActive=true')});};"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);
*/
        // Push callback.  Called from wicketAjaxGet method.
        final AbstractDefaultAjaxBehavior behave = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {

                // Update Model

                // Render the Panel
                target.addComponent(this.getComponent());
            }
        };
        add(behave);

        // Form used to retrieve the callback url.  Not necessary once you have the url.
/*        Form form = new Form("pushPanelForm");
        // BUTTON OUTPUTS CALLBACK URL THAT SHOULD BE THE ARGUMENT OF YOUR WICKETAJAXGET CALL.
        form.add(new AjaxButton("callbackURLButton") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                // Copy this output into wicketAjaxGet method
                System.out.println("callbackURL: " + behave.getCallbackUrl());
                // An Example push call
                PushContext.getInstance(getWebRequest().getHttpServletRequest().getSession().getServletContext()).push("ExamplePanel");
            }
        });
        add(form);
*/    }

}
