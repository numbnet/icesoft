package org.icefaces.ace.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 5/11/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeriesSelectionEvent extends FacesEvent {
    int seriesIndex;
    int pointIndex;

    public SeriesSelectionEvent(UIComponent c, int seriesIndex, int pointIndex) {
        super(c);
        this.seriesIndex = seriesIndex;
        this.pointIndex = pointIndex;
    }

    public SeriesSelectionEvent(UIComponent component) {
        super(component);
    }

    @Override
    public boolean isAppropriateListener(FacesListener facesListener) {
        return true;
    }

    @Override
    public void processListener(FacesListener facesListener) {
        throw new UnsupportedOperationException();
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(int pointIndex) {
        this.pointIndex = pointIndex;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    public void setSeriesIndex(int seriesIndex) {
        this.seriesIndex = seriesIndex;
    }
}
