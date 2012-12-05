package org.icefaces.ace.component.datatable;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 12/5/12
 * Time: 11:26 AM
 */
public class PageState {
    Integer rows;
    Integer page;

    public PageState() {}

    public PageState(DataTable table) {
        rows = table.getRows();
        page = table.getPage();
    }

    public PageState(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        String rowsParam = params.get(clientId + "_rows");
        String pageParam = params.get(clientId + "_page");

        rows = Integer.valueOf(rowsParam);
        page = Integer.valueOf(pageParam);
    }

    public void restoreState(DataTable table) {
        table.setRows(rows);
        table.setPage(page);
        table.setFirst((table.getPage() - 1) * table.getRows());
    }
}
