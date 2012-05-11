package org.icefaces.ace.component.chart;

import org.icefaces.ace.event.SeriesSelectionEvent;
import org.icefaces.ace.event.TableFilterEvent;
import org.icefaces.ace.event.UnselectEvent;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;

public class Chart extends ChartBase {
   public boolean hasAxisConfig() {
        return (getXAxis() != null) ||
               (getX2Axis() != null) ||
               (getYAxes() != null);
   }

    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression me = null;

        if (event instanceof SeriesSelectionEvent) me = getSelectListener();

        if (me != null)
            me.invoke(context.getELContext(), new Object[] {event});
    }

}