package org.icefaces.ace.component.list;

class ImmigrationRecord {
    Object value;
    int destination;
    boolean selected;

    public ImmigrationRecord(Object value, int destination, boolean selected) {
        this.value = value;
        this.destination = destination;
        this.selected = selected;
    }

    public Object getValue() {
        return value;
    }

    public int getDestination() {
        return destination;
    }

    public boolean isSelected() {
        return selected;
    }
}
