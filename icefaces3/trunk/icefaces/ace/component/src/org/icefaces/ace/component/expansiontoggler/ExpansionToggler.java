/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
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

package org.icefaces.ace.component.expansiontoggler;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.event.*;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.TreeDataModel;

import javax.el.MethodExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;

public class ExpansionToggler extends ExpansionTogglerBase {
    private boolean isRowExpansionRequest(FacesContext x, String tableId)    { return isIdPrefixedParamSet(tableId, "_rowExpansion", x); }
    private boolean isIdPrefixedParamSet(String tableId, String param, FacesContext x) {
        return x.getExternalContext().getRequestParameterMap().containsKey(tableId + param);
    }

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
        UIComponent parent = getParent();
        while (!(parent instanceof DataTable)) { parent = parent.getParent(); }
        DataTable table = (DataTable)parent;
        String tableId = table.getClientId(context);

        if (isRowExpansionRequest(context, tableId)) {
            RowState rowState = (RowState) context.getExternalContext().getRequestMap().get(table.getRowStateVar());
            if (rowState.isExpandable()) {
                rowState.setExpanded(!rowState.isExpanded());

                if (table.hasTreeDataModel())
                    queueEvent(new ExpansionChangeEvent(
                            this,
                            table.getRowData(),
                            rowState.isExpanded(),
                            ((TreeDataModel)table.getModel()).getRowEntry().getValue()));
                else
                    queueEvent(new ExpansionChangeEvent(this, table.getRowData(), rowState.isExpanded()));
            }
        }
    }

    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression me = null;

        if (event instanceof ExpansionChangeEvent) me = getChangeListener();

        if (me != null)
            me.invoke(context.getELContext(), new Object[] {event});
    }
}
