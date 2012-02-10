/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.navigation;

import org.icemobile.samples.mobileshowcase.view.examples.media.audio.AudioBean;
import org.icemobile.samples.mobileshowcase.view.examples.media.image.ImageBean;
import org.icemobile.samples.mobileshowcase.view.examples.media.video.VideoBean;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.MenuLink;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 * Menu items for media output components
 */
@org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu(
        title = "menu.media.title",
        menuLinks = {
                @MenuLink(title = "menu.media.audio.title",
                        exampleBeanName = AudioBean.BEAN_NAME),
                @MenuLink(title = "menu.media.image.title",
                        exampleBeanName = ImageBean.BEAN_NAME),
                @MenuLink(title = "menu.media.video.title",
                        exampleBeanName = VideoBean.BEAN_NAME)
        })
@ManagedBean(name = MediaPlaybackMenu.BEAN_NAME)
@ApplicationScoped
public class MediaPlaybackMenu extends org.icemobile.samples.mobileshowcase.view.metadata.context.Menu<MediaPlaybackMenu>
        implements Serializable {

    public static final String BEAN_NAME = "mediaPlaybackMenu";

    public MediaPlaybackMenu() {
        super(MediaPlaybackMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }
}
