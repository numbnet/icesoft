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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package org.icefaces.push.server;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.net.messaging.AbstractMessageHandler;
import com.icesoft.net.messaging.DefaultMessageService;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.TextMessage;
import com.icesoft.net.messaging.expression.Equal;
import com.icesoft.net.messaging.expression.Identifier;
import com.icesoft.net.messaging.expression.Or;
import com.icesoft.net.messaging.expression.StringLiteral;
import com.icesoft.util.Properties;

import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PushServerMessageService
extends DefaultMessageService {
    private static final Log LOG = LogFactory.getLog(PushServerMessageService.class);

    private final MessageHandler bufferedContextEventsMessageHandler = new BufferedContextEventsMessageHandler();
    private final MessageHandler contextEventMessageHandler = new ContextEventMessageHandler();
    private final MessageHandler helloMessageHandler =
        new AbstractMessageHandler(
            new MessageSelector(new Equal(new Identifier(Message.MESSAGE_TYPE), new StringLiteral("Presence")))) {

            public void handle(final Message message) {
                if (message instanceof TextMessage) {
                    if (((TextMessage)message).getText().equals("Hello")) {
                        Properties _messageProperties = new Properties();
                        _messageProperties.setStringProperty(
                            Message.DESTINATION_SERVLET_CONTEXT_PATH,
                            message.getStringProperty(Message.SOURCE_SERVLET_CONTEXT_PATH));
                        getMessageServiceClient().publish(
                            new StringBuffer().
                                append("Acknowledge").append(";").
                                append(ProductInfo.PRODUCT).append(";").
                                append(ProductInfo.PRIMARY).append(";").
                                append(ProductInfo.SECONDARY).append(";").
                                append(ProductInfo.TERTIARY).append(";").
                                append(ProductInfo.RELEASE_TYPE).append(";").
                                append(ProductInfo.BUILD_NO).append(";").
                                append(ProductInfo.REVISION).
                                    toString(),
                            _messageProperties,
                            "Presence");
                    }
                }
            }
        };
    private final MessageHandler updatedViewsMessageHandler = new UpdatedViewsMessageHandler();

    public PushServerMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        super(messageServiceClient, scheduledThreadPoolExecutor);
    }

    public PushServerMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        final boolean retryOnFail)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        super(messageServiceClient, scheduledThreadPoolExecutor, retryOnFail);
    }

    public PushServerMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        final Configuration configuration)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        super(messageServiceClient, scheduledThreadPoolExecutor, configuration);
    }

    public PushServerMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        final Configuration configuration, final boolean retryOnFail)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        super(messageServiceClient, scheduledThreadPoolExecutor, configuration, retryOnFail);
    }

    public MessageHandler getBufferedContextEventsMessageHandler() {
        return bufferedContextEventsMessageHandler;
    }

    public MessageHandler getContextEventMessageHandler() {
        return contextEventMessageHandler;
    }

    public MessageHandler getUpdatedViewsMessageHandler() {
        return updatedViewsMessageHandler;
    }

    protected void setUpMessageServiceClient()
    throws MessageServiceException {
        // throws MessageServiceException
        getMessageServiceClient().subscribe(
            new MessageSelector(
                new Or(
                    helloMessageHandler.getMessageSelector().getExpression(),
                    new Or(
                        bufferedContextEventsMessageHandler.getMessageSelector().getExpression(),
                        new Or(
                            contextEventMessageHandler.getMessageSelector().getExpression(),
                            updatedViewsMessageHandler.getMessageSelector().getExpression())))));
        getMessageServiceClient().addMessageHandler(helloMessageHandler);
        getMessageServiceClient().addMessageHandler(bufferedContextEventsMessageHandler);
        getMessageServiceClient().addMessageHandler(contextEventMessageHandler);
        getMessageServiceClient().addMessageHandler(updatedViewsMessageHandler);
    }

    protected void tearDownMessageServiceClient()
    throws MessageServiceException {
        getMessageServiceClient().removeMessageHandler(updatedViewsMessageHandler);
        getMessageServiceClient().removeMessageHandler(contextEventMessageHandler);
        getMessageServiceClient().removeMessageHandler(bufferedContextEventsMessageHandler);
        getMessageServiceClient().removeMessageHandler(helloMessageHandler);
        // throws MessageServiceException
        getMessageServiceClient().unsubscribe();
    }
}
