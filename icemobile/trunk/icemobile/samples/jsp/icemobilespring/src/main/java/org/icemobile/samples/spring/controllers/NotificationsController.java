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

package org.icemobile.samples.spring.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.samples.spring.NotificationsBean;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.icepush.PushContext;
import org.icepush.PushNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;

@Controller
@SessionAttributes("notificationsBean")
public class NotificationsController extends ICEmobileBaseController{

    Timer pushTimer = new Timer(true);

    @Autowired
    private WebApplicationContext context;

    @RequestMapping(value = "/notifications", 
            headers="content-type=application/x-www-form-urlencoded")
    public void fileUploadFormPost(HttpServletRequest request, Model model) {
        fileUploadForm(request, model);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public void fileUploadForm(HttpServletRequest request, Model model) {
        model.addAttribute("isGET", Boolean.TRUE);
    }

    @RequestMapping(value = "/notificationsregion")
    public void notificationsRegion() {
    }

    @ModelAttribute("notificationsBean")
    public NotificationsBean createBean() {
        return new NotificationsBean();
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.POST)
    public void process(HttpServletRequest request,
            @RequestParam(value = "pushType", required = false) String pushType,
            @ModelAttribute("notificationsBean") NotificationsBean model)
            throws IOException {
        
        final PushContext pushContext = PushContext.getInstance(
                context.getServletContext() );
        final String sessionId = request.getSession().getId();
        final String title = model.getTitle();
        final String message = model.getMessage();
        model.setTitle("");
        model.setMessage("");
        final NotificationsBean notificationsBean = model;
        final boolean isCloud = "Priority Push".equals(pushType);
        TimerTask pushTask = new TimerTask()  {
            public void run()  {
                notificationsBean.setTitle(title);
                notificationsBean.setMessage(message);
                if (isCloud)  {
                    pushContext.push(sessionId,
                            new PushNotification(title, message));
                } else  {
                    pushContext.push(sessionId);
                }
            }
        };
        pushTimer.schedule(pushTask, model.getDelay() * 1000);
    }

    //temporary method until cloud push is added to JavaScript API
    //in PUSH-267
    @RequestMapping(value = "/pushtocloud", method = RequestMethod.POST)
    public @ResponseBody String pushtocloud(HttpServletRequest request,
            @RequestParam(value = "group") final String group,
            @RequestParam(value = "title") final String title,
            @RequestParam(value = "message") final String message,
            @RequestParam(value = "delay") final int delay)
            throws IOException {
        final PushContext pushContext = PushContext.getInstance(
                context.getServletContext() );

        TimerTask pushTask = new TimerTask()  {
            public void run()  {
                pushContext.push(group,
                        new PushNotification(title, message));
            }
        };
        pushTimer.schedule(pushTask, delay * 1000);

        return "pushed to cloud";
    }
}
