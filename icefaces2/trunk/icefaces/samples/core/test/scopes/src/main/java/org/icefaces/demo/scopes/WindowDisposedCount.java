/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.demo.scopes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

import org.icefaces.bean.WindowDisposed;

@ManagedBean(name = "WindowDisposedCount")
@SessionScoped
public class WindowDisposedCount {
    int value = 0;

    public int getValue()  {
        return value;
    }

    public void setValue(int value)  {
        this.value = value;
    }
}
