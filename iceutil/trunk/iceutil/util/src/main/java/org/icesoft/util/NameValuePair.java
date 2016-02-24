package org.icesoft.util;

import static org.icesoft.util.ObjectUtilities.isNotNull;
import static org.icesoft.util.PreCondition.checkArgument;
import static org.icesoft.util.StringUtilities.isNotNullAndIsNotEmpty;

import java.util.logging.Logger;

public class NameValuePair<N extends String, V> {
    private static final Logger LOGGER = Logger.getLogger(NameValuePair.class.getName());

    private final N name;
    private final V value;

    public NameValuePair(final N name, final V value)
    throws IllegalArgumentException {
        checkArgument(
            isNotNullAndIsNotEmpty(name), "Illegal argument name: '" + name + "'.  Argument cannot be null or empty."
        );
        checkArgument(
            isNotNull(value), "Illegal argument value: '" + value + "'.  Argument cannot be null."
        );
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(final Object object) {
        return
            object instanceof NameValuePair &&
                ((NameValuePair)object).getName().equals(getName()) &&
                ((NameValuePair)object).getValue().equals(getValue());
    }

    public N getName() {
        return name;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("NameValuePair[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }

    protected String classMembersToString() {
        return
            new StringBuilder().
                append("name: '").append(getName()).append("', ").
                append("value: '").append(getValue()).append("'").
                    toString();
    }
}
