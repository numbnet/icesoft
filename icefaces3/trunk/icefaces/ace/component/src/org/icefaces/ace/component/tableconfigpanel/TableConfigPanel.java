package org.icefaces.ace.component.tableconfigpanel;


import org.icefaces.ace.component.datatable.DataTable;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
public class TableConfigPanel extends TableConfigPanelBase {
    // Find component cache
    private DataTable table;

    public DataTable getTargetedDatatable() {
        if (this.table != null) return table;

        String target = this.getFor();
        if (target == null) {
            // If nested in table
            UIComponent parent = this;
            while ((parent = parent.getParent()) != null)
                if (parent instanceof DataTable) {
                    ((DataTable) parent).setTableConfigPanel(this);
                    this.table = (DataTable)parent;
                    return (DataTable)parent;
                }
            throw new FacesException("TableConfigPanel: Must be nested within a DataTable or target one using the 'for' attribute.");
        }

        DataTable table = (DataTable)this.findComponent(target);

        if (table == null) throw new FacesException("TableConfigPanel: DataTable with clientId determined by 'for' attribute value '" + target + "' could not be found.");

       // table.setTableConfigPanel(this);
        this.table = table;
        return table;
    }

    public void setInView(boolean isInView) {
        getTargetedDatatable().setTableConfigPanel(this);
        super.setInView(isInView);
    }
}
