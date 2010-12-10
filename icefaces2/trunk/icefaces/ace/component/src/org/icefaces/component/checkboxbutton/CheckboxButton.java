package org.icefaces.component.checkboxbutton;

import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;



public class CheckboxButton extends CheckboxButtonBase {

    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }

}

