package org.springframework.webflow.samples.booking;

import javax.faces.model.DataModel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class represents...
 *
 * @author Ben Simpson <ben.simpson@icesoft.com>
 *         Date: 3/4/11
 *         Time: 1:18 PM
 */
public abstract class LazyDataModel<T> extends DataModel implements Serializable {

	private int rowIndex = -1;

	private int rowCount;

	private int pageSize;

	private List<T> data;

	public LazyDataModel() {
		super();
	}

	public boolean isRowAvailable() {
		if(data == null) {
            return false;
        }

		return rowIndex >= 0 && rowIndex < data.size();
	}

	public int getRowCount() {
		return rowCount;
	}

	public Object getRowData() {
        return data.get(rowIndex);
	}

    public int getRowIndex() {
        return this.rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        if(pageSize == 0) {
            return;
        }
        this.rowIndex = (rowIndex % pageSize);
    }

	public Object getWrappedData() {
		return data;
	}
	public void setWrappedData(Object list) {
		this.data = (List) list;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public abstract List<T> load(int first, int pageSize, String sortField, boolean sortOrder, Map<String,String> filters);
}
