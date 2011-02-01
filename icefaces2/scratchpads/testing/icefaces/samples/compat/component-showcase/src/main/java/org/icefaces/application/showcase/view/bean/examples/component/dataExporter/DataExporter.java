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

package org.icefaces.application.showcase.view.bean.examples.component.dataExporter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
@ManagedBean(name = "dataExporter")
@ViewScoped
public class DataExporter extends DataTableBase{
    private Effect changeEffect;
    private String type;

    public DataExporter() {
        changeEffect = new Highlight("#fda505");
        changeEffect.setFired(true);
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Effect getChangeEffect() {
        return changeEffect;
    }

    public void setChangeEffect(Effect changeEffect) {
        this.changeEffect = changeEffect;
    }
    
    public void typeChangeListener(ValueChangeEvent event){
        this.changeEffect.setFired(false);
    }
}
