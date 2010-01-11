/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket.model;

import java.io.Serializable;
import org.icepush.samples.icechat.beans.model.BaseNewChatRoomMessageBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;

public class NewChatRoomMessageBean extends
		BaseNewChatRoomMessageBean
		implements INewChatRoomMessageBean, Serializable {}
