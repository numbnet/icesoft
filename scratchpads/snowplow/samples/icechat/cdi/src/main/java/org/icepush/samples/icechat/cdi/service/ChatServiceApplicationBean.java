/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.icepush.samples.icechat.cdi.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.icepush.samples.icechat.beans.service.BaseChatServiceBean;
import org.icepush.samples.icechat.service.IChatService;

/**
 *
 * @author pbreau
 */
@Named(value="chatService")
@ApplicationScoped
public class ChatServiceApplicationBean extends BaseChatServiceBean 
	implements IChatService, Serializable {

}
