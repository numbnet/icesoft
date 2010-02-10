package org.icepush.integration.wicket.samples.pushpanel;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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
 * In this example, a click from "rightButton" will push an update out to all
 * members of the "rightPushPanel" group.  The model update can be found in the
 * pushCallback() method.  In this case, we simply add a String stating the
 * source of the push.
 */
public final class RightPushPanel extends PushPanel {

    final ListView pushListView;
    private List pushList = new ArrayList();

    boolean isPushMine = false;

    public RightPushPanel(String id) {
        super (id);
        this.setOutputMarkupId(true);

        Form rightForm = new Form("rightForm");
        rightForm.setOutputMarkupId(true);

        rightForm.add(new AjaxButton("rightButton") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                // PUSH CALL
                push();
                isPushMine=true;
                target.addComponent(this.getParent());
        }
        });

        // Temporary Button used to register callback until PUSH-32 is resolved
/*        Button testButton = new Button("testButton"){
            @Override
            protected void onComponentTag(final ComponentTag tag){
                super.onComponentTag(tag);
                System.out.println("Right TEsT BUTTON ONCLICK ATTRIBUTE: " + behave.getCallbackUrl());
                tag.put("onclick", "ice.push.register(['" + pushRequestContext.getCurrentPushId() + "'],function(){wicketAjaxGet('" + behave.getCallbackUrl() + "')});");
            }
        };
        rightForm.add(testButton);
*/
        // Button used to clear push source list
        rightForm.add(new AjaxButton("rightClear") {
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                pushList.clear();
                target.addComponent(this.getParent());
            }
        });

        // List containing the source of each push
        rightForm.add(pushListView = new ListView("pushListView", pushList){
            public void populateItem(final ListItem listItem){
                listItem.add(new Label("pushSource",(String)listItem.getModelObject()));
            }
        });

        add(rightForm);
    
    }

    // PUSH CALLBACK
    protected void pushCallback(AjaxRequestTarget target) {
        if(isPushMine){
            pushList.add("My Push.");
        }else{
            pushList.add("Pushed From Another User.");
        }
        isPushMine=false;
        pushListView.modelChanged();
    }

}
