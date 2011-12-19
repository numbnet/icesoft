/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.dialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = DialogBean.BEAN_NAME,
        title = "example.ace.dialog.dialogEffectAndSize.title",
        description = "example.ace.dialog.dialogEffectAndSize.description",
        example = "/resources/examples/ace/dialog/dialogEffectsAndSize.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dialogEffectsAndSize.xhtml",
                    resource = "/resources/examples/ace/dialog/dialogEffectsAndSize.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="dialogEffectsAndSize.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dialog/dialogEffectsAndSizeBean.java")
        }
)
@ManagedBean(name= DialogEffectsAndSizeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DialogEffectsAndSizeBean extends ComponentExampleImpl<DialogEffectsAndSizeBean> implements Serializable
{
    public static final String BEAN_NAME = "dialogEffectsAndSizeBean";
    private String showEffect;
    private String hideEffect;
    private int minWidth;
    private int minHeight;
    

    public DialogEffectsAndSizeBean() 
    {
        super(DialogEffectsAndSizeBean.class);
        initializeDefaultBeanValues();
    }

    public String getHideEffect() {
        return hideEffect;
    }

    public void setHideEffect(String hideEffect) {
        this.hideEffect = hideEffect;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public String getShowEffect() {
        return showEffect;
    }

    public void setShowEffect(String showEffect) {
        this.showEffect = showEffect;
    }

    private void initializeDefaultBeanValues() 
    {
        this.showEffect = "blind";
        this.hideEffect = "blind";
        this.minWidth = 200;
        this.minHeight = 200;
    }
}
