/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package com.icesoft.faces.async.server.messaging;

import com.icesoft.util.net.messaging.Message;
import com.icesoft.util.net.messaging.MessageHandler;
import com.icesoft.util.net.messaging.MessageSelector;
import com.icesoft.util.net.messaging.MessageServiceClient;
import com.icesoft.util.net.messaging.TextMessage;
import com.icesoft.util.net.messaging.expression.Equal;
import com.icesoft.util.net.messaging.expression.Identifier;
import com.icesoft.util.net.messaging.expression.StringLiteral;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>BufferedContextEventsMessageHandler</code> class is responsible
 *   for handling JMS messages with message type
 *   &quot;<code>BufferedContextEvents</code>&quot;.
 * </p>
 *
 * @see        MessageServiceClient
 */
public abstract class BufferedContextEventsMessageHandler
extends AbstractContextEventMessageHandler
implements MessageHandler {
    protected static final String MESSAGE_TYPE = "BufferedContextEvents";

    private static final Log LOG =
        LogFactory.getLog(BufferedContextEventsMessageHandler.class);

    private static MessageSelector messageSelector =
        new MessageSelector(
            new Equal(
                new Identifier(Message.MESSAGE_TYPE),
                new StringLiteral(MESSAGE_TYPE)));

    protected BufferedContextEventsMessageHandler() {
        super(messageSelector);
    }

    public void handle(final Message message) {
        if (message == null) {
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling:\r\n\r\n" + message);
        }
        if (message instanceof TextMessage) {
            StringTokenizer _messages =
                new StringTokenizer(((TextMessage)message).getText());
            while (_messages.hasMoreTokens()) {
                StringTokenizer _tokens =
                    new StringTokenizer(_messages.nextToken(), ";");
                String _event = _tokens.nextToken();
                if (_event.equals("ContextDestroyed")) {
                    // message-body:
                    //     <event-name>
                    contextDestroyed();
                } else if (_event.equals("ICEfacesIDRetrieved")) {
                    // message-body:
                    //     <event-name>;<ICEfaces ID>
                    iceFacesIdRetrieved(_tokens.nextToken());
                } else if (_event.equals("SessionDestroyed")) {
                    // message-body:
                    //     <event-name>;<ICEfaces ID>
                    sessionDestroyed(_tokens.nextToken());
                } else if (_event.equals("ViewNumberRetrieved")) {
                    // message-body:
                    //     <event-name>;<ICEfaces ID>;<View Number>
                    viewNumberRetrieved(
                        _tokens.nextToken(), _tokens.nextToken());
                }
            }
        }
    }

    public String toString() {
        return getClass().getName();
    }
}
