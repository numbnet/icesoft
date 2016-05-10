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

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractConfiguration
implements Configuration {
    private static final Logger LOGGER = Logger.getLogger(AbstractConfiguration.class.getName());

    private final Configuration defaultConfiguration;

    protected AbstractConfiguration() {
        this(null);
    }

    protected AbstractConfiguration(final Configuration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    public String getAttribute(final String name, final String defaultValue) {
        try {
            // throws ConfigurationException
            return getAttribute(name);
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getAttribute(name, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public boolean getAttributeAsBoolean(final String name)
    throws ConfigurationException {
        try {
            return
                Boolean.valueOf(
                    // throws ConfigurationException
                    getAttribute(name)
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttributeAsBoolean(name);
            } else {
                throw exception;
            }
        }
    }

    public boolean getAttributeAsBoolean(final String name, final boolean defaultValue) {
        try {
            // throws ConfigurationException
            return getAttributeAsBoolean(name);
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getAttributeAsBoolean(name, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public double getAttributeAsDouble(final String name)
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Double.parseDouble(
                    // throws ConfigurationException
                    getAttribute(name)
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttributeAsDouble(name);
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public double getAttributeAsDouble(final String name, final double defaultValue) {
        try {
            // throws ConfigurationException
            return getAttributeAsDouble(name);
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getAttributeAsDouble(name, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public float getAttributeAsFloat(final String name)
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Float.parseFloat(
                    // throws ConfigurationException
                    getAttribute(name)
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttributeAsFloat(name);
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public float getAttributeAsFloat(final String name, final float defaultValue) {
        try {
            // throws ConfigurationException
            return getAttributeAsFloat(name);
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getAttributeAsFloat(name, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public int getAttributeAsInteger(final String name)
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Integer.parseInt(
                    // throws ConfigurationException
                    getAttribute(name)
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttributeAsInteger(name);
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public int getAttributeAsInteger(final String name, final int defaultValue) {
        try {
            // throws ConfigurationException
            return getAttributeAsInteger(name);
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getAttributeAsInteger(name, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public long getAttributeAsLong(final String name)
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Long.parseLong(
                    // throws ConfigurationException
                    getAttribute(name)
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getAttributeAsLong(name);
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public long getAttributeAsLong(final String name, final long defaultValue) {
        try {
            // throws ConfigurationException
            return getAttributeAsLong(name);
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getAttributeAsLong(name, defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public String getValue(final String defaultValue) {
        try {
            // throws ConfigurationException
            return getValue();
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getValue(defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public boolean getValueAsBoolean()
    throws ConfigurationException {
        try {
            return
                Boolean.valueOf(
                    // throws ConfigurationException
                    getValue()
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getValueAsBoolean();
            } else {
                throw exception;
            }
        }
    }

    public boolean getValueAsBoolean(final boolean defaultValue) {
        try {
            // throws ConfigurationException
            return getValueAsBoolean();
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getValueAsBoolean(defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public double getValueAsDouble()
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Double.parseDouble(
                    // throws ConfigurationException
                    getValue()
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getValueAsDouble();
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public double getValueAsDouble(final double defaultValue) {
        try {
            // throws ConfigurationException
            return getValueAsDouble();
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getValueAsDouble(defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public float getValueAsFloat()
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Float.parseFloat(
                    // throws ConfigurationException
                    getValue()
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getValueAsFloat();
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public float getValueAsFloat(final float defaultValue) {
        try {
            // throws ConfigurationException
            return getValueAsFloat();
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getValueAsFloat(defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public int getValueAsInteger()
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Integer.parseInt(
                    // throws ConfigurationException
                    getValue()
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getValueAsInteger();
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public int getValueAsInteger(final int defaultValue) {
        try {
            // throws ConfigurationException
            return getValueAsInteger();
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getValueAsInteger(defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    public long getValueAsLong()
    throws ConfigurationException {
        try {
            return
                // throws NumberFormatException
                Long.parseLong(
                    // throws ConfigurationException
                    getValue()
                );
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                // throws ConfigurationException
                return getDefaultConfiguration().getValueAsLong();
            } else {
                throw exception;
            }
        } catch (final NumberFormatException exception) {
            throw new ConfigurationException(exception);
        }
    }

    public long getValueAsLong(final long defaultValue) {
        try {
            // throws ConfigurationException
            return getValueAsLong();
        } catch (final ConfigurationException exception) {
            if (hasDefaultConfiguration()) {
                return getDefaultConfiguration().getValueAsLong(defaultValue);
            } else {
                return defaultValue;
            }
        }
    }

    protected Configuration getDefaultConfiguration() {
        return defaultConfiguration;
    }

    protected boolean hasDefaultConfiguration() {
        return getDefaultConfiguration() != null;
    }
}
