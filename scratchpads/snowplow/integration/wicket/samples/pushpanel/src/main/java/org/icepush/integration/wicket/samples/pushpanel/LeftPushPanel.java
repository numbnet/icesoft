package org.icepush.integration.wicket.samples.pushpanel;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.icepush.integration.wicket.core.PushPanel;

/**
 * Example ICEpush integration.
 * This class extends the PushPanel class to implement a push region with the
 * same name as the component id.
 *
 * STEPS TO IMPLEMENT PUSH FOR THIS PANEL:
 * 1. Add push call(s) to your panel:
 *    push();
 * 2. Implement pushCallback(AjaxRequestTarget target) method to update your
 *    model and render the appropriate components on callback.
 *
 * In this example, a click from "leftButton" will push an update out to all
 * members of the "leftPushPanel" group.  The model update can be found in the
 * pushCallback() method.  In this case, we simply add a String stating the
 * source of the push.
 */
public final class LeftPushPanel extends PushPanel {

    final ListView pushListView;
    private List pushList = new ArrayList();

    boolean isPushMine = false;
//    ContextImage image;

    Button testButton;


    public LeftPushPanel(String id) {
        super (id);
        this.setOutputMarkupId(true);

        Form leftForm = new Form("leftForm");
        leftForm.setOutputMarkupId(true);

        leftForm.add(new AjaxButton("leftButton") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                // PUSH CALL
                push();
                isPushMine=true;
                target.addComponent(this.getParent());
            }
        });

        // Temporary Button used to register callback until PUSH-32 is resolved
/*        testButton = new Button("testButton"){
            @Override
            protected void onComponentTag(final ComponentTag tag){
                super.onComponentTag(tag);
                System.out.println("TEsT BUTTON ONCLICK ATTRIBUTE: " + behave.getCallbackUrl());
                tag.put("onclick", "ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('" + behave.getCallbackUrl() + "')});");
            }
        };
        leftForm.add(testButton);
*/
        // Button used to clear push source list
        leftForm.add(new AjaxButton("leftClear") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                pushList.clear();
                target.addComponent(this.getParent());
            }
        });
/*
        image = new ContextImage("leftDie","./images/dice-0.png");
        image.setOutputMarkupId(true);
        leftForm.add(image);
*/
        // List containing the source of each push
        leftForm.add(pushListView = new ListView("pushListView", pushList){
            public void populateItem(final ListItem listItem){
                listItem.add(new Label("pushSource",(String)listItem.getModelObject()));
            }
        });

        add(leftForm);
    }

    // PUSH CALLBACK
    protected void pushCallback(AjaxRequestTarget target) {
        //image.add(new AttributeModifier("src", true, new Model(".././images/dice-1.png")));
        //target.addComponent(image);
        if(isPushMine){
            pushList.add("My Push.");
        }else{
            pushList.add("Pushed From Another User.");
        }
        isPushMine=false;
        pushListView.modelChanged();
    }

}
