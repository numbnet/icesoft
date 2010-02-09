/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.samples.icechat.AbstractPushRequestContext;
import org.icepush.samples.icechat.IPushRequestContext;

/**
 *
 * @author bkroeger
 */
public abstract class PushPanel extends Panel {

    IPushRequestContext pushRequestContext;

    @Inject
    ChatManagerViewControllerSessionBean chatManagerVC;

    Label pushJavascript;
    String javascriptString;

    final AbstractDefaultAjaxBehavior behave;

    public PushPanel(String id) {
        super (id);

        // Push callback.  Called from wicketAjaxGet method.
        behave = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {

                pushCallback(target);

                // Render the Panel
                target.addComponent(this.getComponent());
            }
        };
        add(behave);

        // ICEpush code
        getPushRequestContext();
        chatManagerVC.setPushRequestContext(pushRequestContext);
/*        javascriptString = "window.onload = function(){ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('')});};";
        pushJavascript = new Label("pushJavascript", new PropertyModel(this,"javascriptString"));
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

    abstract protected void pushCallback(AjaxRequestTarget target);

    class WicketPushRequestContextAdapter extends AbstractPushRequestContext{
            public WicketPushRequestContextAdapter(){
                    WebRequest webRequest = (WebRequest)getRequest();
                    WebResponse webResponse = (WebResponse)getResponse();

                    intializePushContext((HttpServletRequest)webRequest.getHttpServletRequest(),
                                    (HttpServletResponse)webResponse.getHttpServletResponse());
            }
    }

    public IPushRequestContext getPushRequestContext(){
            if( pushRequestContext == null ){
                    pushRequestContext = new WicketPushRequestContextAdapter();
            }
            return pushRequestContext;
    }
}
