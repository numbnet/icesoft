package com.icesoft.faces.async.render;

import com.icesoft.net.messaging.AbstractMessageHandler;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.expression.And;
import com.icesoft.net.messaging.expression.Equal;
import com.icesoft.net.messaging.expression.Identifier;
import com.icesoft.net.messaging.expression.NotEqual;
import com.icesoft.net.messaging.expression.StringLiteral;
import com.icesoft.net.messaging.jms.JMSAdapter;

import java.net.MalformedURLException;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The BroadcastHub is responsible for publishing and subscribing render
 * requests to a central JMS topic.  Each application on a node is both a sender
 * and receiver of render messages to this topic.  When a render is required
 * across nodes in a cluster, the relayRenderRequest method can be called,
 * providing the name of the group to render.  BroadcastHub instances on all
 * nodes, including this one, will, once the message is received, trigger a
 * render call on the named renderer provided one exists.
 * <p/>
 * The JNDI and JMS configuration data is specific to each app container and JMS
 * provider.
 *
 * @author ICEsoft Technologies, Inc.
 */
public class BroadcastHub {
    private static final Log LOG = LogFactory.getLog(BroadcastHub.class);

    /**
     * The JMS utility for both sending and receiving various JMS messages.
     */
    private MessageServiceClient messageServiceClient;

    /**
     * The message type that is used to filter the incoming JMS messages that
     * the BroadcastHub is interested in receiving.  The keys are used to set
     * properties on the message.  They are used when messages to arrive to
     * determine which renderer should be located.
     */
    protected static final String MESSAGE_TYPE = "RenderBroadcast";
    protected static final String RENDERER_NAME_KEY = "rendererName";
    protected static final String COMMAND_KEY = "command";

    /**
     * The local RenderManager to use for retrieving named AsyncRenderers.
     */
    private RenderManager renderManager;

    /**
     * The message handler for broadcasted render requests.
     */
    private RenderMessageHandler messageHandler;

    /**
     * No args constuctor suitable to be used as a bean.  Initializes the JMS
     * topic and session to be used for broadcasting render messages.
     */
    public BroadcastHub() {
        // do nothing.
    }

    /**
     * Prepare the required JMS resources by creating a JMSClient.  Properties
     * can be provided to specify the appropriate JNDI context, etc.  If no
     * properties are supplied, then the JMSClient uses the default settings for
     * the environment it is running in.
     */
    public void init() {
        //The JMSClient needs the ServletContext to uniquely identify which
        //message handlers should get which messages. We only use a single
        //JMS topic for everything so filtering this way is important.
        ServletContext servletContext = getServletContext();
        if (servletContext == null) {
            throw
                new IllegalStateException(
                    "could not retrieve the ServletContext");
        }

        MessageServiceAdapter messageServiceAdapter =
            new JMSAdapter(servletContext);
        //Attempt to open a JMS connection.  This is the basic JNDI/JMS
        //initialization.
        messageServiceClient =
            new MessageServiceClient(
                messageServiceAdapter.getMessageServiceConfiguration(),
                messageServiceAdapter,
                servletContext);
        //Subscribe to the default topic. We only want to get messages that
        //pertain to our application.  We don't want to get render messages
        //from other applications since the render groups may have the same
        //name. To avoid cross-application render messages, we set the
        //servlet context path as a message selector to filter messages based
        //on the individual application.
        try {
            String path = servletContext.getResource("/").getPath();
            int index = path.lastIndexOf("/");
            path = path.substring(path.lastIndexOf("/", index - 1) + 1, index);
            MessageSelector messageSelector =
                new MessageSelector(
                    new And(
                        new And(
                            new Equal(
                                new Identifier(Message.MESSAGE_TYPE),
                                new StringLiteral(BroadcastHub.MESSAGE_TYPE)),
                            new Equal(
                                new Identifier(Message.SOURCE_SERVLET_CONTEXT_PATH),
                                new StringLiteral(path))),
                        new NotEqual(
                            new Identifier("source_id"),
                            new StringLiteral(messageServiceClient.toString()))));
            messageServiceClient.subscribe(
                MessageServiceClient.RENDER_TOPIC_NAME, messageSelector, true);
            //Add ourselves as a message handler using our topic.
            messageHandler = new RenderMessageHandler(renderManager);
            messageHandler.setMessageSelector(messageSelector);
            messageServiceClient.addMessageHandler(
                messageHandler, MessageServiceClient.RENDER_TOPIC_NAME);
        } catch (MalformedURLException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal("Failed to get servlet context path!", exception);
            }
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to subscribe to topic: " +
                        MessageServiceClient.RENDER_TOPIC_NAME,
                    exception);
            }
        }
        try {
            //Start the client.  Should be able to send and receive now.
            messageServiceClient.start();
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal("Failed to start message delivery!", exception);
            }
        }
    }

    private static ServletContext getServletContext() {
        Object context =
            FacesContext.getCurrentInstance().getExternalContext().getContext();
        if (context instanceof ServletContext) {
            return (ServletContext)context;
        } else {
            return null;
        }
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public void setRenderManager(final RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    /**
     * Using JMS, this method sends a message to a configured topic (specified
     * by the topicName) via a cluster-aware JMS provider.  The message contains
     * a command to render and a the name of the render group that should be
     * notified.
     *
     * @param      renderer
     *                 the renderer on the remote node that should have it's
     *                 {@link AsyncRenderer#requestRender()} method called.
     */
    synchronized void relayRenderRequest(final AsyncRenderer renderer) {
        if (renderer == null) {
            return;
        }
        if (messageServiceClient == null) {
            init();
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Request render: " + renderer.getName());
        }
        Properties msgProperties = new Properties();
        msgProperties.setProperty(RENDERER_NAME_KEY, renderer.getName());
        msgProperties.setProperty(COMMAND_KEY, "render");
        msgProperties.setProperty(Message.MESSAGE_TYPE, MESSAGE_TYPE);
        msgProperties.setProperty("source_id", messageServiceClient.toString());
        messageServiceClient.publish(
            "render broadcast to " + renderer.getName(),
            msgProperties,
            MessageServiceClient.RENDER_TOPIC_NAME);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Broadcast render message sent to " + renderer.getName());
        }
    }

    /**
     * Gracefully dispose of the BroadcastHub instance.
     */
    public synchronized void dispose() {
        try {
            messageServiceClient.stop();
            messageServiceClient.removeMessageHandler(
                messageHandler, MessageServiceClient.RENDER_TOPIC_NAME);
            renderManager = null;
            messageServiceClient.close();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to dispose JMS resources", exception);
            }
        }
    }
}

class RenderMessageHandler extends AbstractMessageHandler {
    private static final Log LOG =
        LogFactory.getLog(RenderMessageHandler.class);

    private RenderManager renderManager;

    public RenderMessageHandler(final RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    /**
     * This is the method that gets called when a new render message arrives.
     * Here we determine the name of the renderer to notify and get it from the
     * supplied RenderManager.  We then call the requestRender method of the
     * named renderer.
     * <p/>
     * It's important that the application doesn't trigger another call to
     * relayRenderRequest from this call stack as it will result in an infinite
     * loop and a resulting message storm.
     *
     * @param message the incoming message from the subscribed topic
     */
    public void handle(final Message message) {
        try {
            String rendererName =
                message.getStringProperty(BroadcastHub.RENDERER_NAME_KEY);
            String command =
                message.getStringProperty(BroadcastHub.COMMAND_KEY);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Message received: " + rendererName + "," + command);
            }
            if (renderManager == null) {
                String errMessage = "Render manager has not been set";
                if (LOG.isErrorEnabled()) {
                    LOG.error(errMessage);
                }
                throw new IllegalStateException(errMessage);
            }
            GroupAsyncRenderer renderer =
                (GroupAsyncRenderer)renderManager.getRenderer(rendererName);
            if (renderer != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Requesting renderer via: " + renderer);
                }
                renderer.requestRender(false);
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Could not find name renderer " + rendererName);
                }
            }
        } catch (Throwable throwable) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Problem processing message", throwable);
            }
        }
    }
}
