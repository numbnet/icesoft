package org.icefaces.ace.component.chart;

import java.util.Arrays;
import java.util.List;

public enum LegendPlacement {
    INSIDE_GRID, OUTSIDE_GRID, OUTSIDE;

    @Override
    public String toString() {
        List<String> parts = Arrays.asList(super.toString().split("_"));
        String name = parts.get(0).toLowerCase();

        for (String part : parts.subList(1, parts.size()))
            name += toProperCase(part);

        return name;
    }

    private String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
