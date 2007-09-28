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

import com.icesoft.util.net.messaging.AbstractMessageHandler;
import com.icesoft.util.net.messaging.Message;
import com.icesoft.util.net.messaging.MessageHandler;
import com.icesoft.util.net.messaging.MessageSelector;
import com.icesoft.util.net.messaging.TextMessage;
import com.icesoft.util.net.messaging.expression.Equal;
import com.icesoft.util.net.messaging.expression.Identifier;
import com.icesoft.util.net.messaging.expression.StringLiteral;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class PurgeMessageHandler
extends AbstractMessageHandler
implements MessageHandler {
    protected static final String MESSAGE_TYPE = "Purge";

    private static final Log LOG = LogFactory.getLog(PurgeMessageHandler.class);

    private static MessageSelector messageSelector =
        new MessageSelector(
            new Equal(
                new Identifier(Message.MESSAGE_TYPE),
                new StringLiteral(MESSAGE_TYPE)));

    protected PurgeMessageHandler() {
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
            Map _purgeMap = new HashMap();
            StringTokenizer _tokens =
                new StringTokenizer(((TextMessage)message).getText());
            while (_tokens.hasMoreTokens()) {
                String _token = (String)_tokens.nextToken();
                int _beginIndex = 0;
                int _endIndex = _token.indexOf(";");
                String _iceFacesId = _token.substring(_beginIndex, _endIndex);
                _beginIndex = _endIndex + 1;
                _purgeMap.put(
                    _iceFacesId,
                    new Long(Long.parseLong(_token.substring(_beginIndex))));
            }
            purgeUpdatedViews(_purgeMap);
        }
    }

    public abstract void purgeUpdatedViews(final Map purgeMap);

    public String toString() {
        return getClass().getName();
    }
}
