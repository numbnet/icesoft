package org.icefaces.component.pushbutton;

import org.icefaces.component.utils.Utils;
import org.icefaces.impl.util.Util;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;

public class PushButton extends PushButtonBase {

    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }

	

}
