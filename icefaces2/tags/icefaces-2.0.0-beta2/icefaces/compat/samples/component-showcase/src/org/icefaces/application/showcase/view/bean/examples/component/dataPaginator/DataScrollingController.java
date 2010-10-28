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
