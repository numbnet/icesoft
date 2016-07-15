/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobi.component.contentpane;

import org.icefaces.mobi.api.ContentPaneController;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;

public class ContentPaneHandler extends ComponentHandler {
    private static final String SKIP_CONSTRUCTION_KEY = "org.icefaces.mobi.contentPane.SKIP_CONSTRUCTION";

//    private final TagAttribute cacheType;
    private final TagAttribute facelet;
    private final TagAttribute client;

    public ContentPaneHandler(ComponentConfig componentConfig) {
        super(componentConfig);
        Tag tag = componentConfig.getTag();
        facelet = tag.getAttributes().get("facelet");
        client = tag.getAttributes().get("client");
       // cacheType = tag.getAttributes().get("cacheType");
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        //System.out.println("CPH.onComponentCreated()");
        updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
    }

    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c)
            throws java.io.IOException, javax.faces.FacesException, javax.el.ELException {
        // onComponentCreated(-) is called on GET and POST restore view,
        // but not POST render.
        // In applyNextHandler(-), on GET and POST restore view,
        // c.parent is null, but in POST render c.parent is non-null.
        // So for POST render, check if should skip in applyNextHandler(-),
        // using the non-null parent as the switch

        //System.out.println("CPH.applyNextHandler()");
        UIComponent parent = c.getParent();
        if (parent != null) {
            updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
        }

      //  if (parent != null) System.out.println("Parent id: " + parent.getId());

        Boolean skip = (Boolean) c.getAttributes().get(SKIP_CONSTRUCTION_KEY);
        if (skip == null || !skip.booleanValue()) {
         //   System.out.println("  " + c.getId() + "\t\tConstruct children");
            super.applyNextHandler(ctx, c);
        }
        else {
          //  System.out.println("  " + c.getId() + "\t\tDon't construct children");
        }
    }

    protected boolean updateFlagShouldOptimiseSkipChildConstruction(
            FaceletContext ctx, UIComponent c, UIComponent parent) {
        boolean skip = shouldOptimiseSkipChildConstruction(ctx, c, parent);
        if (skip) {
            c.getAttributes().put(SKIP_CONSTRUCTION_KEY, Boolean.TRUE);
        }
        else {
            c.getAttributes().remove(SKIP_CONSTRUCTION_KEY);
        }
        return skip;
    }

    protected boolean shouldOptimiseSkipChildConstruction(
            FaceletContext ctx, UIComponent c, UIComponent parent) {

  /*      System.out.println("shouldOptimiseSkipChildConstruction()");
        System.out.println("    c: " + c);
        System.out.println("    c.id: " + c.getId());
        System.out.println("    c.client: "+ ((ContentPane)c).isClient());
        System.out.println("    facelet: " + (facelet == null ? "<unspecified>" : facelet.getValue(ctx)));
        System.out.println("    parent: " + parent);
        System.out.println("    parent instanceof ContentPaneController: " + (parent instanceof ContentPaneController));
        if (parent instanceof ContentPaneController) {
            System.out.println("    parent.getSelectedId: " + ((ContentPaneController)parent).getSelectedId());
            System.out.println("    parent.childCount: " + parent.getChildCount());
            if (parent.getChildren().contains(c)) {
                System.out.println("    parent already has child  index: " + parent.getChildren().indexOf(c));
            }
            else {
                System.out.println("    parent does not have child  index: " + parent.getChildCount());
            }
            //for (UIComponent kid : parent.getChildren()) {
            //    System.out.println("    kid: " + kid.getId() + "  attrib MARK_CREATED: " + kid.getAttributes().get("com.sun.faces.facelets.MARK_ID"));
            //}
        }  */
        boolean overridden = false;
        if (client != null && client.getBoolean(ctx)==true){
             overridden = true;
        }
//        System.out.println("  remove key");
        if (facelet == null || facelet.getBoolean(ctx)!=true || overridden==true) {
//            System.out.println("  cacheType not tobeconstructed");
            return false;
        }
 //       System.out.println("  cacheType is tobeconstructed");
        if (!(parent instanceof ContentPaneController)) {
//            System.out.println("  parent not ContentPaneController");
            return false;
        }
//        System.out.println("  parent is ContentPaneController");
        String selectedId = ((ContentPaneController)parent).getSelectedId();
        if (selectedId == null || selectedId.length() == 0) {
//            System.out.println("  selectedId not set");
            return false;
        }
//        System.out.println("  selectedId is set");
        if (selectedId.equals(c.getId())) {
//            System.out.println("  selectedId equal to id");
            return false;
        }
//        System.out.println("  selectedId not equal to id => SKIP");
        return true;
    }
}
