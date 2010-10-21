package org.icepush.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Add Group Member Request
 *
 * Request-URI:
 *     /<servlet-context-path>/add-group-member.icepush
 *
 * Entity Body:
 *     id=<push-id>&group=<group-name>
 */
public class AddGroupMemberRequest
extends HttpRequest {
    private static final Logger LOGGER = Logger.getLogger(AddGroupMemberRequest.class.getName());

    public AddGroupMemberRequest(final String groupName, final String pushId, final String contextURI)
    throws URISyntaxException {
        super(
            Method.POST,                                                                                       // Method
            // throws URISyntaxException
            new URI(contextURI + "/add-group-member.icepush").normalize(),                                // Request-URI
            toForm(groupName, pushId).getBytes());                                                        // Entity-Body
        // todo: Remove this logging.
        LOGGER.log(
            Level.INFO,
            "[Jack] " +
                "AddGroupMemberRequest(" +
                    "groupName: '" + groupName + "', " +
                    "pushId: '" + pushId + "', " +
                    "contextURI: '" + contextURI + "')");
    }

    private static String toForm(final String groupName, final String pushId) {
        return new StringBuilder().append("group=").append(groupName).append("&id=").append(pushId).toString();
    }
}
