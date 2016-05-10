/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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
package org.icesoft.util;

import static org.icesoft.util.StringUtilities.isNullOrIsEmpty;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemConfiguration
extends AbstractConfiguration
implements Configuration {
    private static final Logger LOGGER = Logger.getLogger(SystemConfiguration.class.getName());

    private final String prefix;

    public SystemConfiguration() {
        this(null, null);
    }

    public SystemConfiguration(final Configuration defaultConfiguration) {
        this(null, defaultConfiguration);
    }

    public SystemConfiguration(final String prefix) {
        this(prefix, null);
    }

    public SystemConfiguration(final String prefix, final Configuration defaultConfiguration) {
        super(defaultConfiguration);
        this.prefix = prefix;
    }

    public String getAttribute(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        String _value = System.getProperty(_prefixedName);
        if (_value != null) {
            return _value;
        } else {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttribute(name);
            } else {
                throw new ConfigurationException("Cannot find parameter: " + _prefixedName);
            }
        }
    }

    public Configuration getChildConfiguration(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        String _value = System.getProperty(_prefixedName);
        if (_value != null) {
            if (getDefaultConfiguration() != null) {
                return
                    new SystemConfiguration(
                        _prefixedName, getDefaultConfiguration().getChildConfiguration(name)
                    );
            } else {
                return
                    new SystemConfiguration(
                        _prefixedName
                    );
            }
        } else {
            throw new ConfigurationException("Cannot find parameter: " + _prefixedName);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getValue()
    throws ConfigurationException {
        String _value = System.getProperty(getPrefix());
        if (_value != null) {
            return _value;
        } else {
            throw new ConfigurationException("Cannot find parameter: " + getPrefix());
        }
    }

    private String getPrefixedName(final String name) {
        return (isNullOrIsEmpty(getPrefix())) ? name : getPrefix() + '.' + name;
    }
}
