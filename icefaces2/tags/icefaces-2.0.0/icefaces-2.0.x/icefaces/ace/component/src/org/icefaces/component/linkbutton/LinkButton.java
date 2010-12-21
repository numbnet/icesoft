package org.icefaces.component.linkbutton;

import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;

public class LinkButton extends LinkButtonBase {

    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }
}