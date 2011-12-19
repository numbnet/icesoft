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

package org.icefaces.samples.showcase.ace.documentationResources;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import org.icefaces.samples.showcase.metadata.context.ResourceRootPath;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;


@ExampleResources(
        resources ={
            // WIKI Resources
            @ExampleResource(type = ResourceType.wiki,
                title="ace:animation",
                resource = ResourceRootPath.FOR_WIKI + "Animation"),
            // TLD Resources
            @ExampleResource(type = ResourceType.tld,
                title="ace:animation",
                resource = ResourceRootPath.FOR_ACE_TLD + "animation.html")
        }
)

@ManagedBean(name= AnimationResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AnimationResources extends ComponentExampleImpl<AnimationResources> implements Serializable
{
    public static final String BEAN_NAME = "animationResources";

    public AnimationResources() {
        super(AnimationResources.class);
    }
}
