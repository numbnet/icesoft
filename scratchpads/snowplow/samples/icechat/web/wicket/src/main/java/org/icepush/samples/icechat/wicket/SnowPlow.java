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
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.samples.icechat.AbstractPushRequestContext;
import org.icepush.samples.icechat.IPushRequestContext;

/**
 *
 * @author bkroeger
 */
public class SnowPlow extends Panel {

    IPushRequestContext pushRequestContext;

    @Inject
    ChatManagerViewControllerSessionBean chatManagerVC;

    public SnowPlow(String id) {
        super (id);

        // ICEpush code
        getPushRequestContext();
        chatManagerVC.setPushRequestContext(pushRequestContext);
        Label pushJavascript = new Label("pushJavascript", new Model("window.onload = function(){ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){alert('Callback!');});};"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);
    }

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
