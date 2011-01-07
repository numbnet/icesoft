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

package org.icefaces.application.showcase.view.bean.examples.component.dataPaginator;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.util.CoreComponentUtils;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class DataScrollingController {
    // Used in this example to reset the paginator when moving between
    // scrolling views, not needed in normal application development. 
    private DataPaginator dataPaginatorBinding;


    public DataPaginator getDataPaginatorBinding() {
        return dataPaginatorBinding;
    }

    public void setDataPaginatorBinding(DataPaginator dataPaginatorBinding) {
        this.dataPaginatorBinding = dataPaginatorBinding;
    }

    public void dataModelChangeListener(ValueChangeEvent event){
        String oldPagingValue = (String)event.getOldValue();
        if (oldPagingValue.equals(DataScrollingModel.PAGINATOR_SCROLLING) &&
                dataPaginatorBinding != null){
            dataPaginatorBinding.gotoFirstPage();
        }
    }
}
