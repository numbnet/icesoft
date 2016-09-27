package org.icesoft.util;

import static org.icesoft.util.PreCondition.checkIfIsNotNull;
import static org.icesoft.util.StringUtilities.isNullOrIsEmpty;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesConfiguration
extends AbstractConfiguration
implements Configuration {
    private static final Logger LOGGER = Logger.getLogger(PropertiesConfiguration.class.getName());

    private final Properties properties = new Properties();

    private final String prefix;

    public PropertiesConfiguration(
        final Properties properties)
    throws NullPointerException {
        // throws NullPointerException
        this(properties, (String)null, (Configuration)null);
    }

    public PropertiesConfiguration(
        final Properties properties, final Configuration defaultConfiguration)
    throws NullPointerException {
        // throws NullPointerException
        this(properties, (String)null, defaultConfiguration);
    }

    public PropertiesConfiguration(
        final Properties properties, final String prefix)
    throws NullPointerException {
        // throws NullPointerException
        this(properties, prefix, (Configuration)null);
    }

    public PropertiesConfiguration(
        final Properties properties, final String prefix, final Configuration defaultConfiguration)
    throws NullPointerException {
        super(defaultConfiguration);
        getModifiableProperties().
            putAll(
                // throws NullPointerException
                checkIfIsNotNull(
                    properties, "Illegal argument properties: '" + properties + "'.  Argument cannot be null."
                )
            );
        this.prefix = prefix;
    }

    public String getAttribute(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        try {
            String _value =
                // throws ClassCastException
                (String)getModifiableProperties().get(_prefixedName);
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
        } catch (final ClassCastException exception) {
            throw new ConfigurationException("Illegal value for name '" + _prefixedName + "'.", exception);
        }
    }

    public Configuration getChildConfiguration(final String name)
    throws ConfigurationException {
        String _prefixedName = getPrefixedName(name);
        try {
            String _value =
                // throws ClassCastException
                (String)getModifiableProperties().get(_prefixedName);
            if (_value != null) {
                if (getDefaultConfiguration() != null) {
                    return
                        new PropertiesConfiguration(
                            getModifiableProperties(),
                            _prefixedName,
                            getDefaultConfiguration().getChildConfiguration(name)
                        );
                } else {
                    return
                        new PropertiesConfiguration(
                            getModifiableProperties(),
                            _prefixedName
                        );
                }
            } else {
                throw new ConfigurationException("Cannot find parameter: " + _prefixedName);
            }
        } catch (final ClassCastException exception) {
            throw new ConfigurationException("Illegal value for name '" + _prefixedName + "'.", exception);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getValue()
    throws ConfigurationException {
        try {
            String _value =
                // throws ClassCastException
                (String)getModifiableProperties().get(getPrefix());
            if (_value != null) {
                return _value;
            } else {
                throw new ConfigurationException("Cannot find parameter: " + getPrefix());
            }
        } catch (final ClassCastException exception) {
            throw new ConfigurationException("Illegal value for name '" + getPrefix() + "'.", exception);
        }
    }

    protected Properties getModifiableProperties() {
        return properties;
    }

    protected String getPrefixedName(final String name) {
        return (isNullOrIsEmpty(getPrefix())) ? name : getPrefix() + '.' + name;
    }
}
