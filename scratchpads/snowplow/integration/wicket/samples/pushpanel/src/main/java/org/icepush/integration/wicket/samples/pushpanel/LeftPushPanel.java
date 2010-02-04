package org.icepush.integration.wicket.samples.pushpanel;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.icepush.PushContext;

/**
 *
 * @author bkroeger
 */
public final class LeftPushPanel extends Panel {

    WicketPushRequestContext pushRequestContext = new WicketPushRequestContext((WebRequest)getRequest(),(WebResponse)getResponse());

    final ListView pushListView;
    private List pushList = new ArrayList();

    boolean isPushMine = false;


    public LeftPushPanel(String id) {
        super (id);
        this.setOutputMarkupId(true);

        // PUSH CALLBACK
        final AbstractDefaultAjaxBehavior behave = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                if(isPushMine){
                    pushList.add("My Push.");
                }else{
                    pushList.add("Pushed From Another Session.");
                }
                isPushMine=false;
                pushListView.modelChanged();
                target.addComponent(this.getComponent());
            }
        };
        add(behave);

        PushContext.getInstance(getWebRequest().getHttpServletRequest().getSession().getServletContext()).addGroupMember("LeftPanel",pushRequestContext.getCurrentPushId());
        Label pushJavascript = new Label("pushJavascript", new Model("window.onload = function(){ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('?wicket:interface=:0:leftPushPanel::IActivePageBehaviorListener:0:-1&wicket:ignoreIfNotActive=true')});};"));
        pushJavascript.setEscapeModelStrings(false);
        add(pushJavascript);

        Form leftForm = new Form("leftForm");
        leftForm.setOutputMarkupId(true);

        leftForm.add(new AjaxButton("leftButton") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                // Obtain url to paste in to wicketAjaxGet callback method.  This url will call AbstractDefaultAjaxBehavior's respond method.
                System.out.println("Left BEHAVE callbackURL: " + behave.getCallbackUrl());
                PushContext.getInstance(getWebRequest().getHttpServletRequest().getSession().getServletContext()).push("LeftPanel");
                isPushMine=true;
                target.addComponent(this.getParent());
            }
        });

        leftForm.add(new AjaxButton("leftClear") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                pushList.clear();
                target.addComponent(this.getParent());
            }
        });

        leftForm.add(pushListView = new ListView("pushListView", pushList){
            public void populateItem(final ListItem listItem){
                listItem.add(new Label("pushSource",(String)listItem.getModelObject()));
            }
        });

        add(leftForm);
    }

}
