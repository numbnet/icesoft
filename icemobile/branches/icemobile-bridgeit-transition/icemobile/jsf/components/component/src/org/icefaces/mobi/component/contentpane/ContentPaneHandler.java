/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;

import org.icefaces.mobi.component.contentstack.ContentStack;

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
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
    }

    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c)
            throws java.io.IOException, javax.faces.FacesException, javax.el.ELException {
        UIComponent parent = c.getParent();
        if (parent != null) {
            updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
        }

        Boolean skip = (Boolean) c.getAttributes().get(SKIP_CONSTRUCTION_KEY);
        if (skip == null || !skip.booleanValue()) {
            super.applyNextHandler(ctx, c);
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
        boolean overridden = false;
        if (client != null && client.getBoolean(ctx)==true){
             overridden = true;
        }
        if (facelet == null || facelet.getBoolean(ctx)!=true || overridden==true) {
            return false;
        }
        if (!(parent instanceof ContentStack)) {
            return false;
        }
        String currentId = ((ContentStack)parent).getCurrentId();
        if (currentId == null || currentId.length() == 0) {
            return false;
        }
        if (currentId.equals(c.getId())) {
            return false;
        }
        return true;
    }
}
