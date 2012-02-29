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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
package com.icesoft.net.messaging.jms;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.net.messaging.MessageServiceConfiguration;
import com.icesoft.net.messaging.MessageServiceConfigurationProperties;

import java.io.InputStream;
import java.io.IOException;

public class JMSProviderConfigurationProperties
extends MessageServiceConfigurationProperties
implements JMSProviderConfiguration, MessageServiceConfiguration {
    public JMSProviderConfigurationProperties(final Configuration configuration) {
        super(configuration);
    }

    public JMSProviderConfigurationProperties(final InputStream inputStream, final Configuration configuration)
    throws IOException {
        super(inputStream, configuration);
    }

    public String getInitialContextFactory() {
        return
            configuration.getAttribute(
                INITIAL_CONTEXT_FACTORY,
                messageServiceConfigurationProperties.getProperty(INITIAL_CONTEXT_FACTORY, null));
    }

    public String getPassword() {
        return
            configuration.getAttribute(
                PASSWORD,
                messageServiceConfigurationProperties.getProperty(PASSWORD, null));
    }

    public String getProviderURL() {
        return
            configuration.getAttribute(
                PROVIDER_URL,
                messageServiceConfigurationProperties.getProperty(PROVIDER_URL, null));
    }

    public String getTopicConnectionFactoryName() {
        return
            configuration.getAttribute(
                TOPIC_CONNECTION_FACTORY_NAME,
                messageServiceConfigurationProperties.getProperty(TOPIC_CONNECTION_FACTORY_NAME, "ConnectionFactory"));
    }

    public String getTopicNamePrefix() {
        return
            configuration.getAttribute(
                TOPIC_NAME_PREFIX,
                messageServiceConfigurationProperties.getProperty(TOPIC_NAME_PREFIX, null));
    }

    public String getURLPackagePrefixes() {
        return
            configuration.getAttribute(
                URL_PACKAGE_PREFIXES,
                messageServiceConfigurationProperties.getProperty(URL_PACKAGE_PREFIXES, null));
    }

    public String getUserName() {
        return
            configuration.getAttribute(
                USER_NAME,
                messageServiceConfigurationProperties.getProperty(USER_NAME, null));
    }
}
