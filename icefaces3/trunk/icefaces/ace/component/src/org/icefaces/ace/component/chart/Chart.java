package org.icefaces.ace.component.chart;

import org.icefaces.ace.event.PointValueChangeEvent;
import org.icefaces.ace.event.SeriesSelectionEvent;
import org.icefaces.ace.event.TableFilterEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.chart.ChartSeries;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import java.util.ArrayList;
import java.util.List;

public class Chart extends ChartBase {
   public boolean hasAxisConfig() {
        return (getXAxis() != null) ||
               (getX2Axis() != null) ||
               (getYAxes() != null);
   }

    @Override
    public Object getValue() {
        final Object o = super.getValue();

        if (o instanceof List)
            return o;
        else if (o instanceof ChartSeries)
            return new ArrayList() {{
                add(o);
            }};
        else
            throw new RuntimeException("Chart component value is not an instance of, or a List of ChartSeries subclasses.");
    }

    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression me = null;

        if (event instanceof SeriesSelectionEvent) me = getSelectListener();
        if (event instanceof PointValueChangeEvent) me = getPointChangeListener();

        if (me != null)
            me.invoke(context.getELContext(), new Object[] {event});
    }

}