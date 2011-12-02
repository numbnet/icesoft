package org.springframework.webflow.samples.booking;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class represents...
 *
 * @author Ben Simpson <ben.simpson@icesoft.com>
 *         Date: 3/4/11
 *         Time: 11:08 PM
 */
public class HotelModelBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sortColumn = "name";
    private boolean ascending = true;
    private Hotel selected = null;


    public List<Hotel> getHotels(SearchCriteria searchCriteria, BookingService bookingService) {
        List<Hotel> hotels = bookingService.findHotels(searchCriteria,0,sortColumn,true);
        return hotels;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }


    public String selectHotelAction() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //note, the attribute used below "hotel" matches the var attribute of the data table
        Map requestMap = externalContext.getRequestMap();
        this.selected = (Hotel) requestMap.get("hotel");
        return "select";
    }

    public Hotel getSelected() {
        return selected;
    }

    public void setSelected(Hotel selected) {
        this.selected = selected;
    }
}
