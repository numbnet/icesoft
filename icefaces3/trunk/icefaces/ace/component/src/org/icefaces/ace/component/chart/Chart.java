package org.icefaces.ace.component.chart;

public class Chart extends ChartBase {
   public boolean hasAxisConfig() {
        return (getXAxis() != null) ||
               (getX2Axis() != null) ||
               (getYAxes() != null);
   }
}
