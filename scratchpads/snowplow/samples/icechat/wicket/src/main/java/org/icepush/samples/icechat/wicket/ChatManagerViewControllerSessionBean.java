/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.icepush.samples.icechat.cdi.view.ChatManagerViewControllerBean;
import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;

/**
 *
 * @author bkroeger
 */
@Named
@SessionScoped
public class ChatManagerViewControllerSessionBean extends ChatManagerViewControllerBean{

        public ICurrentChatSessionHolderBean getCurrentChatSessionHolder(){
            return currentChatSessionHolder;
        }
}
