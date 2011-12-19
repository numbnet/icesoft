package org.icefaces.ace.event;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.datatable.DataTable;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import java.util.List;

public class TableFilterEvent extends FacesEvent {
    private Column column;

    public TableFilterEvent(UIComponent component, Column filteredColumn) {
        super(component);
        this.column = filteredColumn;
    }

    @Override
    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    @Override
    public void processListener(FacesListener facesListener) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the column of this TableFilterEvent.
     * @return Column that has had it's filter altered
     */
    public Column getColumn() {
        return column;
    }
}