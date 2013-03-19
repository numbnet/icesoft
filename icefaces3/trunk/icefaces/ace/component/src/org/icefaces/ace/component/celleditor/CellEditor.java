/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.celleditor;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.el.ValueExpression;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.datatable.DataTableConstants;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.util.HTML;
import org.icefaces.resources.ICEResourceDependencies;
import java.util.List;
import java.util.ArrayList;

@ICEResourceDependencies({

})
public class CellEditor extends CellEditorBase {
    DataTable table = null;

    protected DataTable findParentTable(FacesContext context) {
        if (table != null) return table;

        UIComponent parent = this.getParent();

        while(parent != null)
            if (parent instanceof DataTable) {
                table = (DataTable) parent;
                break;
            }
            else parent = parent.getParent();

        return table;
    }

    public void processUpdates(FacesContext context) {
        DataTable table = findParentTable(context);
        RowState rowState = (RowState) context.getExternalContext().getRequestMap().get(table.getRowStateVar());
        List<String> selectedEditorIds = rowState.getActiveCellEditorIds();

        if (selectedEditorIds.contains(this.getId())) {
            this.getFacet("input").setRendered(true);
        } else {
            this.getFacet("input").setRendered(false);
        }

        super.processUpdates(context);
    }
}